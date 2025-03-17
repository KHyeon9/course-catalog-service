package com.kotlinspring.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

// @get은 @NotBlank를 직접 사용이 안되기 때문에 get을 붙여서 사용할 수 있도록 해줌
// NotBlank는 문자열의 공백, 빈 값, null값의 입력을 허용하지 않음
data class CourseDto(
    val id: Int?,
    @get:NotBlank(message = "courseDto.name는 비어있으면 안됩니다.")
    val name: String,
    @get:NotBlank(message = "courseDto.category는 비어있으면 안됩니다.")
    val category: String,
    // int형이기 때문에 NotBlank는 에러가 생기므로 notNull을 사용
    @get:NotNull(message = "courseDto.instructorId는 비어있으면 안됩니다.")
    val instructorId: Int? = null
)