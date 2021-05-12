package de.romqu.trdesktopapi


import com.fasterxml.jackson.databind.ObjectMapper
import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.data.shared.di.WEB_SOCKET_CLIENT
import de.romqu.trdesktopapi.domain.LoginService
import de.romqu.trdesktopapi.domain.ResetDeviceService
import de.romqu.trdesktopapi.rest.login.LoginInDto
import de.romqu.trdesktopapi.shared.map
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.security.Security
import java.util.*

@SpringBootApplication
class TrDesktopApiApplication(
    private val sessionRepository: SessionRepository,
    private val loginService: LoginService,
    private val resetDeviceService: ResetDeviceService,
    private val objectMapper: ObjectMapper,
    @Qualifier(WEB_SOCKET_CLIENT) private val client: OkHttpClient,
) {


    init {
        // sessionRepository.deleteAll()
        // login()
    }

    private fun login() {

        GlobalScope.launch {
            val result = loginService.execute(LoginInDto(countryCode = "49", phoneNumber = "15783936784", "4289"), null)
            result.map { session ->

                println("Enter Code: ")
                val scanner = Scanner(System.`in`)
                val code = scanner.next()

                // resetDeviceService.execute(code, session)
                // loginService.execute(LoginInDto(countryCode = "49", phoneNumber = "15783936784", "4289"))
            }
        }

/*
        GlobalScope.launch {
            val result = authenticateAccountTask.execute(15783936784)
                .doOn({
                    val session = sessionRepository.getByUuid(it)
                    loginService.execute(15783936784, 4289, session!!)
                    val session1 = sessionRepository.getByUuid(it)!!

                    println("Enter Code: ")
                    val scanner = Scanner(System.`in`)
                    val code = scanner.next()

                    resetDeviceService.execute(code, session1)
                    loginService.execute(15783936784, 4289, session1)

                }, {})
        }
*/
    }

/*        webSocket.send("""connect 22 {"clientId":"de.traderepublic.app","clientVersion":"1.1.5486","device":"$device","locale":"en","platformId":"android","platformVersion":"30"}""")
        webSocket.send("echo " + System.currentTimeMillis().toString())
        webSocket.send("""sub 1 {"type":"portfolioStatus","token":"$token"}""")
        webSocket.send("""sub 2 {"range":"1d","type":"portfolioAggregateHistoryLight","token":"$token"}""")
        webSocket.send("""sub 3 {"type":"compactPortfolio","token":"$token"}""")
        webSocket.send("""sub 4 {"type":"cash","token":"$token"}""")*/
}


fun main(args: Array<String>) {

    Security.insertProviderAt(BouncyCastleProvider(), 1)

    runApplication<TrDesktopApiApplication>(*args)
}
