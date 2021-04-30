package de.romqu.trdesktopapi.data.auth.login

import de.romqu.trdesktopapi.data.shared.ApiCall
import de.romqu.trdesktopapi.data.shared.ApiCallDelegate
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.shared.Result
import org.springframework.stereotype.Repository

@Repository
class LoginRepository(
    private val api: LoginApi,
    private val apiCallDelegate: ApiCallDelegate,
): ApiCall by apiCallDelegate{

    suspend fun login(dto: LoginOutDto, sessionId: Long): Result<ApiCallError, LoginInDto> =
        makeApiCallWith { api.login(dto, sessionId) }

}