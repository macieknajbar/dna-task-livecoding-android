package io.dnatechnology.dnataskandroid.api.purchase.model

data class PurchaseConfirmRequest(
    val order: Map<String, Long>,
    val transactionID: String
)