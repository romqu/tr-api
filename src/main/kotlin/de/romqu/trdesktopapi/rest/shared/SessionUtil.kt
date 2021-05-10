package de.romqu.trdesktopapi.rest.shared

import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.rest.shared.config.AUTH_BEARER_PATTERN
import java.util.*

object SessionUtil {

    fun getSessionFromAuthHeader(authHeader: String, sessionRepository: SessionRepository): SessionEntity {
        val authMatcher = AUTH_BEARER_PATTERN.matcher(authHeader)

        val sessionUuid = if (authMatcher.matches()) {
            val token = authMatcher.group(1)

            try {
                UUID.fromString(token)
            } catch (ex: Exception) {
                throw Exception() // not a valid uuid
            }
        } else throw Exception() // Nat a bearer token

        return sessionRepository.getByUuid(sessionUuid) ?: throw Exception()
    }


}