package com.wutsi.payment.mtn.api

import com.wutsi.payment.mtn.http.MtnHttp
import com.wutsi.payment.mtn.model.MtnTransferResponse
import com.wutsi.payment.mtn.model.MtnTransferRequest

class MtnDisbursementApi(
        config: MtnConfig,
        http: MtnHttp
): MtnApi(config, http) {
    override fun uri(path: String): String =
            "${config.url()}/disbursement$path"

    fun transfer(
            referenceId: String,
            accessToken: String,
            request: MtnTransferRequest
    ) {
        http.post(
                uri = uri("/v1_0/transfer"),
                requestPayload = request,
                headers = mapOf(
                        "Content-Type" to "application/json",
                        "Authorization" to "Bearer $accessToken",
                        "X-Callback-Url" to config.callbackUrl.toString(),
                        "X-Reference-Id" to referenceId,
                        "X-Target-Environment" to config.environment.name.toLowerCase(),
                        "Ocp-Apim-Subscription-Key" to config.subscriptionKey
                ),
                responseType = Any::class.java
        )
    }

    fun getTransfer(
            referenceId: String,
            accessToken: String
    ): MtnTransferResponse =
        http.get(
                uri = uri("/v1_0/transfer/$referenceId"),
                headers = mapOf(
                        "Authorization" to "Bearer $accessToken",
                        "X-Target-Environment" to config.environment.name.toLowerCase(),
                        "Ocp-Apim-Subscription-Key" to config.subscriptionKey
                ),
                responseType = MtnTransferResponse::class.java
        )!!

}

