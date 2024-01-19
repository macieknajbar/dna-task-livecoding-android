package io.dnatechnology.dnataskandroid.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dnatechnology.dnataskandroid.api.purchase.Product
import io.dnatechnology.dnataskandroid.api.purchase.PurchaseApiClient
import io.dnatechnology.dnataskandroid.main.model.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    purchaseApiClient: PurchaseApiClient,
    private val getProductsUseCase: suspend () -> List<Product> = purchaseApiClient::getProducts,
    private val mainViewStateMapper: MainViewStateMapper = MainViewStateMapper(),
) : ViewModel() {

    private val state = MutableStateFlow(State())
    val viewState = state.map(mainViewStateMapper::map)

    init {
        viewModelScope.launch {
            state.update { it.copy(products = getProductsUseCase()) }
        }
    }

    fun addToCart(productID: String) {
        state.update {
            val cart = it.cart.toMutableMap()
            val product = checkNotNull(it.products).first { product -> product.productID == productID }

            val isProductAvailable = (cart[productID] ?: 0L) < product.maxAmount
            if (isProductAvailable) {
                val amount = cart.getOrPut(productID) { 0L }
                cart[productID] = amount.inc()
            }

            it.copy(cart = cart)
        }
    }

    fun removeFromCart(productID: String) {
        state.update {
            val cart = it.cart.toMutableMap()

            when (val value = cart[productID]) {
                null -> Unit
                1L -> cart.remove(productID)
                else -> cart[productID] = value.dec()
            }

            it.copy(cart = cart)
        }
    }

    data class State(
        val products: List<Product>? = null,
        val cart: Map<String, Long> = mapOf(),
    )

    data class ViewState(
        val products: List<ProductModel>,
        val isLoadingVisible: Boolean,
        val payButtonText: String,
    )
}