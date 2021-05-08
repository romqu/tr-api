package de.romqu.trdesktopapi.domain

import de.romqu.trdesktopapi.data.websocket.WebsocketRepository
import de.romqu.trdesktopapi.data.websocket.connect.ConnectOutDto
import de.romqu.trdesktopapi.public_.tables.pojos.SessionEntity

class ConnectToWebSocketService(
    private val websocketRepository: WebsocketRepository,
) {

    fun execute(session: SessionEntity) {
        if (!websocketRepository.hasConnection(session.uuidId)) {
            val websocket = websocketRepository.create(session.uuidId)
            websocketRepository.save(session.uuidId, websocket)
            val connectOutDto = ConnectOutDto(
                clientId = "",
                clientVersion = "Xiaomi Mi A3",
                device = session.deviceId,
                locale = "DE",
                platformId = "android",
                platformVersion = 30
            )
            websocketRepository.connect(session.uuidId, connectOutDto, websocket)
        }
    }
}