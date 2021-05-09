package de.romqu.trdesktopapi.rest.login

import de.romqu.trdesktopapi.data.auth.login.LoginInDto
import de.romqu.trdesktopapi.domain.LoginService
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val loginService: LoginService,
) {

    fun login(@RequestBody dto: LoginInDto) {

    }

}