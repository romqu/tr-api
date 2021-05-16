package de.romqu.trdesktopapi.domain

import com.fasterxml.jackson.databind.ObjectMapper
import de.romqu.trdesktopapi.data.websocket.WebSocketDtoType
import de.romqu.trdesktopapi.data.websocket.WebSocketRepository
import de.romqu.trdesktopapi.data.websocket.portfolio.PortfolioAggregateHistoryLightInDto
import de.romqu.trdesktopapi.shared.CO_SCOPE_APPLICATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class WebSocketMessageBrokerService(
    private val webSocketMessageStream: Flow<WebSocketRepository.WebSocketMessageInDto>,
    private val objectMapper: ObjectMapper,
    private val portfolioAggregateHistoryLightBroker: MutableSharedFlow<PortfolioAggregateHistoryLightInDto>,
    @Qualifier(CO_SCOPE_APPLICATION) private val scope: CoroutineScope,
) {

    private val bodyMatcher = Regex("^[0-9]*")
    private val replaceMatcher = Regex("((\\[(.*?))|(\\{(.*?)).*?)")


    init {
        scope.launch {
            parseAndDistribute()
        }
    }

    private suspend fun parseAndDistribute() {
        webSocketMessageStream.collect { dto ->
            val dtoValue = bodyMatcher.find(dto.message)?.value
            val dtoType = dto.dtoType

            if (dtoValue != null) {

                val match = replaceMatcher.find(dtoValue)?.value

                val (updatedDtoValue, updatedMatch) = if (match == "[") {
                    Pair("""{${dtoValue}}""", "{")
                } else {
                    Pair(
                        dtoValue,
                        match)
                }

                val dtoValueToRead =
                    updatedDtoValue.replaceFirst(
                        replaceMatcher,
                        """"${updatedMatch}sessionId:"${dto.sessionUuid}","""
                    )

                when (dtoType) {
                    WebSocketDtoType.CONNECT -> {
                    }
                    WebSocketDtoType.PORTFOLIO_HISTORY_LIGHT -> portfolioAggregateHistoryLightBroker.tryEmit(
                        objectMapper.readValue(dtoValueToRead, PortfolioAggregateHistoryLightInDto::class.java)
                    )
                }
            }
        }
    }
}