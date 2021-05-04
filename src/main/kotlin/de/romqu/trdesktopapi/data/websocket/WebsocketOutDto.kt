package de.romqu.trdesktopapi.data.websocket

sealed interface WebsocketOutDto {
    val type: WebsocketDtoType
}