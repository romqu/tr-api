package de.romqu.trdesktopapi


import com.fasterxml.jackson.databind.ObjectMapper
import de.romqu.trdesktopapi.data.auth.session.SessionRepository
import de.romqu.trdesktopapi.data.shared.di.WEB_SOCKET_CLIENT
import de.romqu.trdesktopapi.data.shared.signrequest.HEADER_SESSION_ID
import de.romqu.trdesktopapi.domain.AuthenticateAccountService
import de.romqu.trdesktopapi.domain.LoginService
import de.romqu.trdesktopapi.domain.ResetDeviceService
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.beans.factory.annotation.Qualifier
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
    private val objectMapper: ObjectMapper,
    @Qualifier(WEB_SOCKET_CLIENT) private val client: OkHttpClient,
) {


    init {
        /*sessionRepository.deleteAll()
        login()
        websocket(client)
        */// client.dispatcher.executorService.shutdown()
    }

    private fun login() {
        GlobalScope.launch {
            val result = authenticateAccountService.execute(15783936784)
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
    }

    private fun websocket(client: OkHttpClient) {
        val request = Request.Builder()
            .addHeader(HEADER_SESSION_ID, "3")
            .url("wss://api.traderepublic.com")
            .build()

        val session = sessionRepository.getById(3)
        val device = session!!.deviceId
        val token = session.token

        val webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {

                if (text.contains("AUTHENTICATION_ERROR") && text.contains("Unauthorized")) {

                    runBlocking {
                        val sessionTokenInDto = sessionRepository.getRemote(webSocket.request().header(
                            HEADER_SESSION_ID)!!.toLong()
                        )
                        val tokenValue = objectMapper.readTree(text).get("token")
                        val currentSession = sessionRepository.getById(webSocket.request().header(
                            HEADER_SESSION_ID)!!.toLong()
                        )!!

                        with(currentSession) {
                            SessionEntity(id,
                                uuidId,
                                deviceId,
                                tokenValue.textValue(),
                                refreshToken,
                                trackingId,
                                resetProcessId,
                                keypairId)
                        }
                    }

                }
            }
        })

        webSocket.send("""connect 22 {"clientId":"de.traderepublic.app","clientVersion":"1.1.5486","device":"$device","locale":"en","platformId":"android","platformVersion":"30"}""")
        webSocket.send("echo " + System.currentTimeMillis().toString())
        webSocket.send("""sub 1 {"type":"portfolioStatus","token":"$token"}""")
        webSocket.send("""sub 2 {"range":"1d","type":"portfolioAggregateHistoryLight","token":"$token"}""")
        webSocket.send("""sub 3 {"type":"compactPortfolio","token":"$token"}""")
        webSocket.send("""sub 4 {"type":"cash","token":"$token"}""")
    }
}

fun main(args: Array<String>) {

    Security.insertProviderAt(BouncyCastleProvider(), 1)

    runApplication<TrDesktopApiApplication>(*args)
}
