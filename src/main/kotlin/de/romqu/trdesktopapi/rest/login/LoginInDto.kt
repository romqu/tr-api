package de.romqu.trdesktopapi.rest.login

import javax.validation.constraints.Max
import javax.validation.constraints.Min

class LoginInDto(
    @field:Min(2)
    @field:Max(3)
    val countryCode: Long,

    @field:Min(5)
    @field:Max(15)
    val phoneNumber: Long,

    @field:Min(4)
    @field:Max(4)
    val pinNumber: Int,
)