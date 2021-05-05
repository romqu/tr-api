package de.romqu.trdesktopapi.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

const val CO_SCOPE_APPLICATION = "CO_SCOPE_APPLICATION"

@Component
class CoroutineScopeModule {

    @Bean(CO_SCOPE_APPLICATION)
    fun applicationScope() = CoroutineScope(Dispatchers.Default + SupervisorJob())
}