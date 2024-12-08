package org.akm.contragentdatastore.data.schemaindex.repository

import org.akm.contragentdatastore.data.schemaindex.entity.Organization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrganisationRepository : JpaRepository<Organization, String>