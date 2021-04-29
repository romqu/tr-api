package de.romqu.trdesktopapi.domain

import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.ECGenParameterSpec

@Service
class CreateKeypairTask {

    fun execute(): KeyPair {
        val instance = KeyPairGenerator.getInstance("EC", "BC")
        val ecGenParameterSpec = ECGenParameterSpec("prime256v1")
        instance.initialize(ecGenParameterSpec)
        return instance.generateKeyPair()
    }
}