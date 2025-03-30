package org.akm.contragentdatastore.core.service.files

import java.io.InputStream

interface MinioService {
    fun getObject(bucketName: String, objectName: String): InputStream
    fun getObjects(bucketName: String, path: String): List<InputStream>
    fun listAllObjects(bucketName: String): List<String>
}