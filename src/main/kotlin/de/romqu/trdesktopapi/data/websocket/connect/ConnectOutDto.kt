package de.romqu.trdesktopapi.data.websocket.connect

import de.romqu.trdesktopapi.data.websocket.WebSocketOutDto
import de.romqu.trdesktopapi.data.websocket.WebsocketDtoType
import java.util.*

class ConnectOutDto(
    val clientId: String,
    val clientVersion: String,
    val device: UUID,
    val locale: String,
    val platformId: String,
    val platformVersion: Int,
) : WebSocketOutDto {
    override val dtoType: WebsocketDtoType
        get() = WebsocketDtoType.CONNECT
}