package io.dnatechnology.dnataskandroid.api.purchase.model

data class PurchaseStatusResponse(
    val transactionID: String,
    val status: TransactionStatusRemote,
)