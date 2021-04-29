package de.romqu.trdesktopapi


import de.romqu.trdesktopapi.data.SessionRepository
import de.romqu.trdesktopapi.domain.AuthenticateAccountService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.security.Security

@SpringBootApplication
class TrDesktopApiApplication(
    private val authenticateAccountService: AuthenticateAccountService,
    private val sessionRepository: SessionRepository
){


    init {

        sessionRepository.deleteAll()

        GlobalScope.launch {
            val result = authenticateAccountService.execute(15783936784346543)
            println(result)
        }

    }
}

fun main(args: Array<String>) {

    Security.insertProviderAt(BouncyCastleProvider(), 1)

    runApplication<TrDesktopApiApplication>(*args)
}
