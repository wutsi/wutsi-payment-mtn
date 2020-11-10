package com.wutsi.payment.mtn.model

data class MtnTransferRequest(
        val amount: String,
        val currency: String,
        val externalId: String,
        val payeeNote: String,
        val payerMessage: String,
        val payee: MtnParty
)
