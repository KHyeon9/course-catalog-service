package com.kotlinspring.service

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository
) {

    companion object : KLogging()

    // 코스 저장 서비스 로직
    fun addCourse(courseDto: CourseDto) : CourseDto {
        val courseEntity = courseDto.let {
            Course(null, it.name, it.category)
        }
        
        courseRepository.save(courseEntity)
        logger.info("저장된 course : $courseEntity")


        return courseEntity.let {
            CourseDto(it.id, it.name, it.category)
        }
    }

    fun retrieveCourses(): List<CourseDto> {
        return courseRepository
            .findAll()
            .map {
                CourseDto(it.id, it.name, it.category)
            }
    }
}
