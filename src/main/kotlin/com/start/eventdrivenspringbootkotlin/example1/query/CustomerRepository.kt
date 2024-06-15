package com.start.eventdrivenspringbootkotlin.example1.query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository: MongoRepository<Customer,String> {

    fun findByEmail(email: String): Optional<Customer>

    fun findByName(name: String): Optional<Customer>

    fun findByIsDeleted(deleted: Boolean?, pageable: Pageable?): Page<Customer>


    @Query("{'doctorDeleted': ?0,'doctorName':{\$regex:?1,\$options:'i'}}")
    fun findByIsDeletedAndName(deleted: Boolean?, name: String?, pageable: Pageable?): Page<Customer>
}