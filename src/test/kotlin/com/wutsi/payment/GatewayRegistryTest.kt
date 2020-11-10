package com.wutsi.payment

import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GatewayRegistryTest {
    @Mock private lateinit var mtn: Gateway
    @InjectMocks private lateinit var registry: GatewayRegistry

    @Test
    fun register() {
        registry.register(WalletType.MTN, mtn)

        val result = registry.get(WalletType.MTN)
        assertEquals(mtn, result)
    }

    @Test
    fun registerNotFound() {
        try {
            registry.get(WalletType.ORANGE)
            fail()
        } catch(ex: PaymentException){
            assertEquals(ErrorCode.UNSUPPORTED_GATEWAY, ex.error.code)
            assertNull(ex.error.transactionId)
            assertNull(ex.error.supplierErrorCode)
        }
    }
}
