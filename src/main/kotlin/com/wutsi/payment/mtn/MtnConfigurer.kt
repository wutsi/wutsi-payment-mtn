package com.wutsi.payment.mtn

import com.wutsi.payment.GatewayRegistry
import com.wutsi.payment.WalletType
import com.wutsi.payment.mtn.api.MtnConfig
import com.wutsi.payment.mtn.api.MtnDisbursementApi
import com.wutsi.payment.mtn.http.HttpClientFactory
import com.wutsi.payment.mtn.http.MtnHttp
import com.wutsi.payment.mtn.model.MtnEnvironment
import java.net.URL

class MtnConfigurer(
        val callbackUrl: URL,
        val environment: MtnEnvironment,
        val disbursementUserId: String = "",
        val disbursementApiSecret: String = "",
        val disbursementSubscriptionKey: String = "",

        val registry: GatewayRegistry
) {
    fun configure() {
        val gateway = MtnGateway(
                disbursementApi = MtnDisbursementApi(
                        config = MtnConfig(
                                environment = environment,
                                callbackUrl = callbackUrl,
                                userId = disbursementUserId,
                                apiSecret = disbursementApiSecret,
                                subscriptionKey = disbursementSubscriptionKey
                        ),
                        http = MtnHttp(
                                httpClient = HttpClientFactory().httpClient()
                        )
                )
        )
        registry.register(WalletType.MTN, gateway)
    }
}
