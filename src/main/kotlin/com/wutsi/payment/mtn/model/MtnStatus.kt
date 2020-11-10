package com.wutsi.payment.mtn.model

import com.wutsi.payment.PaymentStatus


enum class MtnStatus(val paymenStatus: PaymentStatus){
    PENDING(PaymentStatus.PENDING),
    SUCCESSFUL(PaymentStatus.SUCCESSFUL),
    FAILED(PaymentStatus.FAILED)
}
