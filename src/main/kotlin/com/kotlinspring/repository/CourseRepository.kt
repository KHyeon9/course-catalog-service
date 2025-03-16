package com.kotlinspring.repository

import com.kotlinspring.entity.Course
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : CrudRepository<Course, Int> {
    // 코스 네임에 인수가 포함된 코스가 존재하는지 확인하는 함수
    fun findByNameContaining(courseName: String): List<Course>

    // 위의 jpa 출력을 네이티브 쿼리로 작성함
    @Query(value = "SELECT * FROM COURSES WHERE name LIKE %:courseName%", nativeQuery = true)
    fun findCoursesByName(@Param("courseName") courseName: String): List<Course>
}