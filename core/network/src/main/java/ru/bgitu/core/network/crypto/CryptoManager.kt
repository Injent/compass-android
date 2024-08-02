package ru.bgitu.core.network.crypto

import java.io.File
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

class CryptoManager internal constructor(publicKeyFile: File) {

    private val publicKey by lazy {
        getPublicKeyFromPEM(publicKeyFile.readText())
    }

    fun encryptData(data: ByteArray): String {
        val encryptedBytes = Cipher.getInstance("RSA/ECB/PKCS1Padding").run {
            init(Cipher.ENCRYPT_MODE, publicKey)
            doFinal(data)
        }
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    private fun getPublicKeyFromPEM(publicKeyPEM: String): PublicKey {
        val keySpec = X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyPEM))
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }
}