package de.romqu.trdesktopapi.data.shared.signrequest

import de.romqu.trdesktopapi.data.KeypairRepository
import de.romqu.trdesktopapi.data.SessionRepository
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import org.springframework.stereotype.Component
import java.io.IOException
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec

const val SIGN_ALGORITHM = "SHA512withECDSA"
const val HEADER_SESSION_ID = "session-id"

@Component
class SignRequestInterceptor(
    private val keypairRepository: KeypairRepository,
    private val sessionRepository: SessionRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val keyFactory = KeyFactory.getInstance("EC")
        val timestamp = System.currentTimeMillis().toString()

        val session = sessionRepository.getById(
            request.header(HEADER_SESSION_ID)!!.toLong()
        )

        val keypair = keypairRepository.getById(session!!.keypairId)!!

        val deviceIdValue = session.deviceId.toString()


        val newRequest = if (!request.url.toString().contains("/api/v1/auth/account/reset/device")) {
            val privateKey = keyFactory.generatePrivate(
                PKCS8EncodedKeySpec(keypair.privateKey)
            )
            val requestBodyValue = request.body.asString()

            val signature = ApiRequestSignerUtil.sign(
                "$timestamp.$requestBodyValue",
                privateKey,
                SIGN_ALGORITHM
            )

            chain.request().newBuilder().apply {
                addHeader("X-Zeta-Device-Id", deviceIdValue)
                addHeader("X-Zeta-Timestamp", timestamp)
                addHeader("X-Zeta-Signature", signature!!)
                addHeader("X-Zeta-Tracking-Id", session.trackingId)
                removeHeader(HEADER_SESSION_ID)
            }.build()
        } else {
            chain.request().newBuilder().apply {
                addHeader("X-Zeta-Device-Id", deviceIdValue)
                addHeader("X-Zeta-Tracking-Id", session.trackingId)
            }.build()
        }

        return chain.proceed(newRequest)
    }

    private fun RequestBody?.asString(): String {
        return try {
            val buffer = Buffer()
            if (this == null) {
                return ""
            }
            writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            ""
        }
    }
}