package org.akm.contragentdatastore.data.clickhouse.impl

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import jakarta.transaction.Transactional
import org.akm.contragentdatastore.core.service.files.MinioService
import org.akm.contragentdatastore.data.clickhouse.ClickhouseFileImportPipeLine
import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition
import org.hibernate.query.sqm.ParsingException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
@Qualifier("minio-import-datastore-csv")
class MinioCsvImportPipeLine(
    private val minioService: MinioService,
    @Qualifier("clickhouse")
    private val jdbcTemplate: JdbcTemplate,
) : ClickhouseFileImportPipeLine() {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun readFile(filePath: String): InputStream {
        return minioService.getObject("contr-agent-bucket", filePath)
    }

    override fun parseFile(inputStream: InputStream): List<Map<String, Any>> {
        val parser = CSVParserBuilder().withSeparator(',').build()

        val reader = CSVReaderBuilder(InputStreamReader(inputStream))
            .withCSVParser(parser)
            .build()

        val records = mutableListOf<Map<String, Any>>()

        val rawHeaders = reader.readNext()
        if (rawHeaders != null) {
            val headers = rawHeaders.map { it.trim() }
            log.info("Parsed headers: $headers")

            var row: Array<String>?
            while (reader.readNext().also { row = it } != null) {
                if (row!!.size != headers.size) {
                    log.warn("Row size ${row!!.size} does not match headers size ${headers.size}: ${row!!.toList()}")
                    continue // Пропускаем такую строку
                }

                val rowMap = headers.zip(row!!.map { it.trim() }).toMap()
                records.add(rowMap)
            }
        } else {
            log.warn("No headers found in CSV.")
        }

        return records
    }

    override fun transformData(parsedData: List<Map<String, Any>>, schema: TableDefinition): List<Map<String, Any>> {
        if (parsedData.isEmpty()) {
            log.warn("Parsed data is empty!")
            return emptyList()
        }

        val firstRow = parsedData.first()
        val availableKeys = firstRow.keys
        log.info("Keys available in CSV rows: $availableKeys")

        val transformed = parsedData.mapIndexed { index, row ->
            val rowWithMappedColumns = schema.columns.associate { column ->
                val key = column.dataSetName?.trim() ?: column.name.trim()
                log.debug("Row $index: Looking for key '$key'")

                val value = row[key]
                if (value == null) {
                    log.error("Row $index: Missing column '$key'. Available keys: ${row.keys}")
                    throw ParsingException("Could not find column named '$key'")
                }

                column.name to value
            }.toMutableMap()

            rowWithMappedColumns["import_timestamp"] = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            rowWithMappedColumns
        }

        log.info("Successfully transformed ${transformed.size} rows")
        return transformed
    }

    @Async
    @Transactional
    override fun uploadToClickHouse(data: List<Map<String, Any>>, schema: TableDefinition, schemaName: String) {
        val batchSize = 1000
        val sql = buildInsertSql(schema, schemaName)

        val batches = data.chunked(batchSize)
        batches.forEach { batch ->
            val batchArgs = batch.map { row ->
                schema.columns.map { column ->
                    row[column.name] ?: ""
                }.toTypedArray() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }
            jdbcTemplate.batchUpdate(sql, batchArgs)
        }

    }

    private fun buildInsertSql(schema: TableDefinition, schemaName: String): String {
        val columns = schema.columns.joinToString(", ") { it.name } + ",import_timestamp"
        val placeholders = schema.columns.joinToString(", ") { "?" }
        return "INSERT INTO $schemaName ($columns) VALUES ($placeholders)"
    }
}