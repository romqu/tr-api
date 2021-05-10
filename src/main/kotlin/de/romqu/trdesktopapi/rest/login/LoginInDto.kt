package de.romqu.trdesktopapi.rest.login

import javax.validation.constraints.Size

class LoginInDto(
    @Size(min = 2, max = 3)
    val countryCode: Long,
    @Size(min = 5, max = 15)
    val phoneNumber: Long,
    @Size(min = 4, max = 4)
    val pinNumber: Int,
)