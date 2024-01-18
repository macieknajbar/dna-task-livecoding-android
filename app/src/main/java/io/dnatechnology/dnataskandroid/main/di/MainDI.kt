package io.dnatechnology.dnataskandroid.main.di

import io.dnatechnology.dnataskandroid.api.purchase.PurchaseApiClient
import io.dnatechnology.dnataskandroid.main.vm.viewmodel.MainViewModel

fun mainViewModel() =
    MainViewModel(
        purchaseApiClient = PurchaseApiClient(),
    )
