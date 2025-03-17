package com.kotlinspring.util

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.dto.InstructorDto
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor

// 테스트에서 사용할 entity data 만드는 함수
fun courseEntityList() = listOf(
    Course(
        null,
        "SpringBoot와 Kotlin을 사용해서 Restful API 만들기",
        "Development"
    ),
    Course(
        null,
        "SpringBoot와 Kotlin을 사용해서 Reactive Microservices 만들기",
        "Development"
    ),
    Course(
        null,
        "SpringBoot와 Kotlin을 사용해서 Junit5 test 만들기",
        "Development"
    ),
)

// courseDto를 쉽게 만들기 위한 메소드
fun courseDto(
    id: Int? = null,
    name: String = "Spring boot와 Kotlin을 사용해서 Restful API 만들기",
    category: String = "Development",
    // instructorId: Int? = 1
) = CourseDto(
    id,
    name,
    category,
    // instructorId
)

// instructorDto를 쉽게 만들기 위한 메소드
fun instructorDto(
    id: Int? = null,
    name: String = "Hyeon"
): InstructorDto = InstructorDto(
    id,
    name,
)
