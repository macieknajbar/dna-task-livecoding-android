package io.dnatechnology.dnataskandroid.main.features.payment.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dnatechnology.dnataskandroid.api.payment.PaymentApiClient
import io.dnatechnology.dnataskandroid.api.payment.model.PaymentRequest
import io.dnatechnology.dnataskandroid.api.payment.model.PaymentStatusRemote
import io.dnatechnology.dnataskandroid.api.purchase.PurchaseApiClient
import io.dnatechnology.dnataskandroid.api.purchase.model.ProductRemote
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseCancelRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseConfirmRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseResponse
import io.dnatechnology.dnataskandroid.api.purchase.model.TransactionStatusRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode

class PaymentViewModel(
    private val holder: Holder,
    private val purchaseApiClient: PurchaseApiClient,
    private val paymentApiClient: PaymentApiClient,
) : ViewModel() {

    val effect: MutableStateFlow<Effect?> = MutableStateFlow(null)

    fun send() {
        viewModelScope.launch {
            val orderedProducts = purchaseApiClient.getProducts()
                .filter { holder.orderedItems.contains(it.productID) }
                .associateWith {
                    val idx = holder.orderedItems.indexOf(it.productID)
                    holder.itemsAmounts[idx]
                }

            val purchaseResponse =
                purchaseApiClient.initiatePurchaseTransaction(PurchaseRequest(order = holder.order))
                    .apply(::println)

            when (val status = purchaseResponse.transactionStatus) {
                TransactionStatusRemote.INITIATED -> pay(purchaseResponse.transactionID, orderedProducts)
                else -> handleStatus(status)
            }
        }
    }

    private suspend fun pay(
        transactionID: String,
        orderedProducts: Map<ProductRemote, Long>,
    ) {
        val response = paymentApiClient.pay(
            PaymentRequest(
                transactionID = transactionID,
                amount = orderedProducts
                    .map {
                        (it.value * it.key.unitNetValue * (1.0 + it.key.tax))
                            .toBigDecimal()
                            .setScale(2, RoundingMode.UP)
                            .toDouble()
                    }.sum(),
                currency = orderedProducts.keys.first().unitValueCurrency,
                cardToken = "#token",
            )
        )

        when (response.status) {
            PaymentStatusRemote.SUCCESS -> {
                purchaseApiClient
                    .confirm(PurchaseConfirmRequest(holder.order, response.transactionID))
                    .apply(::println)
                    .status
                    .let(::handleStatus)
            }

            PaymentStatusRemote.FAILED -> {
                purchaseApiClient
                    .cancel(PurchaseCancelRequest(transactionID = response.transactionID))
                    .apply(::println)
                    .status
                    .let(::handleStatus)
            }
        }
    }

    private fun handleStatus(
        transactionStatus: TransactionStatusRemote,
    ) {
        when (transactionStatus) {
            TransactionStatusRemote.CONFIRMED -> {
                effect.value = Effect.PurchaseSuccessful
                effect.value = null
            }

            TransactionStatusRemote.CANCELLED -> {
                effect.value = Effect.PurchaseCancelled
                effect.value = null
            }

            TransactionStatusRemote.FAILED -> {
                effect.value = Effect.PurchaseFailed
                effect.value = null
            }

            else -> Unit
        }
    }

    data class Holder(
        val orderedItems: List<String>,
        val itemsAmounts: List<Long>,
    ) {
        val order: Map<String, Long> = orderedItems
            .mapIndexed { index, s -> s to itemsAmounts[index] }
            .toMap()
    }

    sealed class Effect {
        object PurchaseSuccessful : Effect()
        object PurchaseFailed : Effect()
        object PurchaseCancelled : Effect()
    }
}