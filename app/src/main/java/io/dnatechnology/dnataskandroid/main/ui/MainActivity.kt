package io.dnatechnology.dnataskandroid.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.dnatechnology.dnataskandroid.R
import io.dnatechnology.dnataskandroid.main.vm.viewmodel.MainViewModel
import io.dnatechnology.dnataskandroid.theme.Black
import io.dnatechnology.dnataskandroid.theme.DNATaskAndroidTheme
import io.dnatechnology.dnataskandroid.theme.MainBackground
import io.dnatechnology.dnataskandroid.theme.White

class RootComposeActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DNATaskAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MainBackground
                ) {
                    ProductsView(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ProductsView(viewModel: MainViewModel) {
    DNATaskAndroidTheme {
        LaunchedEffect(Unit) {
            viewModel.getProducts()
        }

        val products = viewModel.products.collectAsState().value

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (products != null) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (product in products) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp)
                                .background(White)
                                .border(1.dp, Black)
                                .clickable {
                                    viewModel.addToCart(product.productID)
                                }
                        ) {
                            Text(
                                text = product.toString(),
                                color = Black,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            } else {
                Text(text = "LOADING")
            }


            Row(
                Modifier
                    .background(White)
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.pay), color = Black)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DNATaskAndroidTheme {
        ProductsView(viewModel = MainViewModel())
    }
}