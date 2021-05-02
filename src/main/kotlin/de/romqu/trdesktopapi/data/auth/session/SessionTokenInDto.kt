package de.romqu.trdesktopapi.data.auth.session

import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import de.romqu.trdesktopapi.data.auth.login.DecodedJWTDeserializer

data class SessionTokenInDto(
    @JsonDeserialize(using = DecodedJWTDeserializer::class)
    val sessionToken: DecodedJWT,
    val accountState: String,
    val jurisdiction: String,
)