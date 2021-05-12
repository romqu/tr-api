package de.romqu.trdesktopapi.domain

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security
import java.security.spec.ECGenParameterSpec

@Service
class CreateKeypairTask(
    private val keyPairGenerator: KeyPairGenerator,
) {

    fun execute(): KeyPair {
        return keyPairGenerator.generateKeyPair()
    }
}

@Component
object KeyPairGeneratorModule {

    @Bean
    fun keypairGenerator(): KeyPairGenerator {
        Security.insertProviderAt(BouncyCastleProvider(), 1)

        val instance = KeyPairGenerator.getInstance("EC", "BC")
        val ecGenParameterSpec = ECGenParameterSpec("prime256v1")
        instance.initialize(ecGenParameterSpec)
        return instance
    }
}