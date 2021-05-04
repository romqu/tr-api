package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.data.websocket.AuthErrorInDto
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class RefreshSessionTokenService(
    authErrorStream: Flow<AuthErrorInDto>,
    private val sessionRepository: SessionRepository,
) {

    init {
        refreshSessionToken(authErrorStream)
    }

    private fun refreshSessionToken(authErrorStream: Flow<AuthErrorInDto>) {
        authErrorStream.map {
            val sessionTokenInDto = sessionRepository.getRemote(it.sessionId.toString())

            val currentSession = sessionRepository.getByUuid(it.sessionId)!!

            sessionRepository.update(with(currentSession) {
                SessionEntity(id,
                    uuidId,
                    deviceId,
                    sessionTokenInDto.sessionToken.token,
                    refreshToken,
                    trackingId,
                    resetProcessId,
                    keypairId)
            })
        }
    }
}

