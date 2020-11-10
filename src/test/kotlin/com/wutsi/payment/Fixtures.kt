package com.wutsi.payment

import com.wutsi.payment.mtn.api.MtnConfig
import com.wutsi.payment.mtn.api.MtnSandbox
import com.wutsi.payment.mtn.http.HttpClientFactory
import com.wutsi.payment.mtn.http.MtnHttp
import com.wutsi.payment.mtn.model.MtnEnvironment
import java.net.URL
import java.util.UUID


object Fixtures {
    const val NUMBER_PENDING = "46733123454"
    const val NUMBER_ONGOING = "46733123453"
    const val NUMBER_TIMEOUT = "46733123452"
    const val NUMBER_REJECTED = "46733123451"
    const val NUMBER_FAILED = "46733123450"

    fun createValidDisbursementConfig(): MtnConfig {
        val config = createDisbursementConfig()
        val http = createHttp()
        val sandbox = MtnSandbox(config, http)

        val referenceId = UUID.randomUUID().toString()
        sandbox.user(referenceId)
        val apiKey = sandbox.apiKey(referenceId)

        return createDisbursementConfig(
                userId = referenceId,
                apiKey = apiKey
        )
    }

    fun createDisbursementConfig(userId: String = "", apiKey: String = "") =
            MtnConfig(
                    environment = MtnEnvironment.SANDBOX,
                    subscriptionKey = "575487abb6b44f508eb102d5b002dd69",
                    apiSecret = apiKey,
                    userId = userId,
                    callbackUrl = URL("https://foo.com/test")
            )

    fun createHttp(): MtnHttp =
            MtnHttp(
                    httpClient = HttpClientFactory().httpClient()
            )
}
