package de.romqu.trdesktopapi.rest.login

import de.romqu.trdesktopapi.domain.login.LoginService
import de.romqu.trdesktopapi.domain.login.LoginService.Error
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.rest.shared.ResponseDto
import de.romqu.trdesktopapi.rest.shared.errorhandling.ErrorDto
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.Header
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
class LoginController(
    private val service: LoginService,
) {
    companion object {
        const val URL_PATH = "/v1/auth/login"
    }

    @PostMapping(URL_PATH)
    suspend fun login(
        @Header(HttpHeaders.AUTHORIZATION, required = false) authHeader: String?,
        @RequestBody @Valid dto: LoginInDto,
    ): ResponseEntity<ResponseDto<LoginOutDto>> =
        service.execute(dto, authHeader)
            .doOn(::success, ::failure)

    private fun success(it: SessionEntity) =
        ResponseEntity.ok(ResponseDto.success(LoginOutDto(it.uuidId)))

    private fun failure(error: Error): ResponseEntity<ResponseDto<LoginOutDto>> =
        when (error) {
            Error.InvalidSession ->
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDto.failure(ErrorDto(type = "", message = "")))
            Error.AccountDoesNotExist -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDto.failure(ErrorDto("", "Account does not Exist")))
            Error.CouldNotRequestResetDevice -> ResponseEntity.badRequest().build()
            is Error.UserIsLoggedIn -> ResponseEntity.badRequest().build()
            Error.CouldNotHashPhoneNumber -> TODO()
            Error.SessionByHeaderNotFound -> TODO()
            is Error.SessionByHeaderPhoneNumberHashNotFound -> TODO()
        }

}

