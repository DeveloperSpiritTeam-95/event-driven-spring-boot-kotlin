package com.start.eventdrivenspringbootkotlin.example1.api

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class UpdateCustomerCommand(
    @TargetAggregateIdentifier
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
)

data class CustomerUpdatedEvent(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
)

data class CustomerUpdatedDTO(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
)