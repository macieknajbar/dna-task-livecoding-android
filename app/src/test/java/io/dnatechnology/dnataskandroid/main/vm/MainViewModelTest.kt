package io.dnatechnology.dnataskandroid.main.vm

import io.dnatechnology.dnataskandroid.api.purchase.PurchaseApiClient
import io.dnatechnology.dnataskandroid.api.purchase.model.ProductRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainViewModelTest {

    init {
        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun `ON init SHOULD get products`() = runTest {
        val purchaseApiClient: PurchaseApiClient = mock()

        sut(purchaseApiClient = purchaseApiClient)

        verify(purchaseApiClient).getProducts()
    }

    @Test
    fun `ON addToCart SHOULD change state`() = runTest {
        val product = ProductRemote("product_id", "Product name", 3L, 4.4, "EUR", 0.22)
        val products = listOf(product)
        val purchaseApiClient: PurchaseApiClient = mock()

        `when`(purchaseApiClient.getProducts()).thenReturn(products)

        val state = sut(purchaseApiClient = purchaseApiClient).apply {
            advanceUntilIdle()
            addToCart(product.productID)
        }.state

        assertEquals(
            MainViewModel.State(
                products = products,
                cart = mapOf(product.productID to 1L)
            ),
            state.value
        )
    }

    private fun sut(
        purchaseApiClient: PurchaseApiClient = mock(),
    ) = MainViewModel(
        purchaseApiClient = purchaseApiClient,
    )
}