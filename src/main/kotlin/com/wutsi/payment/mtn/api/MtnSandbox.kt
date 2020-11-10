package com.wutsi.payment.mtn.api

import com.wutsi.payment.mtn.http.MtnHttp
import com.wutsi.payment.mtn.model.MtnApiKeyResponse


class MtnSandbox(
        config: MtnConfig,
        http: MtnHttp
): MtnApi(config, http) {
    override fun uri(path: String): String =
            "${config.url()}/v1_0/apiuser$path"

    fun user(referenceId: String) {
        http.post(
                uri = uri(""),
                headers = mapOf(
                        "Content-Type" to "application/json",
                        "X-Reference-Id" to referenceId,
                        "Ocp-Apim-Subscription-Key" to config.subscriptionKey
                ),
                requestPayload = mapOf(
                        "providerCallbackHost" to config.callbackUrl.host
                ),
                responseType = Any::class.java
        )
    }

    fun apiKey(referenceId: String): String =
            http.post(
                    uri = uri("/$referenceId/apikey"),
                    headers = mapOf(
                            "Content-Type" to "application/json",
                            "Ocp-Apim-Subscription-Key" to config.subscriptionKey
                    ),
                    requestPayload = emptyMap<String, String>(),
                    responseType = MtnApiKeyResponse::class.java
            )!!.apiKey

}
