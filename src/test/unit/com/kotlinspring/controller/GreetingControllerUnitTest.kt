package com.kotlinspring.controller

import com.kotlinspring.service.GreetingService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

// 통합 테스트 처럼 모든 테스트 도구를 가져올 필오가 없음
// MVC 컨트롤러만 테스트하기 위해 선언
// 즉 service와 repository는 로드하지 않음
@WebMvcTest(controllers = [GreetingController::class])
@AutoConfigureWebTestClient
class GreetingControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    // 특정 객체를 mock 객체로 등록
    @MockkBean
    lateinit var greetingServiceMock: GreetingService

    @Test
    fun retrieveGreeting() {
        val name = "hyeon"
        
        // mock을 이용해 서비스의 반환값을 직접 지정
        every { greetingServiceMock.retrieveGreeting(any()) } returns "$name, Hello from default profile"

        val result = webTestClient.get()
            .uri("/v1/greetings/{name}", name) // 주소 및 변수 입력
            .exchange() // 주소 호출
            .expectStatus() // 응답 상태
            .is2xxSuccessful // 그 응답 상태가 2xx 응답인지 확인
            .expectBody(String::class.java) // 응답 바디가 스트링인지 확인
            .returnResult() // 결과 반환

        // 결과값 비교
        Assertions
            .assertEquals(
                "$name, Hello from default profile",
                result.responseBody // 결과의 응답 바디를 호출
            )
    }
}