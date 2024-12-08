package org.akm.contragentdatastore.data.schemaindex.repository

import org.akm.contragentdatastore.data.schemaindex.entity.SchemaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SchemaRepository : JpaRepository<SchemaEntity, UUID> {
    fun findBySchemaName(name: String): SchemaEntity?
}