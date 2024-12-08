package org.akm.contragentdatastore.core.config.minio

import io.minio.MinioClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "minio")
class MinioProperties {
    lateinit var endpoint: String
    lateinit var accessKey: String
    lateinit var secretKey: String
}

@Configuration
class MinioConfig(
    private val properties: MinioProperties
) {
    @Bean
    fun minioClient(): MinioClient = MinioClient.builder()
        .endpoint(properties.endpoint)
        .credentials(properties.accessKey, properties.secretKey)
        .build()
}