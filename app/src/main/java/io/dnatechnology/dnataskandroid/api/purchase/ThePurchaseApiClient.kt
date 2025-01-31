package io.dnatechnology.dnataskandroid.api.purchase

import io.dnatechnology.dnataskandroid.api.purchase.model.ProductRemote
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseCancelRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseConfirmRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseResponse
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseStatusResponse
import io.dnatechnology.dnataskandroid.api.purchase.model.TransactionStatusRemote
import kotlinx.coroutines.delay
import java.util.UUID

class ThePurchaseApiClient : PurchaseApiClient {
    companion object {
        val productList = listOf(
            ProductRemote("12345", "Big soda",123, 2.99, "EUR", 0.22),
            ProductRemote("12346", "Medium soda", 3, 1.95, "EUR", 0.22),
            ProductRemote("12347", "Small soda",1000, 1.25, "EUR", 0.22),
            ProductRemote("12348", "Chips",2000, 4.33, "EUR", 0.22),
            ProductRemote("12349", "Snack bar", 0, 10.99, "EUR", 0.23),
        )
    }

    override suspend fun getProducts(): List<ProductRemote> {
        delay(300)
        return productList
    }

    override suspend fun initiatePurchaseTransaction(purchaseRequest: PurchaseRequest): PurchaseResponse {
        delay(250)
        if (purchaseRequest.order.isEmpty()) {
            return PurchaseResponse(purchaseRequest.order, UUID.randomUUID().toString(), TransactionStatusRemote.FAILED)
        }

        return try {
            purchaseRequest.order.map { entry ->
                val orderedProduct = productList.first { product -> product.productID == entry.key }
                if (entry.value <= 0)
                    throw Exception("Not allowed to order not positive number of items")

                if (entry.value > orderedProduct.maxAmount)
                    throw Exception("Not allowed to order more then maxAmount")

                entry.value * orderedProduct.unitNetValue * (1.0 + orderedProduct.tax)
            }.sum()

            PurchaseResponse(purchaseRequest.order, UUID.randomUUID().toString(), TransactionStatusRemote.INITIATED)
        } catch (e: Exception) {
            PurchaseResponse(purchaseRequest.order, UUID.randomUUID().toString(), TransactionStatusRemote.FAILED)
        }
    }

    override suspend fun confirm(purchaseRequest: PurchaseConfirmRequest): PurchaseStatusResponse {
        delay(250)
        if (purchaseRequest.order.isEmpty()) {
            return PurchaseStatusResponse(purchaseRequest.transactionID, TransactionStatusRemote.FAILED)
        }

        try {
            val sum = purchaseRequest.order.map { entry ->
                val orderedProduct = productList.first { product -> product.productID == entry.key }

                if (entry.value <= 0)
                    throw Exception("Not allowed to order not positive number of items")

                entry.value * orderedProduct.unitNetValue * (1.0 + orderedProduct.tax)
            }.sum()

            if (sum > 100.0) {
                return PurchaseStatusResponse(purchaseRequest.transactionID, TransactionStatusRemote.FAILED)
            }

            return PurchaseStatusResponse(purchaseRequest.transactionID, TransactionStatusRemote.CONFIRMED)
        } catch (e: Exception) {
            return PurchaseStatusResponse(purchaseRequest.transactionID, TransactionStatusRemote.FAILED)
        }
    }

    override suspend fun cancel(purchaseRequest: PurchaseCancelRequest): PurchaseStatusResponse {
        delay(250)
        return PurchaseStatusResponse(purchaseRequest.transactionID, TransactionStatusRemote.CANCELLED)
    }

}