package org.akm.contragentdatastore.core.service.files

import org.akm.contragentdatastore.data.schemaindex.entity.ImportStatusEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ImportStatusService {
    fun createImportStatus(): ImportStatusEntity
    fun getImportStatus(id: UUID): ImportStatusEntity
    fun updateImportStatus(importStatusEntity: ImportStatusEntity): ImportStatusEntity
    fun getHistory(pageable: Pageable): Page<ImportStatusEntity>
}