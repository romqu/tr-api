package de.romqu.trdesktopapi.rest.shared.errorhandling

import de.romqu.trdesktopapi.rest.shared.ResponseDto
import org.springframework.http.HttpStatus
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
@ResponseBody
class InvalidSessionExceptionHandler {

    object InvalidSessionException : Exception()

    @ExceptionHandler(InvalidSessionException::class)
    @MessageExceptionHandler(InvalidSessionException::class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    fun invalidSessionException(
        ex: InvalidSessionException,
    ): ResponseDto<Nothing> {
        return ResponseDto.failure(ErrorDto(type = "", message = ""))
    }
}