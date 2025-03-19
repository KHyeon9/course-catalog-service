package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDto
import com.kotlinspring.entity.Course
import com.kotlinspring.repository.CourseRepository
import com.kotlinspring.repository.InstructorRepository
import com.kotlinspring.util.PostgreSQLContainerInitializer
import com.kotlinspring.util.courseEntityList
import com.kotlinspring.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
// @Testcontainers // postgresql로 바꾸면서 테스트 하기 위한 컨테이너 추가
class CourseControllerIntgTest : PostgreSQLContainerInitializer() {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

    //    companion object {
    //        // test를 위해 test container를 통해 postgresql db를 생성
    //        @Container
    //        val postgresDB = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply {
    //            withDatabaseName("testdb")
    //            withUsername("postgres")
    //            withPassword("secret")
    //        }
    //
    //        // test container로 생성한 db를 연결
    //        @JvmStatic
    //        @DynamicPropertySource // 동적으로 속성 소스를 재정의
    //        fun properties(registry: DynamicPropertyRegistry) {
    //            registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
    //            registry.add("spring.datasource.username", postgresDB::getUsername)
    //            registry.add("spring.datasource.password", postgresDB::getPassword)
    //        }
    //    }

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
    fun addCourse() {
        // 강사 조회
        val instructor = instructorRepository.findAll().first()

        val courseDto = CourseDto(
            null,
            "Spring과 Kotlin을 이용한 Restfull API 빌드",
            "Development",
            instructor.id
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

    @Test
    fun retrieveAllCourses_ByName() {
        // URI를 동적으로 구성하며, queryParam을 통해서 파라미터를 추가
        val uri = UriComponentsBuilder.fromUriString("/v1/courses")
            .queryParam("course_name", "SpringBoot")
            .toUriString()

        val courseDtos = webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDto::class.java)
            .returnResult()
            .responseBody

        println("courseDtos = $courseDtos")
        Assertions.assertEquals(3, courseDtos!!.size)
    }

    @Test
    fun updateCourse() {
        // 강사 조회
        val instructor = instructorRepository.findAll().first()

        // 조회시 반환할 course entity를 repository에 저장
        val course = Course(
            null,
            "Spring boot와 Kotlin을 사용해서 Restful API 만들기",
            "Development",
            instructor
        )
        courseRepository.save(course) // 저장으로 id가 채워짐

        // 수정할 course dto
        val updateCourseDto = CourseDto(
            null,
            "Spring boot와 Kotlin을 사용해서 Restful API 만들기2",
            "Development",
            instructor.id
        )

        // 수정 uri 호출
        val updateCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
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
        // 강사 조회
        val instructor = instructorRepository.findAll().first()

        // 조회시 반환할 course entity를 repository에 저장
        val course = Course(
            null,
            "Spring boot와 Kotlin을 사용해서 Restful API 만들기",
            "Development",
            instructor
        )
        courseRepository.save(course) // 저장으로 id가 채워짐

        // 수정 uri 호출
        val updateCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange() // 호출
            .expectStatus().isNoContent // 삭제시 상태가 no content인지 확인
    }
}