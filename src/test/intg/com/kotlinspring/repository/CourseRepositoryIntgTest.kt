package com.kotlinspring.repository

import java.util.stream.Stream
import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals

// jpa 관련 테스트 설정만 로드 즉, db 계층만 테스트 가능하도록 해줌
@DataJpaTest
@ActiveProfiles("test")
class CourseRepositoryIntgTest {

    @Autowired
    lateinit var courseRepository: CourseRepository

    // 모든 코스 조회 테스트를 위한 셋업
    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courses = courseEntityList() // util에 만든 entity 가져오기
        courseRepository.saveAll(courses)
    }

    @Test
    fun findByNameContaining() {
        val courses = courseRepository.findByNameContaining("SpringBoot")
        println("courses: $courses")

        assertEquals(3, courses.size)
    }

    @Test
    fun findCoursesByName() {
        val courses = courseRepository.findCoursesByName("SpringBoot")
        println("courses: $courses")

        assertEquals(3, courses.size)
    }

    // 매개 변수화된 테스트
    @ParameterizedTest
    // 입력과 출력 제공
    @MethodSource("courseAndSize")
    fun findCoursesByName_approach2(name: String, size: Int) {
        val courses = courseRepository.findCoursesByName(name)
        println("courses: $courses")

        assertEquals(size, courses.size)
    }

    companion object {
        // junit5는 java로 구현되기 때문에 아래 함수를 이해하지 못함
        // 따라서, kotlin에서의 비슷한 동작인 companion object를 static으로 인식하게 하기 위해서 사용
        @JvmStatic
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("SpringBoot", 3),
                Arguments.arguments("Junit5", 1)
            )
        }
    }
}