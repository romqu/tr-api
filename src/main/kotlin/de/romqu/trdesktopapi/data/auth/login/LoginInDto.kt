package de.romqu.trdesktopapi.data.auth.login

import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = DecodedJWTDeserializer::class)
data class LoginInDto(
    private val accountState: String,
    private val jurisdiction: String,
    private val refreshToken: DecodedJWT,
    private val sessionToken: DecodedJWT,
)