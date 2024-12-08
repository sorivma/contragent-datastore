package org.akm.contragentdatastore.data.repository

import org.akm.contragentdatastore.data.entity.SchemaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SchemaRepository : JpaRepository<SchemaEntity, UUID>