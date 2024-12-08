package org.akm.contragentdatastore.api.rest.dto

import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition

data class DefineSchemaRequest(
    val schema: String,
    val tableDefinition: TableDefinition,
)
