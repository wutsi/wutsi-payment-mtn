package com.wutsi.payment.mtn.api

import com.wutsi.payment.Fixtures
import com.wutsi.payment.mtn.MtnException
import com.wutsi.payment.mtn.http.MtnHttp
import com.wutsi.payment.mtn.model.MtnErrorCode
import com.wutsi.payment.mtn.model.MtnParty
import com.wutsi.payment.mtn.model.MtnStatus
import com.wutsi.payment.mtn.model.MtnTransferRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.util.UUID

class MtnDisbursementApiTest {

    @Test
    fun token() {
        val api = createApi()
        val response = api.token()

        assertNotNull(response.access_token)
        assertNotNull(response.token_type)
        assertNotNull(response.expires_in)
    }

    @Test
    fun tokenInternalError() {
        var config = Fixtures.createDisbursementConfig(userId="xxx", apiKey = "xxx")

        val api = createApi(config = config)
        try {
            api.token()
            fail()
        } catch (ex: MtnException) {
            assertEquals(500, ex.statusCode)

        }
    }

    @Test
    fun transferPending() {
        transfer(Fixtures.NUMBER_PENDING, MtnStatus.PENDING)
    }

    @Test
    fun transferFailed() {
        transfer(Fixtures.NUMBER_FAILED, MtnStatus.FAILED, MtnErrorCode.INTERNAL_PROCESSING_ERROR)
    }

    @Test
    fun transferRejected() {
        transfer(Fixtures.NUMBER_REJECTED, MtnStatus.FAILED, MtnErrorCode.APPROVAL_REJECTED)
    }

    @Test
    fun transferOngoing() {
        transfer(Fixtures.NUMBER_ONGOING, MtnStatus.PENDING)
    }

    @Test
    fun transferTimeout() {
        transfer(Fixtures.NUMBER_TIMEOUT, MtnStatus.FAILED, MtnErrorCode.EXPIRED)
    }

    @Test
    fun transferAuthenticationIssues() {
        val api = createApi()
        val accessToken = "invalid-token"
        val referenceId = UUID.randomUUID().toString()

        val request = createTransferRequest(Fixtures.NUMBER_PENDING)
        try {
            api.transfer(referenceId, accessToken, request)
        } catch (ex: MtnException){
            assertEquals(401, ex.statusCode)
            assertNull(ex.error)
        }
    }

    @Test
    fun transferBadCurrency() {
        val api = createApi()
        val accessToken = api.token().access_token
        val referenceId = UUID.randomUUID().toString()

        val request = createTransferRequest(Fixtures.NUMBER_PENDING, "XXX")
        try {
            api.transfer(referenceId, accessToken, request)
        } catch (ex: MtnException){
            assertEquals(500, ex.statusCode)
            assertEquals(MtnErrorCode.INVALID_CURRENCY, ex.error?.code)
        }
    }

    private fun createApi(
            config: MtnConfig = Fixtures.createValidDisbursementConfig(),
            http: MtnHttp = Fixtures.createHttp()
    ): MtnDisbursementApi =
            MtnDisbursementApi(config, http)

    private fun createTransferRequest(
            phoneNumber: String,
            currency: String = "EUR"
    ) = MtnTransferRequest(
            currency = currency,
            amount = "15000",
            externalId = "123",
            payeeNote = "Note",
            payerMessage = "Message",
            payee = MtnParty(
                    partyId = phoneNumber
            )
    )

    private fun transfer(number: String, status: MtnStatus, reason: MtnErrorCode? = null) {
        val api = createApi()
        val accessToken = api.token().access_token
        val referenceId = UUID.randomUUID().toString()

        val request = createTransferRequest(number)
        api.transfer(referenceId, accessToken, request)
        val response = api.getTransfer(referenceId, accessToken)

        assertEquals(request.amount, response.amount)
        assertEquals(request.currency, response.currency)
        assertEquals(request.payee, response.payee)
        assertEquals(status, response.status)
        assertEquals(reason, response.reason)
    }
}
