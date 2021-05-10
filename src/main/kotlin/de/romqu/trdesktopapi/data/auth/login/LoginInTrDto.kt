package de.romqu.trdesktopapi.data.auth.login

import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.util.*

data class LoginInTrDto(
    @JsonDeserialize(using = DecodedJWTDeserializer::class)
    val refreshToken: DecodedJWT,
    @JsonDeserialize(using = DecodedJWTDeserializer::class)
    val sessionToken: DecodedJWT,
    val accountState: String,
    val jurisdiction: String,
    val trackingId: UUID,
)