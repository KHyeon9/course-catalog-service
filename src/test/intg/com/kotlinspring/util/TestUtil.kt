package com.kotlinspring.util

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.dto.InstructorDto
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor

// 테스트에서 사용할 entity data 만드는 함수
fun courseEntityList(
    instructor: Instructor? = null
) = listOf(
    Course(
        null,
        "SpringBoot와 Kotlin을 사용해서 Restful API 만들기",
        "Development",
        instructor
    ),
    Course(
        null,
        "SpringBoot와 Kotlin을 사용해서 Reactive Microservices 만들기",
        "Development",
        instructor
    ),
    Course(
        null,
        "SpringBoot와 Kotlin을 사용해서 Junit5 test 만들기",
        "Development",
        instructor
    ),
)

// courseDto를 쉽게 만들기 위한 메소드
fun courseDto(
    id: Int? = null,
    name: String = "Spring boot와 Kotlin을 사용해서 Restful API 만들기",
    category: String = "Development",
    instructorId: Int? = 1
) = CourseDto(
    id,
    name,
    category,
    instructorId
)

// instructor entity
fun instructorEntity(name: String = "Hyeon") = Instructor(null, name)

// instructorDto를 쉽게 만들기 위한 메소드
fun instructorDto(
    id: Int? = null,
    name: String = "Hyeon"
): InstructorDto = InstructorDto(
    id,
    name,
)
