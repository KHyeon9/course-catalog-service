package com.kotlinspring.entity

import com.kotlinspring.dto.InstructorDto
import jakarta.persistence.*

@Entity
@Table(name = "courses")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    var name: String,
    var category: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    var instructor:Instructor? = null
) {
    // 스택 오버플로우 에러를 방지하기 위해 instructor.id를 출력하도록 변경
    override fun toString(): String {
        return "Course(id=$id, name='$name', category='$category', instructor=${instructor!!.id})"
    }
}