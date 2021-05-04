package de.romqu.trdesktopapi.data.websocket

import java.util.*

class ConnectOutDto(
    val clientId: String,
    val clientVersion: String,
    val device: UUID,
    val locale: String,
    val platformId: String,
    val platformVersion: Int,
) : WebsocketOutDto {
    override val type: WebsocketDtoType
        get() = WebsocketDtoType.CONNECT
}