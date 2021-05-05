package de.romqu.trdesktopapi.data.websocket.portfolio

import java.util.*

data class PortfolioAggregateHistoryLightInDto(
    val aggregates: List<Aggregate>,
    val expectedClosingTime: Long,
    val lastAggregateEndTime: Long,
    val resolution: Int,
    val sessionUuid: UUID,
)