package com.start.eventdrivenspringbootkotlin.example1.api

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateCustomerCommand(
    @TargetAggregateIdentifier
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var isDeleted: Boolean? = false
)

data class CustomerCreatedEvent(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var isDeleted: Boolean? = false
)

data class CustomerCreatedDTO(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var isDeleted: Boolean? = false
)

data class CustomerDto(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var isDeleted: Boolean? = false
)