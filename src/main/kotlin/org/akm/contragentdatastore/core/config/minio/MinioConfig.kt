package org.akm.contragentdatastore.data.config.minio

import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig {
    @Bean
    fun minioClient(): MinioClient = MinioClient.builder()
        .endpoint("")
        .credentials("", "")
        .build()
}