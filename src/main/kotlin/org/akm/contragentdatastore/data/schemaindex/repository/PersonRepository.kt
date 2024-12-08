package org.akm.contragentdatastore.data.schemaindex.repository

import org.akm.contragentdatastore.data.schemaindex.entity.PhysicalPersonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PersonRepository : JpaRepository<PhysicalPersonEntity, String>