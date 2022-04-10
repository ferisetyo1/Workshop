package com.feri.workshop.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.R
import com.feri.workshop.data.model.Produk
import com.feri.workshop.ui.helper.dividerSmallH
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.PrimaryColor
import com.feri.workshop.utils.capitalizeWords
import com.feri.workshop.utils.toRupiahCurrency

object ListProduk : Screen {
    override val routeName = "ListProduk"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val produkVM = getMainActivity().produkViewModel
        LaunchedEffect(key1 = true ){
            produkVM.listProduk()
        }
        val createTransaksiViewModel = getMainActivity().transaksiViewModel
        val selectedproduk by remember { createTransaksiViewModel.selectedProduk }
        val produks by remember { produkVM.listProduk }
        val isLoading by remember { produkVM.loadingListProduk }
        val keyboard = LocalSoftwareKeyboardController.current
        var search by remember { mutableStateOf("") }
        Box(Modifier.fillMaxSize()) {
            screenLoading(loading = isLoading)
            Column(Modifier.fillMaxSize()) {
                spacerV(height = 16.dp)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    spacerH(width = 8.dp)
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
                    }
                    spacerH(width = 4.dp)
                    OutlinedTextField(
                        value = search,
                        onValueChange = {
                            search = it
                            produkVM.listProduk(query = search)
                        },
                        placeholder = { Text(text = "Cari produk kamu", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "")
                        }
                    )
                }
                spacerV(height = 8.dp)
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(produks.filter { (it.getJumlahStock() > 0 && it.tipe == Produk.Tipe.BARANG) || it.tipe == Produk.Tipe.JASA }
                        .groupBy { it.kategori.orEmpty() }.toList()) { item ->
                        spacerV(height = 16.dp)
                        Text(text = item.first, fontWeight = FontWeight.W600)
                        spacerV(height = 16.dp)
                        dividerSmallH()
                        item.second.forEach {
                            spacerV(height = 16.dp)
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (it.tipe == Produk.Tipe.BARANG) Icon(
                                        painterResource(id = R.drawable.ic_box),
                                        contentDescription = ""
                                    )
                                    else Icon(
                                        imageVector = Icons.Default.BuildCircle,
                                        contentDescription = "",
                                        modifier = Modifier.size(26.dp)
                                    )
                                    spacerH(width = 16.dp)
                                    Column {
                                        Text(
                                            text = it.nama.orEmpty().capitalizeWords(),
                                            fontWeight = FontWeight.W600
                                        )
                                        if (it.diskon ?: 0.0 > 0.0) Text(
                                            text = it.harga.toRupiahCurrency(),
                                            textDecoration = TextDecoration.LineThrough,
                                            color = Color.Red
                                        )
                                        Text(
                                            text = ((it.harga ?: 0.0) - (it.diskon
                                                ?: 0.0)).toRupiahCurrency()
                                        )
                                        if (it.tipe == Produk.Tipe.BARANG) Text(
                                            text = "Qty ${it.getJumlahStock()}",
                                            fontSize = 12.sp,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    selectedproduk.firstOrNull { it1 -> it1.produk!!.id == it.id }
                                        ?.let {
                                            if (it.produk!!.tipe == Produk.Tipe.BARANG) {
                                                IconButton(onClick = {
                                                    createTransaksiViewModel.addProduk(
                                                        jumlah = it.jumlah!! - 1,
                                                        produk = it.produk!!
                                                    )
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.RemoveCircleOutline,
                                                        contentDescription = ""
                                                    )
                                                }
                                                BasicTextField(
                                                    value = it.jumlah.toString(),
                                                    onValueChange = { text ->
                                                        if (!text.isEmpty()) {
                                                            createTransaksiViewModel.addProduk(
                                                                jumlah = text.toIntOrNull() ?: 0,
                                                                produk = it.produk!!
                                                            )
                                                        }
                                                    },
                                                    textStyle = TextStyle(
                                                        color = Color.White,
                                                        textAlign = TextAlign.Center
                                                    ),
                                                    cursorBrush = SolidColor(value = Color.White),
                                                    modifier = Modifier.width(50.dp),
                                                    singleLine = true,
                                                    keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                                                    keyboardOptions = KeyboardOptions(
                                                        keyboardType = KeyboardType.Number,
                                                        imeAction = ImeAction.Done
                                                    )
                                                )
                                                IconButton(onClick = {
                                                    createTransaksiViewModel.addProduk(
                                                        jumlah = it.jumlah!! + 1,
                                                        produk = it.produk!!
                                                    )
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.AddCircleOutline,
                                                        contentDescription = ""
                                                    )
                                                }
                                            } else {
                                                Text(text = "1x")
                                                IconButton(onClick = {
                                                    createTransaksiViewModel.addProduk(
                                                        jumlah = 0,
                                                        produk = it.produk!!
                                                    )
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.RemoveShoppingCart,
                                                        contentDescription = ""
                                                    )
                                                }
                                            }
                                        }
                                        ?: IconButton(onClick = { createTransaksiViewModel.addProduk(produk = it) }) {
                                            Icon(
                                                imageVector = Icons.Default.AddShoppingCart,
                                                contentDescription = ""
                                            )
                                        }
                                }
                            }
                        }
                    }
                    item {
                        spacerV(height = 60.dp)
                    }
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black, RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "Total Harga", fontSize = 12.sp)
                            Text(
                                text = selectedproduk.map {
                                    it.jumlah!! * ((it.produk!!.harga ?: 0.0) - (it.produk.diskon
                                        ?: 0.0))
                                }.sum().toRupiahCurrency(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500
                            )
                        }

                        Button(
                            onClick = {
                                navController.navigate(Checkout.routeName)
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = buttonColors(
                                backgroundColor = PrimaryColor,
                                disabledBackgroundColor = Color.DarkGray
                            ),
                            enabled = selectedproduk.isNotEmpty()
                        ) {
                            Text(text = "Lanjutkan")
                        }
                    }
                }
            }
        }
    }
}