package com.start.eventdrivenspringbootkotlin.example1.api

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class DeleteCustomerCommand(
    @TargetAggregateIdentifier
    var id: String? = null
)

data class CustomerDeletedEvent(
    var id: String? = null,
)

data class RecoverCustomerCommand(
    @TargetAggregateIdentifier
    var id: String? = null
)

data class CustomerRecoveredEvent(
    var id: String? = null
)