package de.romqu.trdesktopapi.data.auth.account.resetdevice

data class ResetDeviceOutDto(
    val code: Int,
    val deviceKey: String,
)