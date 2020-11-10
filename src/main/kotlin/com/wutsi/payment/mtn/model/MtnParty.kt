package com.wutsi.payment.mtn.model


data class MtnParty(
        val partyId: String = "",
        val partyIdType: MtnPartyType = MtnPartyType.MSISDN
)
