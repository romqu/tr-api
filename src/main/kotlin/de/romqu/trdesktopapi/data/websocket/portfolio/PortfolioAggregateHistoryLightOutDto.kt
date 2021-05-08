package de.romqu.trdesktopapi.data.websocket.portfolio

import de.romqu.trdesktopapi.data.websocket.WebSocketOutDto
import de.romqu.trdesktopapi.data.websocket.WebsocketDtoType

data class PortfolioAggregateHistoryLightOutDto(
    val range: String,
    val token: String,
    val type: String,
) : WebSocketOutDto {
    override val dtoType: WebsocketDtoType
        get() = WebsocketDtoType.PORTFOLIO_HISTORY_LIGHT
}