package org.akm.contragentdatastore.data.schemaindex.entity

import jakarta.persistence.*
import org.akm.contragentdatastore.core.service.files.model.Status
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "import_history")
class ImportStatusEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID? = null,
    val startedAt: LocalDateTime,
    var finishedAt: LocalDateTime? = null,
    var status: Status,
    var message: String? = null,
)
