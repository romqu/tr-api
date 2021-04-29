package de.romqu.trdesktopapi.data.auth.login

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class DecodedJWTDeserializer : JsonDeserializer<DecodedJWT>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): DecodedJWT {
        val jwtValue = p.valueAsString
        return JWT.decode(jwtValue)
    }
}