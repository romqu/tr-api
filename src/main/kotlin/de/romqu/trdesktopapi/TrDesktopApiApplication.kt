package de.romqu.trdesktopapi


import de.romqu.trdesktopapi.data.SessionRepository
import de.romqu.trdesktopapi.domain.AuthenticateAccountService
import de.romqu.trdesktopapi.domain.LoginService
import de.romqu.trdesktopapi.domain.ResetDeviceService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.security.Security
import java.util.*

@SpringBootApplication
class TrDesktopApiApplication(
    private val authenticateAccountService: AuthenticateAccountService,
    private val sessionRepository: SessionRepository,
    private val loginService: LoginService,
    private val resetDeviceService: ResetDeviceService,
) {


    init {

        sessionRepository.deleteAll()

        GlobalScope.launch {
            val result = authenticateAccountService.execute(15783936784)
                .doOn({
                    val session = sessionRepository.getByUuid(it)
                    loginService.execute(15783936784, 4289, session!!)
                    val session1 = sessionRepository.getByUuid(it)!!

                    println("Enter Code: ")
                    val scanner = Scanner(System.`in`)
                    val code: Int = scanner.nextInt()

                    resetDeviceService.execute(code, session1)
                    loginService.execute(15783936784, 4289, session1)
                }, {})
        }

    }
}

fun main(args: Array<String>) {

    Security.insertProviderAt(BouncyCastleProvider(), 1)

    runApplication<TrDesktopApiApplication>(*args)
}
