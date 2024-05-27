package com.example.smartwardrobeanalytics.interfaces.iretrofit

interface ApiCallback<T> {
    fun onSuccess(result: T)
    fun onError(error: String)
}