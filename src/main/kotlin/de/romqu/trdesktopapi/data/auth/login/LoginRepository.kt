package de.romqu.trdesktopapi.data.auth.login

import de.romqu.trdesktopapi.data.shared.ApiCall
import de.romqu.trdesktopapi.data.shared.ApiCallDelegate
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.shared.Result
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LoginRepository(
    private val api: LoginApi,
    private val apiCallDelegate: ApiCallDelegate,
): ApiCall by apiCallDelegate {

    suspend fun login(trDto: LoginOutTrDto, sessionId: UUID): Result<ApiCallError, LoginInTrDto> =
        makeApiCall { api.login(trDto, sessionId.toString()) }

}