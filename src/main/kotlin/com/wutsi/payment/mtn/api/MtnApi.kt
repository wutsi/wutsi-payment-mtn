package com.wutsi.payment.mtn.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.payment.mtn.http.MtnHttp
import com.wutsi.payment.mtn.model.MtnTokenResponse
import java.util.Base64

abstract class MtnApi(
        protected val config: MtnConfig,
        protected val http: MtnHttp
) {
    protected abstract fun uri(path: String): String

    fun token(): MtnTokenResponse =
        http.post(
            uri = uri("/token"),
            headers = mapOf(
                    "Content-Type" to "application/json",
                    "Authorization" to "Basic " + credentials(),
                    "Ocp-Apim-Subscription-Key" to config.subscriptionKey
            ),
            requestPayload = mapOf("foo" to "bar"),
            responseType = MtnTokenResponse::class.java
        )!!

    private fun credentials(): String {
        val str = "${config.userId}:${config.apiSecret}"
        return  Base64.getEncoder().encodeToString(str.toByteArray())
    }
}

