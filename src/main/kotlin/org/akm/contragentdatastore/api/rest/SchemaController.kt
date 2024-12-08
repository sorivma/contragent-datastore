package org.akm.contragentdatastore.api.rest

import org.akm.contragentdatastore.core.service.schema.SchemaService
import org.akm.contragentdatastore.api.rest.dto.DefineSchemaRequest
import org.akm.contragentdatastore.data.schemaindex.entity.SchemaEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("${Routing.APIV1}${Routing.APIv1.SCHEMA}")
class SchemaController(
    private val schemaService: SchemaService
) {
    @PostMapping(Routing.APIv1.Schema.CREATE)
    fun create(@RequestBody defineSchemaRequest: DefineSchemaRequest) = schemaService.defineSchema(defineSchemaRequest)

    @GetMapping(Routing.APIv1.Schema.ALL)
    fun getAll(): List<SchemaEntity> {
        return schemaService.getSchemas()
    }
}