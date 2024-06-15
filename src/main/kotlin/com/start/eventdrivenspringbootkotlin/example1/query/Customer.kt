package com.start.eventdrivenspringbootkotlin.example1.query

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document
data class Customer(
    @Id
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var isDeleted: Boolean? = false
)