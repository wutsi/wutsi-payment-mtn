package com.wutsi.payment.mtn.api

import com.wutsi.payment.mtn.model.MtnEnvironment
import java.net.URL


class MtnConfig(
        val environment: MtnEnvironment,
        val userId: String = "",
        val apiSecret: String = "",
        val subscriptionKey: String = "",
        val callbackUrl: URL = URL("https://foo.com/callback")
) {
    fun url(): String =
            if (environment== MtnEnvironment.PRODUCTION)
                "https://momodeveloper.mtn.com"
            else
                "https://sandbox.momodeveloper.mtn.com"
}
