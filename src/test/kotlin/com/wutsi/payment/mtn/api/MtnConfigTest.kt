package com.wutsi.payment.mtn.api

import com.wutsi.payment.mtn.model.MtnEnvironment
import org.junit.Test

import org.junit.Assert.*

class MtnConfigTest {

    @Test
    fun sandboxUrl() {
        val config = MtnConfig(
                environment = MtnEnvironment.SANDBOX
        )
        assertEquals("https://sandbox.momodeveloper.mtn.com", config.url())
    }

    @Test
    fun productionUrl() {
        val config = MtnConfig(
                environment = MtnEnvironment.PRODUCTION
        )
        assertEquals("https://momodeveloper.mtn.com", config.url())
    }
}
