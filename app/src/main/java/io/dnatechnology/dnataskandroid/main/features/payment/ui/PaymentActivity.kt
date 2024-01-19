package io.dnatechnology.dnataskandroid.main.features.payment.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.lifecycleScope
import io.dnatechnology.dnataskandroid.R
import io.dnatechnology.dnataskandroid.main.features.payment.di.PaymentDI
import io.dnatechnology.dnataskandroid.main.features.payment.vm.PaymentViewModel
import io.dnatechnology.dnataskandroid.theme.DNATaskAndroidTheme
import io.dnatechnology.dnataskandroid.theme.MainBackground
import io.dnatechnology.dnataskandroid.utils.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PaymentActivity : ComponentActivity() {

    private val viewModel: PaymentViewModel by viewModel {
        PaymentDI.paymentViewModel(
            holder = PaymentViewModel.Holder(
                orderedItems = intent.getStringArrayExtra(EXTRA_ORDER_ITEMS)?.toList().orEmpty(),
                itemsAmounts = intent.getLongArrayExtra(EXTRA_ITEM_AMOUNTS)?.toList().orEmpty(),
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.effect.collectLatest {
                when (it) {
                    PaymentViewModel.Effect.PurchaseCancelled -> {
                        Toast.makeText(
                            this@PaymentActivity,
                            getString(R.string.purchase_cancelled),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    PaymentViewModel.Effect.PurchaseFailed -> {
                        Toast.makeText(
                            this@PaymentActivity,
                            getString(R.string.purchase_failed),
                            Toast.LENGTH_SHORT
                        ) .show()
                        finish()
                    }

                    PaymentViewModel.Effect.PurchaseSuccessful -> {
                        Toast.makeText(
                            this@PaymentActivity,
                            getString(R.string.purchase_successful),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    null -> Unit
                }
            }
        }
        viewModel.send()

        setContent {
            DNATaskAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MainBackground
                ) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Initializing payment",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_ORDER_ITEMS = "PaymentActivity:EXTRA_ORDER_ITEMS"
        const val EXTRA_ITEM_AMOUNTS = "PaymentActivity:EXTRA_ITEM_AMOUNTS"
    }
}