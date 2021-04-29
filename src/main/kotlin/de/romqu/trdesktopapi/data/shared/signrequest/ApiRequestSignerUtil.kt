package de.romqu.trdesktopapi.data.shared.signrequest

import java.security.PrivateKey
import java.security.Signature
import kotlin.text.Charsets.UTF_8

object ApiRequestSignerUtil {

    private fun sign(bArr: ByteArray, privateKey: PrivateKey?, algorithm: String): String? {
        if (privateKey == null) {
            return null
        }
        val sign = Signature.getInstance(algorithm).run {
            initSign(privateKey)
            update(bArr)
            sign()
        }
        return Base64Util.encode(sign)
    }

    fun sign(valueToSign: String, privateKey: PrivateKey?, algorithm: String): String? {
        val bytesToSign = valueToSign.toByteArray(UTF_8)
        return sign(bytesToSign, privateKey, algorithm)
    }

    /*fun verify(bArr: ByteArray, str: String, publicKey: PublicKey, str2: String): Boolean {
        val instance = Signature.getInstance(str2)
        instance.initVerify(publicKey)
        instance.update(bArr)
        return instance.verify(Base64Util.decode(str))
    }

    fun verify(str: String, str2: String, publicKey: PublicKey, str3: String): Boolean {
        val bytes = str.toByteArray(UTF_8)
        return verify(bytes, str2, publicKey, str3)
    }*/
}