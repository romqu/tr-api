package de.romqu.trdesktopapi.data.auth.account.resetdevice

data class ResetDeviceOutDto(
    val code: String,
    val deviceKey: String,
)