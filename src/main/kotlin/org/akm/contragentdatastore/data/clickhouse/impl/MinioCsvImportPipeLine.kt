package org.akm.contragentdatastore.data.clickhouse.impl

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import jakarta.transaction.Transactional
import org.akm.contragentdatastore.core.service.files.MinioService
import org.akm.contragentdatastore.data.clickhouse.ClickhouseFileImportPipeLine
import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition
import org.hibernate.query.sqm.ParsingException
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
    override fun readFile(filePath: String): InputStream {
        return minioService.getObject("contragent-data", filePath)
    }

    override fun parseFile(inputStream: InputStream): List<Map<String, Any>> {
        val parser =CSVParserBuilder().withSeparator(';').build()

        val reader = CSVReaderBuilder(InputStreamReader(inputStream))
            .withCSVParser(parser)
            .build()


        val records = mutableListOf<Map<String, Any>>()

        val headers = reader.readNext()
        if (headers != null) {
            var row: Array<String>?
            while (reader.readNext().also { row = it } != null) {
                val rowMap = headers.zip(row!!).toMap()
                records.add(rowMap)
            }
        }
        return records
    }

    override fun transformData(parsedData: List<Map<String, Any>>, schema: TableDefinition): List<Map<String, Any>> {
        return parsedData.map { row ->
            val rowWithTimestamp = schema.columns.associate { column ->
                column.name to (row[column.dataSetName ?: column.name]
                    ?: throw ParsingException("Could not find column named ${column.dataSetName ?: column.name}"))
            }.toMutableMap()

            rowWithTimestamp["import_timestamp"] =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            rowWithTimestamp
        }
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