package org.akm.contragentdatastore.api.rest

object Routing {
    const val APIV1 = "/api/v1"
    object APIv1 {
        const val SCHEMA = "/schema"
        const val IMPORT = "/import"
        const val DATA = "/data"

        object Schema {
            const val CREATE = "/create"
            const val ALL = "/all"
        }

        object Import {
            const val IMPORT_ORGS = "/organizations"
            const val IMPORT_PERSONS = "/persons"
            const val IMPORT_STATUS = "/import-status"
            const val IMPORT_HISTORY = "/import-history"
        }

        object Data {
            const val PERSONS = "/persons"
            const val ORGANIZATIONS = "/organizations"
        }
    }
}