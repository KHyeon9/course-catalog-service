package com.kotlinspring.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class GreetingService {
    
    // yaml파일의 message의 값을 가져옴
    @Value("\${message}")
    // 나중에 사용할 것이라고 선언
    // 이후 값을 지정하면 그 값이 들어감
    lateinit var message: String

    fun retrieveGreeting(name:String) = "$name, $message"
}