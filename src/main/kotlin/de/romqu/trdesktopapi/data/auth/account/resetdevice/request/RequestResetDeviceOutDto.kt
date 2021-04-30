package de.romqu.trdesktopapi.data.auth.account.resetdevice.request

data class RequestResetDeviceOutDto(
    val phoneNumber: String,
    val pin: Int,
)