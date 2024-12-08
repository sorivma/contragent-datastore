package org.akm.contragentdatastore.core.service.schema.dto

import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition

data class DefineSchemaRequest(
    val schema: String,
    val tableDefinition: TableDefinition,
)
