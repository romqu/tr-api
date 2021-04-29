package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.auth.login.LoginInDto
import de.romqu.trdesktopapi.data.auth.login.LoginOutDto
import de.romqu.trdesktopapi.data.auth.login.LoginRepository
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.shared.Result
import de.romqu.trdesktopapi.shared.doIfFailureElseContinue
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val loginRepository: LoginRepository,
) {

    fun execute(phoneNumber: Long, pinNumber: Int) {

    }

    private suspend fun login(
        phoneNumber: Long,
        pinNumber: Int,
    ): Result<ApiCallError, LoginInDto> = loginRepository.login(
        LoginOutDto(
            phoneNumber, pinNumber
        ),
    )

    private fun Result<ApiCallError, LoginInDto>.maybeRequestDeviceReset() =
        doIfFailureElseContinue { Result.Success("") }

}