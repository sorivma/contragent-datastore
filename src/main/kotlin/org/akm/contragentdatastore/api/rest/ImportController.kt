package org.akm.contragentdatastore.api.rest

import org.akm.contragentdatastore.api.rest.dto.ImportFileRequest
import org.akm.contragentdatastore.api.rest.dto.ImportTaskCreatedResponse
import org.akm.contragentdatastore.core.service.files.FileImportService
import org.akm.contragentdatastore.core.service.files.ImportStatusService
import org.akm.contragentdatastore.data.schemaindex.entity.ImportStatusEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("${Routing.APIV1}${Routing.APIv1.DATA}")
class DataController(
    private val fileImportService: FileImportService,
    private val importStatusService: ImportStatusService,
) {
    @PostMapping(Routing.APIv1.Data.IMPORT)
    fun importData(@RequestBody importFileRequest: ImportFileRequest): ImportTaskCreatedResponse {
        return ImportTaskCreatedResponse(
            statusId = fileImportService.importFile(
                fileName = importFileRequest.fileName,
                schemaName = importFileRequest.schemaName,
                format = importFileRequest.format
            ).id.toString()
        )
    }

    @PostMapping(Routing.APIv1.Data.IMPORT_ORGS)
    fun importOrganizations(): ImportStatusEntity {
        return fileImportService.importOrganzations(importStatusService.createImportStatus())
    }

    @PostMapping(Routing.APIv1.Data.IMPORT_PERSONS)
    fun importPersons(): ImportStatusEntity {
        return fileImportService.importPersons(importStatusService.createImportStatus())
    }

    @GetMapping("${Routing.APIv1.Data.IMPORT_STATUS}/{statusId}")
    fun getImportStatus(@PathVariable(value = "statusId") statusId: UUID): ImportStatusEntity {
        return importStatusService.getImportStatus(statusId)
    }
}