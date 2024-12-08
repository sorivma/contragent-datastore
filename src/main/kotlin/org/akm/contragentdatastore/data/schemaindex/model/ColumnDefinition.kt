package org.akm.contragentdatastore.data.schemaindex.model

import com.fasterxml.jackson.annotation.JsonInclude

data class ColumnDefinition (
    val name: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val dataSetName: String? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String? = null,
    val type: String,
)