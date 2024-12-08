package org.akm.contragentdatastore.data.model

data class TableDefinition(
    val columns: List<ColumnDefinition>,
    val engine: ClickhouseEngineType,
    val orderBy: String?,
    val primaryKey: String?,
)
