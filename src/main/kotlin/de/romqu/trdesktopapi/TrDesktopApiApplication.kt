package de.romqu.trdesktopapi


import de.romqu.trdesktopapi.data.SessionRepository
import de.romqu.trdesktopapi.data.auth.account.AccountApi
import de.romqu.trdesktopapi.domain.LoginService
import de.romqu.trdesktopapi.shared.doOn
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.security.KeyStore
import java.security.Security

@SpringBootApplication
class TrDesktopApiApplication(
    private val loginService: LoginService,
    private val sessionRepository: SessionRepository
){


    init {

        sessionRepository.deleteAll()

        GlobalScope.launch {
            val result = loginService.execute(15783936784346543)
            println(result)
        }

    }
}

fun main(args: Array<String>) {

    Security.insertProviderAt(BouncyCastleProvider(), 1)

    runApplication<TrDesktopApiApplication>(*args)
}
