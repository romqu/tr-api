package de.romqu.trdesktopapi.data.shared.signrequest

import org.springframework.stereotype.Component
import java.util.*


object Base64Util {
    fun encode(bytes: ByteArray): String = Base64.getEncoder().encodeToString(bytes)

    fun decode(str: String): ByteArray = Base64.getDecoder().decode(str)
}