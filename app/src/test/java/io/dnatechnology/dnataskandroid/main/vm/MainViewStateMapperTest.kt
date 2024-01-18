package io.dnatechnology.dnataskandroid.main.vm

import io.dnatechnology.dnataskandroid.api.purchase.Product
import io.dnatechnology.dnataskandroid.main.model.ProductModel
import org.junit.Assert.assertEquals
import org.junit.Test

internal class MainViewStateMapperTest {

    @Test
    fun `ON map SHOULD return view state`() {
        val product1 = Product("p1", "P 1", 10L, 4.4, "USD", 0.4)
        val product2 = Product("p2", "P 1", 10L, 4.4, "USD", 0.4)
        val products = listOf(product1, product2)
        val state = MainViewModel.State(products = products)

        assertEquals(
            MainViewModel.ViewState(
                products = listOf(
                    ProductModel(
                        id = product1.productID,
                        text = product1.toString(),
                    ),
                    ProductModel(
                        id = product2.productID,
                        text = product1.toString(),
                    ),
                ),
                isLoadingVisible = false,
                payButtonText = "",
            ),
            sut().map(state)
        )
    }

    @Test
    fun `ON map SHOULD show Loading`() {
        val state = MainViewModel.State(products = null)

        assertEquals(
            MainViewModel.ViewState(
                products = emptyList(),
                isLoadingVisible = true,
                payButtonText = "",
            ),
            sut().map(state)
        )
    }

    @Test
    fun `ON map SHOULD hide Loading`() {
        val state = MainViewModel.State(products = emptyList())

        assertEquals(
            MainViewModel.ViewState(
                products = emptyList(),
                isLoadingVisible = false,
                payButtonText = "",
            ),
            sut().map(state)
        )
    }

    @Test
    fun `ON map SHOULD change pay button text`() {
        val state = MainViewModel.State(cart = mapOf("1" to 1L, "2" to 2L))

        assertEquals(
            MainViewModel.ViewState(
                products = emptyList(),
                isLoadingVisible = true,
                payButtonText = " (3)",
            ),
            sut().map(state)
        )
    }

    private fun sut() = MainViewStateMapper()
}