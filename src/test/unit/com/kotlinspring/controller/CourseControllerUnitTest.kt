package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import com.kotlinspring.util.courseDto
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    // service 단을 mock으로 만듦
    @MockkBean
    lateinit var courseServiceMockk: CourseService

    @Test
    fun addCourse() {
        val courseDto = CourseDto(
            null,
            "Spring과 Kotlin을 이용한 Restfull API 빌드",
            "Development",
            1
        )

        // 서비스 메서드가 호출할 때, 반환하는 값 설정
        every {
            courseServiceMockk.addCourse(any())
        } returns courseDto(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto) // body의 값을 추가
            .exchange() // 호출
            .expectStatus().isCreated
            .expectBody(CourseDto::class.java)
            .returnResult() // 응답의 전체를 가져옴
            .responseBody // 응답의 위에 선언한 객체(CourseDto)만 가져옴

        Assertions.assertTrue {
            response!!.id != null // dto가 존재하며 id가 null이 아니면 true
        }
    }

    @Test
    fun addCourse_validation() {
        val courseDto = CourseDto(
            null,
            "",
            "",
            1
        )

        // 서비스 메서드가 호출할 때, 반환하는 값 설정
        every {
            courseServiceMockk.addCourse(any())
        } returns courseDto(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto) // body의 값을 추가
            .exchange() // 호출
            .expectStatus().isBadRequest // BadRequest가 뜨는지 확인
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            "courseDto.category는 비어있으면 안됩니다., courseDto.name는 비어있으면 안됩니다.",
            response
        )
    }

    @Test
    fun addCourse_runtimeException() {
        val courseDto =  CourseDto(
            null,
            "Spring과 Kotlin을 이용한 Restfull API 빌드",
            "Development",
            1
        )
        val errorMessage = "예기치 않은 에러 발생"

        // 서비스 메서드가 호출할 때, 반환하는 값 설정
        every {
            courseServiceMockk.addCourse(any())
        } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDto) // body의 값을 추가
            .exchange() // 호출
            .expectStatus().is5xxServerError // 서버 에러인지 확인
            .expectBody(String::class.java)
            .returnResult() // 응답의 전체를 가져옴
            .responseBody // 응답의 위에 선언한 객체(CourseDto)만 가져옴

        assertEquals(errorMessage, response)
    }

    @Test
    fun retrieveAllCourses() {
        every {
            courseServiceMockk.retrieveCourses(any())
        }.returnsMany(
            listOf(
                courseDto(id = 1),
                courseDto(id = 2, name = "Spring boot와 Kotlin을 사용해서 Reactive Microservices 만들기"),
            )
        )

        val courseDtos = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDto::class.java)
            .returnResult()
            .responseBody

        println("courseDtos = $courseDtos")
        Assertions.assertEquals(2, courseDtos!!.size)
    }

    @Test
    fun updateCourse() {
        // 조회시 반환할 course entity를 repository에 저장
        val course = Course(
            null,
            "Spring boot와 Kotlin을 사용해서 Restful API 만들기",
            "Development"
        )

        every {
            courseServiceMockk.updateCourse(any(), any())
        } returns courseDto(
            id = 100,
            name = "Spring boot와 Kotlin을 사용해서 Restful API 만들기2"
        )

        // 수정할 course dto
        val updateCourseDto = CourseDto(
            null,
            "Spring boot와 Kotlin을 사용해서 Restful API 만들기2",
            "Development"
        )

        // 수정 uri 호출
        val updateCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updateCourseDto) // 수정한 dto body에 값 추가
            .exchange() // 호출
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)
            .returnResult() // 응답의 전체를 가져옴
            .responseBody

        assertEquals(
            "Spring boot와 Kotlin을 사용해서 Restful API 만들기2",
            updateCourse!!.name
        )
    }

    @Test
    fun deleteCourse() {
        every { courseServiceMockk.deleteCourse(any()) } just runs // 실행하고 아무것도 반환하지 않음

        // 수정 uri 호출
        val updateCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange() // 호출
            .expectStatus().isNoContent // 삭제시 상태가 no content인지 확인

    }
}