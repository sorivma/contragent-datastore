package org.akm.contragentdatastore.api.rest.dto

import org.akm.contragentdatastore.core.service.files.model.FileFormat

data class ImportFileRequest(
    val fileName: String,
    val schemaName: String,
    val format: FileFormat
)
