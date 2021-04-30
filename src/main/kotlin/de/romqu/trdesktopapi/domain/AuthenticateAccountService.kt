package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.KeypairRepository
import de.romqu.trdesktopapi.data.SessionRepository
import de.romqu.trdesktopapi.data.auth.account.AccountRepository
import de.romqu.trdesktopapi.data.auth.account.AuthenticateAccountOutDto
import de.romqu.trdesktopapi.data.shared.ApiCallError
import de.romqu.trdesktopapi.data.shared.extension.asX962
import de.romqu.trdesktopapi.public_.tables.pojos.KeypairEntity
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import de.romqu.trdesktopapi.shared.Result
import de.romqu.trdesktopapi.shared.map
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticateAccountService(
    private val createKeypairTask: CreateKeypairTask,
    private val keypairRepository: KeypairRepository,
    private val sessionRepository: SessionRepository,
    private val accountRepository: AccountRepository,
) {

    suspend fun execute(
        phoneNumber: Long,
    ): Result<ApiCallError, UUID> = createSession()
        .authenticateAccount(phoneNumber)


    private fun createSession(): FirstOut {
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
            null,
            savedKeypairEntity.id,
        )
        return FirstOut(sessionRepository.save(sessionEntity), savedKeypairEntity)
    }

    class FirstOut(val sessionEntity: SessionEntity, val keypairEntity: KeypairEntity)

    private suspend fun FirstOut.authenticateAccount(
        phoneNumber: Long,
    ): Result<ApiCallError, UUID> {

        val deviceKey = keypairEntity.publicKey.asX962()

        val dto = AuthenticateAccountOutDto(deviceKey, jurisdiction = "DE", phoneNumber = "+49$phoneNumber")

        return accountRepository.authenticate(dto, sessionEntity.id)
            .map { sessionEntity.uuidId }
    }

}