package io.dnatechnology.dnataskandroid.main.vm

import io.dnatechnology.dnataskandroid.api.purchase.PurchaseApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class MainViewModelTest {

    init {
        Dispatchers.setMain(Dispatchers.Default)
    }

    @Test
    fun `ON init SHOULD get products`() = runBlocking<Unit> {
        val purchaseApiClient: PurchaseApiClient = mock()

        sut(purchaseApiClient = purchaseApiClient)

        verify(purchaseApiClient).getProducts()
    }

    private fun sut(
        purchaseApiClient: PurchaseApiClient = mock(),
    ) = MainViewModel(
        purchaseApiClient = purchaseApiClient,
    )
}