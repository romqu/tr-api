package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.websocket.WebSocketRepository
import de.romqu.trdesktopapi.data.websocket.connect.ConnectOutDto
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity

class ConnectToWebSocketService(
    private val webSocketRepository: WebSocketRepository,
) {

    fun execute(session: SessionEntity) {
        if (!webSocketRepository.hasConnection(session.uuidId)) {
            val websocket = webSocketRepository.create(session.uuidId)
            webSocketRepository.save(session.uuidId, websocket)
            val connectOutDto = ConnectOutDto(
                clientId = "",
                clientVersion = "Xiaomi Mi A3",
                device = session.deviceId,
                locale = "DE",
                platformId = "android",
                platformVersion = 30
            )
            webSocketRepository.connect(session.uuidId, connectOutDto, websocket)
        }
    }
}