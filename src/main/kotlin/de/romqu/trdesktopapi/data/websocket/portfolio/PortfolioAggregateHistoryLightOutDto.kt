package de.romqu.trdesktopapi.data.websocket.portfolio

import de.romqu.trdesktopapi.data.websocket.WebSocketDtoType
import de.romqu.trdesktopapi.data.websocket.WebSocketOutDto

data class PortfolioAggregateHistoryLightOutDto(
    val range: String,
    val token: String,
    val type: String,
) : WebSocketOutDto {
    override val dtoType: WebSocketDtoType
        get() = WebSocketDtoType.PORTFOLIO_HISTORY_LIGHT
}