package com.kotlinspring.repository

import com.kotlinspring.util.PostgreSQLContainerInitializer
import java.util.stream.Stream
import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals

// jpa 관련 테스트 설정만 로드 즉, db 계층만 테스트 가능하도록 해줌
@DataJpaTest
@ActiveProfiles("test")
// 기본적으로 H2 같은 임베디드 데이터베이스를 자동으로 설정하려고 하기 때문에
// Spring Boot의 통합 테스트에서 데이터베이스 설정을 변경하지 않도록 하는 역할
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryIntgTest : PostgreSQLContainerInitializer() { // postgresql관련 초기 셋팅 인스턴스를 가져옴

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    // 모든 코스 조회 테스트를 위한 셋업
    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        // 강사 초기 데이터 셋팅
        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        // 코스 초기 데이터 셋팅
        val courses = courseEntityList(instructor) // util에 만든 entity 가져오기
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