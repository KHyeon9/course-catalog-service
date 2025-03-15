package com.kotlinspring.exceptionhandler

import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

// 스프링에 사용될 빈으로 등록
@Component
// api 동작 중 발생할 예외를 처리하도록 어노테이션 추가(exceptions interceptor)
@ControllerAdvice
// spring의 기본 예외를 처리할 수 있도록 ResponseEntityExceptionHandler 상속
// 여러 예외 핸들러를 미리 구현해 둔 클래스로, 응답을 ResponseEntity로 변환 가능
class GlobalErrorHandler : ResponseEntityExceptionHandler() {

    companion object : KLogging()

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        // 콘솔에 error 메세지 프린트
        logger.error("MethodArgumentNotValidException이 발생되었습니다 : ${ex.message}", ex)

        // 사용지 친화적 메세지로 변경
        val errors = ex
            .bindingResult
            .allErrors
            .map { error -> error.defaultMessage!! } // dto등에서 설정한 message의 값을 가져옴
            .sorted() // 결과가 많으면 정렬

        // error가 나의 의도대로 변경되었는지 확인을 위한 로그
        logger.info("errors = $errors")

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST) // 지금은 dto에 client가 잘못된 값을 주었으므로 badrequest error
            .body(errors.joinToString(", ")) // 배열을 join하여 []가 없도록 값을 반환
    }

    // 모든 에러 핸들러
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        logger.error("Exception이 발생되었습니다 : ${ex.message}", ex)

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ex.message)
    }
}