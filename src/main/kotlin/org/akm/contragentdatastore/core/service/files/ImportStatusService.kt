package org.akm.contragentdatastore.core.service.files.model

import org.akm.contragentdatastore.data.schemaindex.entity.ImportStatusEntity
import java.util.UUID

interface ImportStatusService {
    fun createImportStatus(): ImportStatusEntity
    fun getImportStatus(id: UUID): ImportStatusEntity
    fun updateImportStatus(id: UUID, status: ImportStatusEntity): ImportStatusEntity
}