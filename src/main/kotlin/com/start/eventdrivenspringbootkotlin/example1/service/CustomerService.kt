package com.start.eventdrivenspringbootkotlin.example1.service

import com.start.eventdrivenspringbootkotlin.configuration.Response
import com.start.eventdrivenspringbootkotlin.example1.api.CreateCustomerCommand
import com.start.eventdrivenspringbootkotlin.example1.api.CustomerDto
import com.start.eventdrivenspringbootkotlin.example1.api.DeleteCustomerCommand
import com.start.eventdrivenspringbootkotlin.example1.api.UpdateCustomerCommand
import com.start.eventdrivenspringbootkotlin.example1.query.CustomerRepository
import com.start.eventdrivenspringbootkotlin.util.PageRequestParam
import com.start.eventdrivenspringbootkotlin.util.RestPage
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
data class CustomerService (private val commandGateway: CommandGateway,
                            private val queryGateway: QueryGateway,
                            private val repository: CustomerRepository){

    private val  logger = LoggerFactory.getLogger(CustomerService::class.java)

    fun signupCustomer(command: CreateCustomerCommand): CompletableFuture<Response<String>> {
        if (repository.findByEmail(command.email!!).isPresent){
            return CompletableFuture.completedFuture(Response.withError("Email Already Exist"))
        }else {
            val result = commandGateway.send<Any>(command).thenApply { _ ->
                Response.ofResponse("Customer Created Successfully with -> ${command.id}")
            }.exceptionally { x ->
                logger.error(x.message)
                Response.withError(x.message, HttpStatus.BAD_REQUEST)
            }
            return result
        }
    }

    fun updateCustomer(command: UpdateCustomerCommand): CompletableFuture<Response<String>> {
        val result = commandGateway.send<Any>(command).thenApply { _ ->
            Response.ofResponse("Customer Updated Successfully with -> ${command.id}")
        }.exceptionally { x ->
            logger.error(x.message)
            Response.withError(x.message, HttpStatus.BAD_REQUEST)
        }
        return result
    }

    fun temporaryDeleteById(command: DeleteCustomerCommand): CompletableFuture<Response<String>> {
        val optionalDoctor = this.repository.findById(command.id!!)
        if (optionalDoctor.isEmpty) {
            return CompletableFuture.completedFuture(Response.withError("Customer Not Present"))
        }
        val result = commandGateway.send<Any>(command).thenApply { _ ->
            Response.ofResponse(" Customer Deleted Successfully with -> ${command.id}")
        }.exceptionally { x ->
            logger.error(x.message)
            Response.withError(x.message)
        }
        return result
    }

    fun getCustomerById(id: String): CompletableFuture<Response<CustomerDto>> {
        val result = this.queryGateway.query("getCustomerById", id,
            ResponseTypes.instanceOf(CustomerDto::class.java))
        return result.thenApply { x ->
            if (x == null) Response.withError("Customer Does Not Exist with $id")
            else Response.ofResponse(x)
        }.exceptionally { e -> Response.withError(e.message) }
    }

    fun getByCustomerName(name: String): CompletableFuture<Response<CustomerDto>> {
        val result = this.queryGateway.query("getByCustomerName", name,
            ResponseTypes.instanceOf(CustomerDto::class.java))
        return result.thenApply { x ->
            if (x == null) Response.withError("Customer Does Not Exist with $name")
            else Response.ofResponse(x)
        }.exceptionally { e -> Response.withError(e.message) }
    }

    fun getAllCustomers(): CompletableFuture<Response<List<CustomerDto>>> {
        val result = this.queryGateway.query("getAllCustomers", "", ResponseTypes.multipleInstancesOf(CustomerDto::class.java))

        return result.thenApply { x ->
            if (x.isEmpty()) Response.withError("Customers Does Not Exist")
            else Response.ofResponse(x)
        }.exceptionally { e -> Response.withError(e.message) }
    }

    fun findAllCustomersPagination(pageRequestParam: PageRequestParam): CompletableFuture<Response<RestPage>> {
        val customers = this.queryGateway.query(
            "findAllCustomersPagination", pageRequestParam, ResponseTypes.instanceOf(RestPage::class.java)
        )
        return customers.thenApply { r ->
            if (r.totalElements?.toInt() == 0) Response.withError("No customers Found")
            else Response.ofResponse(r)
        }.exceptionally { e -> Response.withError(e.message) }
    }


}