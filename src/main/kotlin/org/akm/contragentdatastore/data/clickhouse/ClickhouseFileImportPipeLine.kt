package org.akm.contragentdatastore.data.clickhouse

import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition
import java.io.InputStream

abstract class ClickhouseFileImportPipeLine : FileImportPipeline {
    override fun executePipeline(filePath: String, schemaDefinition: TableDefinition, schemaName: String,
    onComplete: () -> Unit, onError: (error: String) -> Unit
    ) {
        try {
            val inputStream = readFile(filePath)
            val parsedData = parseFile(inputStream)
            val transformedData = transformData(parsedData, schemaDefinition)
            uploadToClickHouse(transformedData, schemaDefinition, schemaName)
            onComplete()
        } catch (e: Exception) {
            onError(e.message ?: "Unknown error")
        }
    }

    protected abstract fun readFile(filePath: String): InputStream

    protected abstract fun parseFile(inputStream: InputStream): List<Map<String, Any>>

    protected abstract fun transformData(parsedData: List<Map<String, Any>>, schema: TableDefinition): List<Map<String, Any>>

    protected abstract fun uploadToClickHouse(data: List<Map<String, Any>>, schema: TableDefinition, schemaName: String)
}