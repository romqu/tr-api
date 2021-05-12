package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.rest.shared.session.AUTH_BEARER_PATTERN
import de.romqu.trdesktopapi.shared.Result
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetSessionByAuthHeaderTask(
    private val sessionRepository: SessionRepository,
) {

    fun execute(authHeader: String?): Result<Error, SessionEntity> {

        authHeader ?: return Result.Failure(Error.HeaderDoesNotExist)

        val authMatcher = AUTH_BEARER_PATTERN.matcher(authHeader)

        val sessionUuid = if (authMatcher.matches()) {
            val token = authMatcher.group(1)

            try {
                UUID.fromString(token)
            } catch (ex: Exception) {
                return Result.Failure(Error.InvalidTokenFormat)
            }
        } else return Result.Failure(Error.InvalidTokenFormat)


        return Result.Success(sessionRepository.getByUuid(sessionUuid)
            ?: return Result.Failure(Error.SessionDoesNotExist))

    }

    sealed class Error {
        object HeaderDoesNotExist : Error()
        object InvalidTokenFormat : Error()
        object SessionDoesNotExist : Error()
    }


}