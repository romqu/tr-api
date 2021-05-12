package de.romqu.trdesktopapi.rest.login

import de.romqu.trdesktopapi.domain.LoginService
import de.romqu.trdesktopapi.domain.LoginService.Error
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
    private val loginService: LoginService,
) {
    companion object {
        const val URL_PATH = "/v1/auth/login"
    }

    @PostMapping(URL_PATH)
    suspend fun login(
        @Header(HttpHeaders.AUTHORIZATION, required = false) authHeader: String?,
        @RequestBody @Valid dto: LoginInDto,
    ): ResponseEntity<ResponseDto<LoginOutDto>> =
        loginService.execute(dto, authHeader)
            .doOn(
                success = {
                    ResponseEntity.ok(ResponseDto.success(LoginOutDto(it.uuidId)))
                },
                failure = { error ->
                    when (error) {
                        Error.InvalidSession ->
                            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(ResponseDto.failure(ErrorDto(type = "", message = "")))
                        Error.AccountDoesNotExist -> ResponseEntity.badRequest().build()
                        Error.CouldNotResetDevice -> ResponseEntity.badRequest().build()
                        is Error.UserIsLoggedIn -> ResponseEntity.badRequest().build()
                    }

                }
            )

}

