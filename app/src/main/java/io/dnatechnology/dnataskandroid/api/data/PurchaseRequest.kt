package io.dnatechnology.dnataskandroid.api.data

data class PurchaseRequest(val order: Map<String,Long>)

data class PurchaseResponse(val order: Map<String, Long>, val transactionID: String, val transactionStatus: TransactionStatus)

data class PurchaseConfirmRequest(val order: Map<String, Long>, val transactionID: String)

data class PurchaseCancelRequest(val transactionID: String)

data class PurchaseStatusResponse(val transactionID: String, val status: TransactionStatus)

enum class TransactionStatus {
    INITIATED,
    CONFIRMED,
    CANCELLED,
    FAILED
}
