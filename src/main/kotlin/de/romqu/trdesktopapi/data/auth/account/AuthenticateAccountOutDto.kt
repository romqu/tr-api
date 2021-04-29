package de.romqu.trdesktopapi.data.auth.account

import com.fasterxml.jackson.annotation.JsonInclude

data class AuthenticateAccountOutDto(
    val deviceKey: String,
    val jurisdiction: String,
    val phoneNumber: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val referralToken: String? = null,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val registrationToken: String? = null,
)