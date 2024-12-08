package org.akm.contragentdatastore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class ContragentDatastoreApplication

fun main(args: Array<String>) {
    runApplication<ContragentDatastoreApplication>(*args)
}
