package com.wutsi.payment.mtn

import com.wutsi.payment.mtn.model.MtnError

class MtnException(
        val statusCode: Int? = null,
        val error: MtnError? = null,
        message: String? = null
): RuntimeException(message) {
    override fun toString(): String =
        "statusCode=$statusCode, error=$error, message=$message"
}
