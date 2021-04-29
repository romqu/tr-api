package de.romqu.trdesktopapi.data.auth.account.resetdevice.request

import java.util.*

data class RequestResetDeviceInDto(
    val processId: UUID,
    val pin: Int,
)