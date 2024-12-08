package org.akm.contragentdatastore.data.clickhouse.impl

import org.akm.contragentdatastore.data.clickhouse.ClickhouseFileImportPipeLine
import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
@Qualifier("minio-import-datastore")
class MinioImportPipeLine : ClickhouseFileImportPipeLine() {
    override fun readFile(filePath: String): InputStream {
        TODO("Not yet implemented")
    }

    override fun parseFile(inputStream: InputStream): List<Map<String, Any>> {
        TODO("Not yet implemented")
    }

    override fun transformData(parsedData: List<Map<String, Any>>, schema: TableDefinition): List<Map<String, Any>> {
        TODO("Not yet implemented")
    }

    override fun uploadToClickHouse(data: List<Map<String, Any>>, schema: TableDefinition) {
        TODO("Not yet implemented")
    }
}