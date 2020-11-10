package com.wutsi.payment.mtn.model

data class MtnTransferResponse(
        val amount: String = "",
        val currency: String = "",
        val financialTransactionId: String = "",
        val payee: MtnParty = MtnParty(),
        val status: MtnStatus = MtnStatus.PENDING,
        val reason: MtnErrorCode? = null
)
