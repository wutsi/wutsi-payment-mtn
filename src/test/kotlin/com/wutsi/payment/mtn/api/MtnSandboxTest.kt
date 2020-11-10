package com.wutsi.payment.mtn.api

import com.wutsi.payment.Fixtures
import org.junit.Test
import java.util.UUID


class MtnSandboxTest {
    @Test
    fun create() {
        val config = Fixtures.createDisbursementConfig()
        val http = Fixtures.createHttp()
        val sandbox = MtnSandbox(config, http)

        val referenceId = UUID.randomUUID().toString()
        sandbox.user(referenceId)
        val apiKey = sandbox.apiKey(referenceId)
    }
}
