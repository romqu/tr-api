package de.romqu.trdesktopapi.data.auth.account.resetdevice.request

data class RequestResetDeviceOutDto(
    val phoneNumber: Long,
    val pin: Int,
)