package com.example.search.domain.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}