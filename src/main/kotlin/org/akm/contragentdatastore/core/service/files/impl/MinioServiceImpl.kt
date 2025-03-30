package org.akm.contragentdatastore.core.service.files.impl

import io.minio.GetObjectArgs
import io.minio.ListObjectsArgs
import io.minio.MinioClient
import io.minio.messages.Item
import org.akm.contragentdatastore.core.service.files.MinioService
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class MinioServiceImpl(
    private val minioClient: MinioClient,
) : MinioService {
    override fun getObject(bucketName: String, objectName: String): InputStream {
        val getObjectArgs = GetObjectArgs.builder()
            .bucket(bucketName)
            .`object`(objectName)
            .build()

        val inputStream = minioClient.getObject(getObjectArgs)
        return inputStream
    }



    override fun getObjects(bucketName: String, path: String): List<InputStream> {
        val inputStreams = mutableListOf<InputStream>()

        // List all objects in the given bucket and path
        val items = minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path)
                .recursive(true)
                .build()
        )

        for (itemResult in items) {
            val item: Item = itemResult.get()
            if (!item.isDir) {
                // Get an InputStream for each object and add it to the list
                val inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                        .bucket(bucketName)
                        .`object`(item.objectName())
                        .build()
                )
                inputStreams.add(inputStream)
            }
        }

        return inputStreams
    }

    override fun listAllObjects(bucketName: String): List<String> {
        val objects = minioClient.listObjects(
            ListObjectsArgs.builder()
                .bucket(bucketName)
                .recursive(true)
                .build()
        )

        return objects.mapNotNull { it.get().objectName() }.toList()
    }
}