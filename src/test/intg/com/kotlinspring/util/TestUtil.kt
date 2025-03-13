package com.kotlinspring.util

import com.kotlinspring.entity.Course

// 테스트에서 사용할 entity data 만드는 함수
fun courseEntityList() = listOf(
    Course(
        null,
        "Spring boot와 Kotlin을 사용해서 Restful API 만들기",
        "Development"
    ),
    Course(
        null,
        "Spring boot와 Kotlin을 사용해서 Reactive Microservices 만들기",
        "Development"
    ),
    Course(
        null,
        "Spring boot와 Kotlin을 사용해서 Junit5 test 만들기",
        "Development"
    )
)
