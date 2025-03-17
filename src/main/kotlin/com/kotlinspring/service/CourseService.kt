package com.kotlinspring.service

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.exception.CourseNotFoundException
import com.kotlinspring.exception.InstructorNotValidException
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository,
    private val instructorService: InstructorService
) {

    companion object : KLogging()

    // 코스 저장 서비스 로직
    fun addCourse(courseDto: CourseDto) : CourseDto {
        val instructorOptional = instructorService.findByInstructorId(courseDto.instructorId!!)

        if (!instructorOptional.isPresent) {
            throw InstructorNotValidException("${courseDto.instructorId}에 해당하는 Instructor를 찾지 못했습니다.")
        }

        val courseEntity = courseDto.let {
            Course(null, it.name, it.category, instructorOptional.get())
        }
        
        courseRepository.save(courseEntity)
        logger.info("저장된 course : $courseEntity")

        return courseEntity.let {
            CourseDto(it.id, it.name, it.category, it.instructor!!.id)
        }
    }

    fun retrieveCourses(courseName: String?): List<CourseDto> {
        // courseName이 null이 아닌경우 검색하고 null인 경우 모든 값을 가져옴
        val courses = courseName?.let {
            courseRepository.findByNameContaining(courseName)
        } ?: courseRepository.findAll()

        return courses
            .map {
                CourseDto(it.id, it.name, it.category)
            }
    }

    fun updateCourse(courseId: Int, courseDto: CourseDto): CourseDto {
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    it.name = courseDto.name
                    it.category = courseDto.category
                    courseRepository.save(it)
                    CourseDto(it.id, it.name, it.category)
                }
        } else {
            throw CourseNotFoundException("${courseId}번에 해당하는 Course를 찾지 못했습니다.")
        }
    }

    fun deleteCourse(courseId: Int) {
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent) {
            existingCourse.get()
                .let {
                    courseRepository.deleteById(courseId)
                }
        } else {
            throw CourseNotFoundException("${courseId}번에 해당하는 Course를 찾지 못했습니다.")
        }
    }
}
