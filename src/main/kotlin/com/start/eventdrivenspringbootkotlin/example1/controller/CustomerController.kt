package com.start.eventdrivenspringbootkotlin.example1.controller

import com.start.eventdrivenspringbootkotlin.configuration.Response
import com.start.eventdrivenspringbootkotlin.example1.api.CustomerCreatedDTO
import com.start.eventdrivenspringbootkotlin.example1.api.CustomerDto
import com.start.eventdrivenspringbootkotlin.example1.api.CustomerUpdatedDTO
import com.start.eventdrivenspringbootkotlin.example1.service.CustomerService
import com.start.eventdrivenspringbootkotlin.example1.util.CustomerConverter
import com.start.eventdrivenspringbootkotlin.util.PageRequestParam
import com.start.eventdrivenspringbootkotlin.util.RestPage
import lombok.AllArgsConstructor
import lombok.Data
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture



@RestController
@RequestMapping("/customers")
class CustomerController {

    @Autowired
    private val customerService: CustomerService? = null

    @PostMapping("/signupCustomer")
    fun signupCustomer(@RequestBody dto: CustomerCreatedDTO): CompletableFuture<Response<String>> {
        val result = CustomerConverter.convertDtoToCreateCommand(dto)
        return this.customerService!!.signupCustomer(result)
    }

    @PutMapping("/updateCustomer")
    fun updateCustomer(@RequestBody dto: CustomerUpdatedDTO): CompletableFuture<Response<String>> {
        val result = CustomerConverter.convertDtoToUpdateCommand(dto)
        return this.customerService!!.updateCustomer(result)
    }


    @PutMapping("/temporaryDeleteById/{id}")
    fun temporaryDeleteById(@PathVariable("id") id: String): CompletableFuture<Response<String>> {
        val result = CustomerConverter.convertDtoToDeleteCommand(id)
        return this.customerService!!.temporaryDeleteById(result)
    }

    @PutMapping( "/recoverById/{id}")
    fun recoverById(@PathVariable("id") id: String): CompletableFuture<Response<String>>{
        val result = CustomerConverter.convertDtoToRecoverCommand(id)
        return this.customerService!!.recoverById(result)
    }


    @GetMapping("/getCustomerById/{id}")
    fun getCustomerById(@PathVariable("id") id: String): CompletableFuture<Response<CustomerDto>>{
        return this.customerService!!.getCustomerById(id)
    }


    @GetMapping("/getByCustomerName/{name}")
    fun getByCustomerName(@PathVariable("name") name: String): CompletableFuture<Response<CustomerDto>>{
        return this.customerService!!.getByCustomerName(name)
    }

    @GetMapping("/getAllCustomers")
    fun getAllCustomers(): CompletableFuture<Response<List<CustomerDto>>>{
        return this.customerService!!.getAllCustomers()
    }

    @CrossOrigin
    @PutMapping("/findAllCustomersPagination")
    fun findAllCustomersPagination(@RequestBody pageRequestParam: PageRequestParam): CompletableFuture<Response<RestPage>> {
        return customerService!!.findAllCustomersPagination(pageRequestParam)
    }


}