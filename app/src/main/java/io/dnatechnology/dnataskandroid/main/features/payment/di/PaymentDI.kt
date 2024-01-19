package io.dnatechnology.dnataskandroid.main.features.payment.di

import io.dnatechnology.dnataskandroid.api.payment.PaymentApiClient
import io.dnatechnology.dnataskandroid.api.purchase.ThePurchaseApiClient
import io.dnatechnology.dnataskandroid.cardReader.CardReaderService
import io.dnatechnology.dnataskandroid.main.features.payment.vm.PaymentViewModel

object PaymentDI {
    fun paymentViewModel(holder: PaymentViewModel.Holder) = PaymentViewModel(
        holder = holder,
        purchaseApiClient = ThePurchaseApiClient(),
        paymentApiClient = PaymentApiClient(),
        cardReaderService = CardReaderService(),
    )
}