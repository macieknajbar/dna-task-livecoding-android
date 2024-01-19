package io.dnatechnology.dnataskandroid.main.vm

import io.dnatechnology.dnataskandroid.main.model.ProductModel

class MainViewStateMapper {
    fun map(state: MainViewModel.State): MainViewModel.ViewState {
        return MainViewModel.ViewState(
            products = state.products.orEmpty().map {
                val itemText = state.cart[it.productID]?.let { "${it}x - " } ?: ""
                ProductModel(
                    id = it.productID,
                    text = itemText + it.toString(),
                    isRemoveIconVisible = state.cart[it.productID] != null,
                )
            },
            isLoadingVisible = state.products == null,
            payButtonText = state.cart.takeUnless { it.isEmpty() }
                ?.let { " (${it.values.reduce { acc, l -> acc + l }})" } ?: "",
        )
    }
}