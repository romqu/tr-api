package de.romqu.trdesktopapi.domain

import com.fasterxml.jackson.databind.ObjectMapper
import de.romqu.trdesktopapi.data.KeypairRepository
import de.romqu.trdesktopapi.data.SessionRepository
import de.romqu.trdesktopapi.data.auth.account.AccountRepository
import de.romqu.trdesktopapi.data.auth.account.AuthenticateAccountInDto
import de.romqu.trdesktopapi.data.auth.account.AuthenticateAccountOutDto
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.data.shared.extension.asX962
import de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.shared.Result
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoginService(
    private val createKeypairTask: CreateKeypairTask,
    private val keypairRepository: KeypairRepository,
    private val sessionRepository: SessionRepository,
    private val accountRepository: AccountRepository,
    private val objectMapper: ObjectMapper,
) {

    suspend fun execute(
        phoneNumber: Long
    ): Result<ApiCallError, Unit> = createSession()
        .authenticateAccount(phoneNumber)


    private fun createSession(): CreateSessionOut {
        val keypair = createKeypairTask.execute()
        val keypairEntity = KeypairEntity(0, keypair.private.encoded, keypair.public.encoded)
        val savedKeypairEntity = keypairRepository.save(keypairEntity)
        val sessionEntity = SessionEntity(
            0,
            UUID.randomUUID(),
            UUID.randomUUID(),
            "",
            "",
            "",
            savedKeypairEntity.id,
        )
        return CreateSessionOut(sessionRepository.save(sessionEntity), savedKeypairEntity)
    }

    class CreateSessionOut(val sessionEntity: SessionEntity, val keypairEntity: KeypairEntity)

    private suspend fun CreateSessionOut.authenticateAccount(
        phoneNumber: Long
    ): Result<ApiCallError, Unit> {

        val deviceKey = keypairEntity.publicKey.asX962()

        val dto = AuthenticateAccountOutDto(deviceKey, jurisdiction = "DE", phoneNumber = "+49$phoneNumber")

        return accountRepository.authenticate(dto, sessionEntity.id)
    }

}