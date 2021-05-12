package de.romqu.trdesktopapi.rest.login

import de.romqu.trdesktopapi.domain.LoginService
import de.romqu.trdesktopapi.rest.shared.ResponseDto
import org.springframework.http.HttpHeaders
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
    fun login(
        @Header(HttpHeaders.AUTHORIZATION, required = false) authHeader: String?,
        @RequestBody @Valid dto: LoginInDto,
    ): ResponseEntity<ResponseDto<String>> {
        return ResponseEntity.ok(ResponseDto("token", listOf()))
    }

}

