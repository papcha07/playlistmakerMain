package com.example.domain.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}