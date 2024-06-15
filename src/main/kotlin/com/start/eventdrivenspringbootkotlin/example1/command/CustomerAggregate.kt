package com.start.eventdrivenspringbootkotlin.example1.command

import com.start.eventdrivenspringbootkotlin.example1.api.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class CustomerAggregate {

    @AggregateIdentifier
    private var id: String? = null
    private var name: String? = null
    private var email: String? = null
    private var password: String? = null
    private var isDeleted: Boolean? = false


    constructor()

    @CommandHandler
    constructor(command: CreateCustomerCommand){
        AggregateLifecycle.apply(CustomerCreatedEvent(
            command.id,command.name,command.email,command.password,command.isDeleted))
    }


    @EventSourcingHandler
    fun handlerForCreation(event: CustomerCreatedEvent){
        this.id = event.id
        this.name = event.name
        this.email = event.email
        this.password = event.password
        this.isDeleted = event.isDeleted
    }

    @CommandHandler
    fun handlerForUpdate(command: UpdateCustomerCommand){
        AggregateLifecycle.apply(CustomerUpdatedEvent(
            command.id,command.name,command.email,command.password))
    }

    @EventSourcingHandler
    fun handlerForUpdate(event: CustomerUpdatedEvent){
        this.id = event.id
        this.name = event.name
        this.email = event.email
        this.password = event.password
    }

    @CommandHandler
    fun handlerForDelete(command: DeleteCustomerCommand){
        AggregateLifecycle.apply(CustomerDeletedEvent(command.id))
    }

    @EventSourcingHandler
    fun handlerForDelete(event: CustomerDeletedEvent){
        this.id = event.id
        this.isDeleted = true
    }


}