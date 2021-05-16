package de.romqu.trdesktopapi.rest.resetdevice

import javax.validation.constraints.Size

class ResetDeviceInDto(
    @Size(min = 4, max = 4)
    val code: String,
)