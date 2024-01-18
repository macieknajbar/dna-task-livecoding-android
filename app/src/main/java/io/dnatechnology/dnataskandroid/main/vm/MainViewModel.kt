package io.dnatechnology.dnataskandroid.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dnatechnology.dnataskandroid.api.purchase.Product
import io.dnatechnology.dnataskandroid.api.purchase.PurchaseApiClient
import io.dnatechnology.dnataskandroid.api.purchase.ThePurchaseApiClient
import io.dnatechnology.dnataskandroid.main.model.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val purchaseApiClient: PurchaseApiClient,
    private val mainViewStateMapper: MainViewStateMapper = MainViewStateMapper(),
) : ViewModel() {

    private val state = MutableStateFlow(State())
    val viewState = state.map(mainViewStateMapper::map)

    init {
        viewModelScope.launch {
            state.update { it.copy(products = purchaseApiClient.getProducts()) }
        }
    }

    fun addToCart(productID: String) {
        state.update {
            val cart = it.cart.toMutableMap()
            cart[productID] = cart.getOrPut(productID) { 0L } + 1L
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