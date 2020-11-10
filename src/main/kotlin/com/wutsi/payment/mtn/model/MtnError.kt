package com.wutsi.payment.mtn.model

data class MtnError(
        val code: MtnErrorCode? = null,
        val message: String? = null
)
