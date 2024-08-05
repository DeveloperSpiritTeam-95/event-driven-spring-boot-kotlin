package com.start.eventdrivenspringbootkotlin.example1.util

import com.start.eventdrivenspringbootkotlin.example1.api.*
import com.start.eventdrivenspringbootkotlin.example1.query.Customer
import java.util.*

object CustomerConverter {

    fun convertModelToDTO(state: Customer): CustomerDto {
        return CustomerDto(state.id,state.name, state.email, state.password,state.isDeleted)
    }
    fun convertDtoToCreateCommand(createDTO: CustomerCreatedDTO): CreateCustomerCommand{
        return CreateCustomerCommand(
            UUID.randomUUID().toString(),createDTO.name, createDTO.email, createDTO.password,
            false)
    }

    fun convertDtoToUpdateCommand(updateDTO: CustomerUpdatedDTO): UpdateCustomerCommand{
        return UpdateCustomerCommand( updateDTO.id,updateDTO.name, updateDTO.email, updateDTO.password)
    }

    fun convertDtoToDeleteCommand(id: String): DeleteCustomerCommand {
        return DeleteCustomerCommand(id)
    }

    fun convertDtoToRecoverCommand(id: String): RecoverCustomerCommand{
        return RecoverCustomerCommand(id)
    }

    fun modelToDtoList(customers: List<Customer>): List<CustomerDto> {
        return customers.stream().map{ x -> convertModelToDTO(x) }.toList()
    }
}