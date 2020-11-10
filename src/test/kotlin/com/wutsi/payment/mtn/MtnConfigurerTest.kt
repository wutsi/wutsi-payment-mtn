package com.wutsi.payment.mtn

import com.wutsi.payment.GatewayRegistry
import com.wutsi.payment.WalletType
import com.wutsi.payment.mtn.model.MtnEnvironment
import org.junit.Test

import org.junit.Assert.*
import java.net.URL

class MtnConfigurerTest {

    @Test
    fun configure() {
        val registry = GatewayRegistry()

        MtnConfigurer(
                environment = MtnEnvironment.SANDBOX,
                callbackUrl = URL("https://www.google.ca"),
                disbursementApiSecret = "disbursement-api-secret",
                disbursementSubscriptionKey = "disbursement-subscription-key",
                disbursementUserId = "disbursement-user-id",
                registry = registry
        ).configure()

        val gateway = registry.get(WalletType.MTN)

        assertTrue(gateway is MtnGateway)
    }
}
