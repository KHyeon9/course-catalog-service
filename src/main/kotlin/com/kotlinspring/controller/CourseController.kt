package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
class CourseController(val courseService : CourseService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 요청 성공시 201 create 상태 반환
    fun addCourse(@RequestBody courseDto: CourseDto) : CourseDto {
        return courseService.addCourse(courseDto)
    }

    @GetMapping
    fun retrieveAllCourses() : List<CourseDto> = courseService.retrieveCourses()
}