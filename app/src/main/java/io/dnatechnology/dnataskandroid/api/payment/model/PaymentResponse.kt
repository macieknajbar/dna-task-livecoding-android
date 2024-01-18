package io.dnatechnology.dnataskandroid.api.payment.model

data class PaymentResponse(
    val transactionID: String,
    val status: PaymentStatusRemote
)