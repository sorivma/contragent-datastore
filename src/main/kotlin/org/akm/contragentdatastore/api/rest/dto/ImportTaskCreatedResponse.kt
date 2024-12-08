package org.akm.contragentdatastore.api.rest.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import org.akm.contragentdatastore.api.rest.Routing

class ImportTaskCreatedResponse(
    @JsonIgnore
    val statusId: String,
    val message: String = "Import Task Created",
    val href: String = "${Routing.APIV1}${Routing.APIv1.Import.IMPORT_STATUS}/$statusId}",
)