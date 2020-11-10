package com.wutsi.payment.mtn.model

data class MtnTokenResponse(
        val access_token: String = "",
        val token_type: String = "",
        val expires_in: Int = -1
)
