package io.dnatechnology.dnataskandroid.api.payment.model

data class PaymentRequest(
    val transactionID: String,
    val amount: Double,
    val currency: String,
    val cardToken: String
)

