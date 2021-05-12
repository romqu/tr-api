package de.romqu.trdesktopapi.rest.login

import javax.validation.constraints.Size

class LoginInDto(
    @Size(min = 2, max = 3)
    val countryCode: String,

    @Size(min = 5, max = 15)
    val phoneNumber: String,

    @Size(min = 4, max = 4)
    val pinNumber: String,
)