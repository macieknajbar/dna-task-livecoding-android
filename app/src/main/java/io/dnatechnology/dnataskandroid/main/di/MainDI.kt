package io.dnatechnology.dnataskandroid.main.di

import io.dnatechnology.dnataskandroid.api.purchase.ThePurchaseApiClient
import io.dnatechnology.dnataskandroid.main.vm.MainViewModel

object MainDI {

    fun mainViewModel() =
        MainViewModel(
            purchaseApiClient = ThePurchaseApiClient(),
        )
}
