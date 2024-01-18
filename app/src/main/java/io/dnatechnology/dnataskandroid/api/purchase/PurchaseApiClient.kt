package io.dnatechnology.dnataskandroid.api.purchase

import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseCancelRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseConfirmRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseResponse
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseStatusResponse

interface PurchaseApiClient {
    suspend fun getProducts(): List<Product>
    suspend fun initiatePurchaseTransaction(purchaseRequest: PurchaseRequest): PurchaseResponse
    suspend fun confirm(purchaseRequest: PurchaseConfirmRequest): PurchaseStatusResponse
    suspend fun cancel(purchaseRequest: PurchaseCancelRequest): PurchaseStatusResponse
}