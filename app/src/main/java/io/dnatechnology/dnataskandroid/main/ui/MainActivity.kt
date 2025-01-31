package io.dnatechnology.dnataskandroid.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import io.dnatechnology.dnataskandroid.R
import io.dnatechnology.dnataskandroid.main.di.MainDI
import io.dnatechnology.dnataskandroid.main.features.payment.ui.PaymentActivity
import io.dnatechnology.dnataskandroid.main.model.ProductModel
import io.dnatechnology.dnataskandroid.main.vm.MainViewModel
import io.dnatechnology.dnataskandroid.theme.Black
import io.dnatechnology.dnataskandroid.theme.DNATaskAndroidTheme
import io.dnatechnology.dnataskandroid.theme.MainBackground
import io.dnatechnology.dnataskandroid.theme.White
import io.dnatechnology.dnataskandroid.utils.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel(MainDI::mainViewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEffectsHandler()

        setContent {
            DNATaskAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MainBackground
                ) {
                    viewModel.viewState.collectAsState(initial = null).value?.let {
                        ProductsView(
                            viewState = it,
                            onItemClicked = viewModel::addToCart,
                            onRemoveItemClicked = viewModel::removeFromCart,
                            onPayClicked = viewModel::onPayClicked,
                        )
                    }
                }
            }
        }
    }

    private fun initEffectsHandler() = lifecycleScope.launch {
        viewModel.effect.collectLatest {
            when (it) {
                null -> Unit

                is MainViewModel.Effect.GoToPaymentScreen -> {
                    startActivity(
                        Intent(this@MainActivity, PaymentActivity::class.java)
                            .putExtra(
                                PaymentActivity.EXTRA_ORDER_ITEMS,
                                it.orderItems.toTypedArray()
                            )
                            .putExtra(
                                PaymentActivity.EXTRA_ITEM_AMOUNTS,
                                it.itemAmounts.toLongArray()
                            )
                    )
                }

                MainViewModel.Effect.ItemMaxedOut ->
                    Toast.makeText(this@MainActivity, "Item maxed out", Toast.LENGTH_SHORT)
                        .show()

                MainViewModel.Effect.EmptyCart ->
                    Toast.makeText(this@MainActivity, "Empty cart", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun ProductsView(
    viewState: MainViewModel.ViewState,
    onItemClicked: (String) -> Unit = {},
    onRemoveItemClicked: (String) -> Unit = {},
    onPayClicked: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (viewState.isLoadingVisible) {
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                text = "LOADING",
            )
        } else {

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                viewState.products.map { product ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                                .background(White)
                                .border(1.dp, Black)
                                .clickable { onItemClicked(product.id) },
                        ) {
                            Text(
                                text = product.text,
                                color = Black,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .padding(vertical = 8.dp)
                                    .weight(1f)
                            )
                            if (product.isRemoveIconVisible) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    tint = Color.Black,
                                    contentDescription = "Remove",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable { onRemoveItemClicked(product.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .background(White)
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.pay) + viewState.payButtonText,
                color = Black,
                modifier = Modifier.clickable { onPayClicked() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProductsView(
        viewState = MainViewModel.ViewState(
            products = listOf(
                ProductModel("1", "Item 1", false),
                ProductModel("2", "Item 2", false),
                ProductModel("3", "Item 3", true),
            ),
            isLoadingVisible = false,
            payButtonText = " (2)",
        ),
    )
}