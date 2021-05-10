package de.romqu.trdesktopapi.rest.login

import de.romqu.trdesktopapi.domain.LoginService
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class LoginController(
    private val loginService: LoginService,
) {

    fun login(@RequestBody @Valid dto: LoginInDto, bindingResult: BindingResult) {

    }

}