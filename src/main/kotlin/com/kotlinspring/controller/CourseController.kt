package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/courses")
class CourseController(val courseService : CourseService) {

    // 코스 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 요청 성공시 201 create 상태 반환
    fun addCourse(@RequestBody courseDto: CourseDto) : CourseDto {
        return courseService.addCourse(courseDto)
    }

    // 모든 코스 조회
    @GetMapping
    fun retrieveAllCourses() : List<CourseDto> = courseService.retrieveCourses()

    // 코스 수정
    @PutMapping("/{course_id}")
    fun updateCourse(
        @PathVariable("course_id") courseId: Int,
        @RequestBody courseDto: CourseDto
    ) = courseService.updateCourse(courseId, courseDto)

    // 코스 삭제
    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 콘텐츠 없음 상태를 반환
    fun deleteCourse(@PathVariable("course_id") courseId: Int)
    = courseService.deleteCourse(courseId)
}