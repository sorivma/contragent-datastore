package org.akm.contragentdatastore.core.service.schema

import org.akm.contragentdatastore.api.rest.dto.DefineSchemaRequest
import org.akm.contragentdatastore.data.schemaindex.entity.SchemaEntity
import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition

interface SchemaService {
    fun defineSchema(schemaRequest: DefineSchemaRequest): String
    fun getSchemas(): List<SchemaEntity>
    fun getDefinition(schemaName: String): TableDefinition
}