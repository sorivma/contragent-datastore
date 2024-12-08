package org.akm.contragentdatastore.data.schemaindex.model

data class TableDefinition(
    val columns: List<ColumnDefinition>,
    val engine: ClickhouseEngineType,
    val orderBy: String = columns.first().name,
    val primaryKey: String = columns.first().name,
)
