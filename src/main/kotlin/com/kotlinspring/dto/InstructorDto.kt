package com.kotlinspring.dto

import jakarta.validation.constraints.NotBlank

data class InstructorDto(
    val id: Int?,
    @get:NotBlank(message = "instructorDto.name는 비어있으면 안됩니다.")
    val name: String
)