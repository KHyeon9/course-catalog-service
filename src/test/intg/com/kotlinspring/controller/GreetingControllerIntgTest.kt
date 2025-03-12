package com.kotlinspring.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

// 애플리케이션 컨텍스트를 로드하여 실제 애플리케이션 환경과
// 유사한 상태에서 테스트를 실행하도록 해줍니다.
// 안에 설정은 테스트에서 내장 웹 서버를 실행하고,
// 랜덤 포트에서 실행하도록 설정
// 즉, 실제 서버가 실행되지만 포트가 랜덤하게 할당되므로,
// 다른 테스트 환경과 충돌을 방지
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// 설정 파일의 프로필이 test인 환경으로 실행
@ActiveProfiles("test")
// 비동기 http 요청을 보내고 응답을 검증하는 도구
// 자동으로 컨트롤러를 스캔
@AutoConfigureWebTestClient
class GreetingControllerIntgTest {

    @Autowired // 자동으로 빈을 주입
    // 비동기 테스트 클라이언트로 http 비동기 요청을 보내고
    // 응답을 검증 하는데 사용
    lateinit var webTestClient: WebTestClient

    @Test
    fun retrieveGreeting() {
        val name = "hyeon"

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