package de.romqu.trdesktopapi


import de.romqu.trdesktopapi.data.SessionRepository
import de.romqu.trdesktopapi.data.shared.di.WEB_SOCKET_CLIENT
import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import de.romqu.trdesktopapi.domain.AuthenticateAccountService
import de.romqu.trdesktopapi.domain.LoginService
import de.romqu.trdesktopapi.domain.ResetDeviceService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.security.Security

@SpringBootApplication
class TrDesktopApiApplication(
    private val authenticateAccountService: AuthenticateAccountService,
    private val sessionRepository: SessionRepository,
    private val loginService: LoginService,
    private val resetDeviceService: ResetDeviceService,
    @Qualifier(WEB_SOCKET_CLIENT) client: OkHttpClient,
) {


    init {
        val request = Request.Builder()
            .addHeader(HEADER_SESSION_ID, "14")
            .url("wss://api.traderepublic.com")
            .build()

        val webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
            }

        })

        webSocket.send("echo " + System.currentTimeMillis().toString())

        client.dispatcher.executorService.shutdown()
        // sessionRepository.deleteAll()

        /*GlobalScope.launch {
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
        }*/

    }
}

fun main(args: Array<String>) {

    Security.insertProviderAt(BouncyCastleProvider(), 1)

    runApplication<TrDesktopApiApplication>(*args)
}
