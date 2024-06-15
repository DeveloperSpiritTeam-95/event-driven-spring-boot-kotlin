package com.start.eventdrivenspringbootkotlin.util

data class PageRequestParam(var pageSize: Int=0,
                            var pageNumber: Int=0,
                            var sortColumns: List<String>? = listOf(),
                            var searchString: String? = null
)

data class RestPage (
    var totalElements:Long?=0,
    var pageResponseDto:List<Any>?=null)