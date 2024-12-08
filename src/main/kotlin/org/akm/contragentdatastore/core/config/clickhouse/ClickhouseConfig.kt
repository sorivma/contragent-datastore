package org.akm.contragentdatastore.core.config.clickhouse

import com.clickhouse.jdbc.ClickHouseDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import java.util.*

@Configuration
@ConfigurationProperties(prefix = "clickhouse")
class ClickHouseProperties {
    lateinit var url: String
    var username: String? = null
    var password: String? = null
    lateinit var database: String
}

@Configuration
class ClickhouseConfig(
    private val properties: ClickHouseProperties
) {
    @Bean
    @Qualifier("clickhouse")
    fun clickHouseDataSource(): JdbcTemplate {
        val connectionProperties = Properties().apply {
            this["user"] = properties.username
            this["password"] = properties.password
        }

        val urlWithDataBase = "${properties.url}/${properties.database}"
        return JdbcTemplate(ClickHouseDataSource(urlWithDataBase, connectionProperties))
    }
}