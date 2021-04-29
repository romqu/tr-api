package de.romqu.trdesktopapi.data.auth.account

data class AuthenticateAccountInDto(
    private val countdownInSeconds: Int,
    private val registrationId: String
)