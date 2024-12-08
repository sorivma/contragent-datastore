package org.akm.contragentdatastore.core.service.files

import org.akm.contragentdatastore.data.schemaindex.entity.Organization
import org.akm.contragentdatastore.data.schemaindex.entity.PhysicalPersonEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DataAccessService {
    fun getOrganizations(pageable: Pageable): Page<Organization>
    fun getPersons(pageable: Pageable): Page<PhysicalPersonEntity>
    fun getOrganization(inn: String): Organization?
    fun getPerson(inn: String): PhysicalPersonEntity?
}