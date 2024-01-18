package io.dnatechnology.dnataskandroid.main.vm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.dnatechnology.dnataskandroid.api.purchase.Product
import io.dnatechnology.dnataskandroid.api.purchase.PurchaseApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val purchaseApiClient: PurchaseApiClient = PurchaseApiClient(),
): ViewModel() {

    private val mutableCart = MutableStateFlow<Map<String, Long>>(mapOf())
    val cart: StateFlow<Map<String, Long>> = mutableCart

    private val mutableProducts = MutableStateFlow<List<Product>?>(null)
    val products: StateFlow<List<Product>?> = mutableProducts

    init {
        viewModelScope.launch {
            mutableProducts.value = purchaseApiClient.getProducts()
        }
    }

    fun addToCart(productID: String) {

        val newMap = mutableCart.value.toMutableMap()
        newMap[productID] = (mutableCart.value[productID] ?: 0L) + 1L

        mutableCart.value = newMap.toMap()
    }
}