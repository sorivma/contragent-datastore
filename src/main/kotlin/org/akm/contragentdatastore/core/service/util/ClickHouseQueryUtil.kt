package org.akm.contragentdatastore.core.service.util

import org.akm.contragentdatastore.data.schemaindex.entity.SchemaEntity
import org.akm.contragentdatastore.data.schemaindex.model.ColumnDefinition

object ClickHouseQueryUtil {
    fun SchemaEntity.toClickHouseQuery(): String {
        val importTimeStampColumn = ColumnDefinition(
            name = "import_timestamp",
            type = "TIMESTAMP",
        )

        val columnDefs = (table.columns + importTimeStampColumn).joinToString(", ") {
            "${it.name} ${it.type}"
        }

        val orderBy = table.orderBy

        return """
            |CREATE TABLE IF NOT EXISTS $schemaName
            | ($columnDefs) ENGINE = ${table.engine.type}
            | ORDER BY ($orderBy, ${importTimeStampColumn.name}) PRIMARY KEY (${table.primaryKey}, import_timestamp);
        """.trimMargin()
    }
}