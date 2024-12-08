package org.akm.contragentdatastore.data.clickhouse

import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition
import java.io.InputStream

interface FileImportPipeline {
    fun executePipeline(
        filePath: String,
        schemaDefinition: TableDefinition,
        schemaName: String,
        onComplete: () -> Unit,
        onError: (error: String) -> Unit
    )
}