package org.akm.contragentdatastore.core.service.files.model

data class ImportStatus(
    val status: Status,
    val message: String
)

enum class Status {
    COMPLETED,
    FAILED,
    ACTIVE,
}
