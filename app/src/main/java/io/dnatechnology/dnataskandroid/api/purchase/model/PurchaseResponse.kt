package io.dnatechnology.dnataskandroid.api.purchase.model

data class PurchaseResponse(
    val order: Map<String, Long>,
    val transactionID: String,
    val transactionStatus: TransactionStatusRemote,
)