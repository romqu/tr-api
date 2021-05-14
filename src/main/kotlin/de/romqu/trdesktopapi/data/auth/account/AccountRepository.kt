package de.romqu.trdesktopapi.data.auth.account

import de.romqu.trdesktopapi.data.shared.ApiCall
import de.romqu.trdesktopapi.data.shared.ApiCallDelegate
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.shared.Result
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AccountRepository(
    private val api: AccountApi,
    private val apiCallDelegate: ApiCallDelegate,
) : ApiCall by apiCallDelegate {

    suspend fun authenticate(
        dto: AuthenticateAccountOutDto,
        sessionId: UUID,
    ): Result<ApiCallError, Unit> =
        makeApiCall { api.authenticate(dto, sessionId.toString()) }

}