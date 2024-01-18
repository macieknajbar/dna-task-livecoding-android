package io.dnatechnology.dnataskandroid

import io.dnatechnology.dnataskandroid.api.payment.PaymentApiClient
import io.dnatechnology.dnataskandroid.api.purchase.PurchaseApiClient
import io.dnatechnology.dnataskandroid.api.payment.model.PaymentRequest
import io.dnatechnology.dnataskandroid.api.payment.model.PaymentStatusRemote
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseConfirmRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.PurchaseRequest
import io.dnatechnology.dnataskandroid.api.purchase.model.TransactionStatusRemote
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class PaymentAPITests {
    private val paymentAPI = PaymentApiClient()

    @Test
    fun whenCorrectDataThenSuccess() = runBlocking {
        // given
        val paymentRequest = PaymentRequest(
            transactionID = "Tr1",
            amount = 33.66,
            currency = "EUR",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.pay(paymentRequest)

        // then
        assertEquals(paymentResponse.status, PaymentStatusRemote.SUCCESS)
    }

    @Test
    fun whenIncorrectAmountThenFail() = runBlocking {
        // given
        val paymentRequest = PaymentRequest(
            transactionID = "Tr1",
            amount = 19.66,
            currency = "EUR",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.pay(paymentRequest)

        // then
        assertEquals(paymentResponse.status, PaymentStatusRemote.FAILED)
    }

    @Test
    fun whenIncorrectCurrencyThenFail() = runBlocking {
        // given
        val paymentRequest = PaymentRequest(
            transactionID = "Tr1",
            amount = 22.66,
            currency = "PLN",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.pay(paymentRequest)

        // then
        assertEquals(paymentResponse.status, PaymentStatusRemote.FAILED)
    }

    @Test
    fun whenRevertingCorrectAmountThenSuccess() = runBlocking {
        // given
        val paymentRequest = PaymentRequest(
            transactionID = "Tr1",
            amount = 12.66,
            currency = "EUR",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.revert(paymentRequest)

        // then
        assertEquals(paymentResponse.status, PaymentStatusRemote.SUCCESS)
    }

    @Test
    fun whenRevertingIncorrectAmountThenFail() = runBlocking {
        // given
        val paymentRequest = PaymentRequest(
            transactionID = "Tr1",
            amount = 0.66,
            currency = "EUR",
            cardToken = "Token"
        )

        // when
        val paymentResponse =  paymentAPI.revert(paymentRequest)

        // then
        assertEquals(paymentResponse.status, PaymentStatusRemote.FAILED)
    }
}

class PurchaseAPITests {
    private val purchaseApiClient = PurchaseApiClient()

    @Test
    fun whenGetProductsThenSuccess() = runBlocking {
        // when
        val products =  purchaseApiClient.getProducts()

        // then
        assertEquals(products.size, 5)
    }

    @Test
    fun whenInitiateEmptyOrderThenFail() = runBlocking {
        // given
       val purchaseRequest = PurchaseRequest(
           mapOf()
       )

        // when
        val purchaseResponse =  purchaseApiClient.initiatePurchaseTransaction(purchaseRequest)

        // then
        assertEquals(purchaseResponse.transactionStatus, TransactionStatusRemote.FAILED)
    }

    @Test
    fun whenInitiateOrderWithZeroItemsThenFail() = runBlocking {
        // given
        val purchaseRequest = PurchaseRequest(
            mapOf("12345" to 0)
        )

        // when
        val purchaseResponse =  purchaseApiClient.initiatePurchaseTransaction(purchaseRequest)

        // then
        assertEquals(purchaseResponse.transactionStatus, TransactionStatusRemote.FAILED)
    }

    @Test
    fun whenInitiateOrderWithToManyItemsThenFail() = runBlocking {
        // given
        val purchaseRequest = PurchaseRequest(
            mapOf("12348" to 2001)
        )

        // when
        val purchaseResponse =  purchaseApiClient.initiatePurchaseTransaction(purchaseRequest)

        // then
        assertEquals(purchaseResponse.transactionStatus, TransactionStatusRemote.FAILED)
    }

    @Test
    fun whenConfirmrderWithToManyItemsThenFail() = runBlocking {
        // given
        val purchaseRequest = PurchaseConfirmRequest(
            mapOf("12348" to 2001),
            "Tr2"
        )

        // when
        val purchaseResponse =  purchaseApiClient.confirm(purchaseRequest)

        // then
        assertEquals(purchaseResponse.status, TransactionStatusRemote.FAILED)
    }
}