package org.akm.contragentdatastore.core.service.files.impl

import jakarta.persistence.EntityNotFoundException
import org.akm.contragentdatastore.core.service.files.ImportStatusService
import org.akm.contragentdatastore.core.service.files.model.Status
import org.akm.contragentdatastore.data.schemaindex.entity.ImportStatusEntity
import org.akm.contragentdatastore.data.schemaindex.repository.StatusRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
data class ImportStatusServiceImpl(
    private val importStatusRepository: StatusRepository,
    private val statusRepository: StatusRepository
) : ImportStatusService {
    override fun createImportStatus(): ImportStatusEntity {
        return importStatusRepository.save(
            ImportStatusEntity(
                status = Status.ACTIVE,
                startedAt = LocalDateTime.now()
            )
        )
    }

    override fun getImportStatus(id: UUID): ImportStatusEntity {
        return importStatusRepository.findById(id).orElseThrow { EntityNotFoundException("ImportStatus $id not found") }
    }

    override fun updateImportStatus(importStatusEntity: ImportStatusEntity): ImportStatusEntity {
        return statusRepository.save(importStatusEntity)
    }

    override fun getHistory(pageable: Pageable): Page<ImportStatusEntity> {
        return importStatusRepository.findAll(pageable)
    }
}