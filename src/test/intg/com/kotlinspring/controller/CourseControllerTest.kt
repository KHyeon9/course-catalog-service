package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.util.courseEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

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
    fun addCourse() {
        val courseDto = CourseDto(
            null,
            "Spring과 Kotlin을 이용한 Restfull API 빌드",
            "Development"
        )
        val saveCourseDto = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto) // body의 값을 추가
            .exchange() // 호출
            .expectStatus().isCreated
            .expectBody(CourseDto::class.java)
            .returnResult() // 응답의 전체를 가져옴
            .responseBody // 응답의 위에 선언한 객체(CourseDto)만 가져옴

        Assertions.assertTrue {
            saveCourseDto!!.id != null // dto가 존재하며 id가 null이 아니면 true
        }
    }

    @Test
    fun retrieveAllCourses() {
        val courseDtos = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDto::class.java)
            .returnResult()
            .responseBody

        println("courseDtos = $courseDtos")
        Assertions.assertEquals(3, courseDtos!!.size)
    }
}