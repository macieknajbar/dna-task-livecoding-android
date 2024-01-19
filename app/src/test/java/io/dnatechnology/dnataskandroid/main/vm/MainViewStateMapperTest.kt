package io.dnatechnology.dnataskandroid.main.vm

import io.dnatechnology.dnataskandroid.api.purchase.model.ProductRemote
import io.dnatechnology.dnataskandroid.main.model.ProductModel
import org.junit.Assert.assertEquals
import org.junit.Test

internal class MainViewStateMapperTest {

    @Test
    fun `ON map SHOULD show Loading`() {
        val state = MainViewModel.State(products = null)

        assertEquals(
            viewState.copy(isLoadingVisible = true),
            sut().map(state)
        )
    }

    @Test
    fun `ON map SHOULD return view state`() {
        val product1 = ProductRemote("p1", "P 1", 10L, 4.4, "USD", 0.4)
        val product2 = ProductRemote("p2", "P 1", 10L, 4.4, "USD", 0.4)
        val products = listOf(product1, product2)
        val state = MainViewModel.State(products = products)

        assertEquals(
            viewState.copy(
                products = listOf(
                    ProductModel(
                        id = product1.productID,
                        text = product1.toString(),
                        isRemoveIconVisible = false,
                    ),
                    ProductModel(
                        id = product2.productID,
                        text = product1.toString(),
                        isRemoveIconVisible = false,
                    ),
                ),
                isLoadingVisible = false,
            ),
            sut().map(state)
        )
    }

    @Test
    fun `ON map SHOULD hide Loading`() {
        val state = MainViewModel.State(products = emptyList())

        assertEquals(
            viewState,
            sut().map(state)
        )
    }

    @Test
    fun `ON map SHOULD change pay button text`() {
        val products1 = ProductRemote("1", "", 1L, 1.0, "", 2.0)
        val products2 = ProductRemote("2", "", 2L, 1.0, "", 2.0)
        val products3 = ProductRemote("3", "", 2L, 1.0, "", 2.0)
        val state = MainViewModel.State(
            products = listOf(products1, products2, products3),
            cart = mapOf("1" to 1L, "2" to 2L),
        )

        assertEquals(
            viewState.copy(
                products = listOf(
                    ProductModel("1", "1x - $products1", true),
                    ProductModel("2", "2x - $products2", true),
                    ProductModel("3", "$products3", false),
                ),
                payButtonText = " (3)",
            ),
            sut().map(state)
        )
    }

    private fun sut() = MainViewStateMapper()

    private val viewState = MainViewModel.ViewState(
        products = emptyList(),
        isLoadingVisible = false,
        payButtonText = "",
    )
}