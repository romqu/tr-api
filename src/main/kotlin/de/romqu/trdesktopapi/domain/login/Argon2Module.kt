package de.romqu.trdesktopapi.domain.login

import de.mkammerer.argon2.Argon2Advanced
import de.mkammerer.argon2.Argon2Constants
import de.mkammerer.argon2.Argon2Factory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class Argon2Module {

    @Bean
    fun argon2(): Argon2Advanced = Argon2Factory.createAdvanced(
        Argon2Factory.Argon2Types.ARGON2id,
        8,
        Argon2Constants.DEFAULT_HASH_LENGTH
    )
}