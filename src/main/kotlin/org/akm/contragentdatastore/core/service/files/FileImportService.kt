package org.akm.contragentdatastore.core.service.files

import org.akm.contragentdatastore.core.service.files.model.FileFormat
import org.akm.contragentdatastore.data.schemaindex.entity.ImportStatusEntity

interface FileImportService {
    fun importFile(fileName: String, schemaName: String, format: FileFormat): ImportStatusEntity
    fun importOrganzations(importStatuses: ImportStatusEntity): ImportStatusEntity
    fun importPersons(importStatuses: ImportStatusEntity): ImportStatusEntity
}