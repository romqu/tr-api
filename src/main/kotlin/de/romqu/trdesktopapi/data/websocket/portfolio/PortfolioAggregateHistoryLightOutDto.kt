package de.romqu.trdesktopapi.data.websocket.portfolio

import de.romqu.trdesktopapi.data.websocket.WebsocketDtoType
import de.romqu.trdesktopapi.data.websocket.WebsocketOutDto

data class PortfolioAggregateHistoryLightOutDto(
    val range: String,
    val token: String,
    val type: String,
) : WebsocketOutDto {
    override val dtoType: WebsocketDtoType
        get() = WebsocketDtoType.PORTFOLIO_HISTORY_LIGHT
}