package de.romqu.trdesktopapi.data.shared.extension

import de.romqu.trdesktopapi.data.shared.signrequest.Base64Util

fun ByteArray.asX962(): String = Base64Util.encode(copyOfRange(26, size))