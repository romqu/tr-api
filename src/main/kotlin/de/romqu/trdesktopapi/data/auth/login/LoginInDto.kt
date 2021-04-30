package de.romqu.trdesktopapi.data.auth.login

import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.util.*

@JsonDeserialize(using = DecodedJWTDeserializer::class)
data class LoginInDto(
    val refreshToken: DecodedJWT,
    val sessionToken: DecodedJWT,
    val accountState: String,
    val jurisdiction: String,
    val trackingId: UUID,
)