package org.akm.contragentdatastore.data.schemaindex

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.akm.contragentdatastore.core.exception.SchemaParsingException
import org.akm.contragentdatastore.data.schemaindex.model.TableDefinition

@Converter
class TableAttributeConverter : AttributeConverter<TableDefinition, String> {
    override fun convertToDatabaseColumn(p0: TableDefinition?): String {
        return try {
            objectMapper.writeValueAsString(p0)
        } catch (e: JsonProcessingException) {
            throw SchemaParsingException("Could not parse table attribute data: ${e.message}")
        }
    }

    override fun convertToEntityAttribute(p0: String?): TableDefinition {
        try {
            return objectMapper.readValue(p0, TableDefinition::class.java)
        } catch (e: JsonProcessingException) {
            throw SchemaParsingException("Could not parse table attribute data: ${e.message}")
        }
    }

    companion object {
        val objectMapper = jacksonObjectMapper()
    }
}