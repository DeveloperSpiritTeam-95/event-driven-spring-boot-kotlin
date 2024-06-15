package com.start.eventdrivenspringbootkotlin.example1.query

import com.start.eventdrivenspringbootkotlin.example1.api.CustomerCreatedEvent
import com.start.eventdrivenspringbootkotlin.example1.api.CustomerDeletedEvent
import com.start.eventdrivenspringbootkotlin.example1.api.CustomerDto
import com.start.eventdrivenspringbootkotlin.example1.api.CustomerUpdatedEvent
import com.start.eventdrivenspringbootkotlin.example1.util.CustomerConverter
import com.start.eventdrivenspringbootkotlin.util.RestPage
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
data class CustomerProjection(private val repository: CustomerRepository) {

    @EventHandler
    fun createCustomer(event: CustomerCreatedEvent) {
        val state = Customer(event.id, event.name, event.email, event.password, false)
        this.repository.save(state)
    }

    // Customer update on events
    @EventHandler
    fun updateCustomer(event: CustomerUpdatedEvent) {
        val state = this.repository.findById(event.id!!)

        if (state.isPresent) {
            state.get().id = event.id
            state.get().name = event.name
            state.get().email = event.email
            state.get().password = event.password
            state.get().isDeleted = false
            this.repository.save(state.get())
        }
    }

    // Customer temporary deletion on events
    @EventHandler
    fun temporaryDeleteCustomer(event: CustomerDeletedEvent) {
        val state = this.repository.findById(event.id!!)
        if (state.isPresent) {
            state.get().isDeleted = true
            this.repository.save(state.get())
        }
    }


    @QueryHandler(queryName = "getCustomerById")
    fun getCustomerById(doctorId: String): CustomerDto {
        val result = this.repository.findById(doctorId)
        if (result.get().isDeleted == false) {
            return CustomerConverter.convertModelToDTO(result.get())
        }
        return CustomerDto()
    }

    //get Customer Data By Customer Name
    @QueryHandler(queryName = "getByCustomerName")
    fun getByCustomerName(doctorName: String): CustomerDto {
        val result = this.repository.findByName(doctorName)
        if (result.isPresent) {
            return CustomerConverter.convertModelToDTO(result.get())
        }
        return CustomerDto()
    }

    // get All Customers in list
    @QueryHandler(queryName = "getAllCustomers")
    fun findAll(): List<CustomerDto> {
        val customers = this.repository.findAll()
        val activeCustomers = mutableListOf<CustomerDto>()
        if (customers.isNotEmpty()) {
            for (doctor in customers) {
                if (doctor.isDeleted == false) {
                    val dto = CustomerConverter.convertModelToDTO(doctor)
                    activeCustomers.add(dto)
                }
            }
            return activeCustomers

        } else
            return emptyList()

    }


    // find All Customer Pagination


    @QueryHandler(queryName = "findAllCustomersPagination")
    fun searchByNamePagination(pagination: com.start.eventdrivenspringbootkotlin.util.PageRequestParam): RestPage {

        val result: Page<Customer>
        val sortColumns = pagination.sortColumns?.toTypedArray()
        val sort = if (sortColumns.isNullOrEmpty()) Sort.by("name").ascending() else Sort.by(*sortColumns)

        val pageRequest = PageRequest.of(pagination.pageNumber, pagination.pageSize, sort)

        if ((!pagination.searchString.isNullOrEmpty())) {
            result = repository.findByIsDeletedAndName(false, pagination.searchString!!, pageRequest)
        } else {
            result = repository.findByIsDeleted(false, pageRequest)
        }

        if (result.size > 0) {
            val records = CustomerConverter.modelToDtoList(result.toList()).toList()
            return RestPage(result.totalElements, records)
        } else {
            return RestPage()
        }
    }

    /* @QueryHandler(queryName = "findAllCustomersPagination")
     fun findAll(pageRequestParam: PageRequestParam): RestPage {

         val sortColumns = pageRequestParam.sortColumns?.toTypedArray()
         val sort = if (sortColumns.isNullOrEmpty()) Sort.by("firstName","lastname") else Sort.by(*sortColumns)

         val requestPageParam = PageRequest.of(pageRequestParam.pageNumber, pageRequestParam.pageSize, sort)
         val doctor = this.doctorInfo(pageRequestParam, requestPageParam)
         if (doctor.isEmpty) {
             return RestPage()
         }
         return RestPage(doctor.totalElements, CustomerConverter.modelToDtoList(doctor.toList()))
     }

     private fun doctorInfo(pageRequestParam: PageRequestParam, requestPageParam: Pageable): Page<DoctorState> {
         val doctors: Page<Customer>
         if (pageRequestParam.searchString != null) {
             doctors = this.repository.findByIsDeletedAndName(false, pageRequestParam.searchString, requestPageParam)

         } else {
             doctors = this.repository.findByIsDeleted(false, requestPageParam)
         }
         return doctors
     }*/


}