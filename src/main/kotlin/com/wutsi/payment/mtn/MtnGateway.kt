package com.wutsi.payment.mtn

import com.wutsi.payment.Error
import com.wutsi.payment.ErrorCode
import com.wutsi.payment.Gateway
import com.wutsi.payment.MobileWallet
import com.wutsi.payment.PaymentException
import com.wutsi.payment.TransferRequest
import com.wutsi.payment.TransferResponse
import com.wutsi.payment.TransferStatusResponse
import com.wutsi.payment.mtn.api.MtnDisbursementApi
import com.wutsi.payment.mtn.model.MtnParty
import com.wutsi.payment.mtn.model.MtnPartyType
import com.wutsi.payment.mtn.model.MtnTransferRequest
import com.wutsi.payment.mtn.model.MtnTransferResponse
import java.util.UUID

class MtnGateway(
        private val disbursementApi: MtnDisbursementApi
): Gateway {
    override fun transfer(request: TransferRequest): TransferResponse {
        val transactionId = UUID.randomUUID().toString()
        try {
            disbursementApi.transfer(
                    referenceId = transactionId,
                    accessToken = disbursementApi.token().access_token,
                    request = toMtnTransferRequest(request)
            )

            return TransferResponse(transactionId)
        } catch (ex: Throwable){
            throw toPaymentException(transactionId, ex)
        }
    }

    override fun transferStatus(transactionId: String): TransferStatusResponse {
        try {
            val response = disbursementApi.getTransfer(
                    referenceId = transactionId,
                    accessToken = disbursementApi.token().access_token
            )

            return toTransferStatusResponse(transactionId, response)
        } catch (ex: Throwable){
            throw toPaymentException(transactionId, ex)
        }
    }

    private fun toTransferStatusResponse(transactionId: String, response: MtnTransferResponse): TransferStatusResponse =
        TransferStatusResponse(
                transactionId = transactionId,
                status = response.status.paymenStatus,
                error = if (response.reason != null)
                    Error(
                            code = response.reason.paymentErrorCode,
                            supplierErrorCode = response.reason.name,
                            transactionId = transactionId
                    )
                else
                    null
        )

    private fun toMtnTransferRequest(request: TransferRequest): MtnTransferRequest =
        MtnTransferRequest(
                payee = MtnParty(
                        partyId = (request.wallet as MobileWallet).number,
                        partyIdType = MtnPartyType.MSISDN
                ),
                currency = request.currency,
                amount = request.amount.toString(),
                payerMessage = request.description ?: "-",
                payeeNote = "Transfer of ${request.amount} ${request.currency} to ${request.party.fullName}",
                externalId = request.externalId
        )

    private fun toPaymentException(transactionId: String, ex: Throwable): PaymentException =
        if (ex is MtnException){

            PaymentException(
                    error = Error(
                            transactionId = transactionId,
                            code = ex.error?.code?.paymentErrorCode ?: ErrorCode.UNKNOWN_ERROR,
                            supplierErrorCode = ex.error?.code?.paymentErrorCode?.name
                    ),
                    message = ex.message,
                    cause = ex
            )

        } else {

            PaymentException(
                    error = Error(
                            transactionId = transactionId,
                            code = ErrorCode.UNKNOWN_ERROR
                    ),
                    message = ex.message,
                    cause = ex
            )
        }
}
