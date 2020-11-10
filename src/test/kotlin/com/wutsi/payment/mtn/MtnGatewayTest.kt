package com.wutsi.payment.mtn

import com.wutsi.payment.Fixtures
import com.wutsi.payment.Gateway
import com.wutsi.payment.MobileWallet
import com.wutsi.payment.Party
import com.wutsi.payment.PaymentStatus
import com.wutsi.payment.TransferRequest
import com.wutsi.payment.WalletType
import com.wutsi.payment.mtn.api.MtnDisbursementApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MtnGatewayTest {
    private val gateway = createGateway()

    @Test
    fun transfer(){
        val transactionId = gateway.transfer(request = TransferRequest(
                externalId = "123",
                amount = 15000.0,
                currency = "EUR",
                description = "Your Wutsi revenus for Oct 2020",
                party = Party(
                        firstName = "Ray",
                        lastName = "Sponsible",
                        fullName = "Ray Sponsible"
                ),
                wallet = MobileWallet(
                        number = Fixtures.NUMBER_PENDING,
                        type = WalletType.MTN
                )
        )).transactionId

        val status = gateway.transferStatus(transactionId)

        assertEquals(transactionId, status.transactionId)
        assertEquals(PaymentStatus.PENDING, status.status)
        assertNull(status.error)
    }

    private fun createGateway(): Gateway {
        val config = Fixtures.createValidDisbursementConfig()
        val http = Fixtures.createHttp()

        return MtnGateway(
                disbursementApi = MtnDisbursementApi(config, http)
        )
    }
}
