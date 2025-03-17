package com.kotlinspring.controller

import com.kotlinspring.dto.InstructorDto
import com.kotlinspring.service.InstructorService
import com.kotlinspring.util.instructorDto
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@WebMvcTest(controllers = [InstructorController::class])
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorServiceMockk: InstructorService

    @Test
    fun createInstructor() {
        val instructorDto = InstructorDto(
            null,
            "Hyeon"
        )

        every {
            instructorServiceMockk.createInstructor(any())
        } returns instructorDto(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDto::class.java)
            .returnResult()
            .responseBody

        assertTrue { response!!.id != null }
    }

    @Test
    fun createInstructor_Validation() {
        val instructorDto = InstructorDto(
            null,
            ""
        )

        every {
            instructorServiceMockk.createInstructor(any())
        } returns instructorDto(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDto)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("instructorDto.name는 비어있으면 안됩니다.", response)
    }
}