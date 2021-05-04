package de.romqu.trdesktopapi.data.websocket.portfolio

data class Aggregate(
    val adjValue: Double,
    val close: Double,
    val high: Double,
    val low: Double,
    val `open`: Double,
    val time: Long,
    val volume: Int,
)