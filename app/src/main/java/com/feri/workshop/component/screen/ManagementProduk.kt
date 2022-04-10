package com.feri.workshop.component.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.R
import com.feri.workshop.data.model.Produk
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.capitalizeWords
import com.feri.workshop.utils.toRupiahCurrency

object ManagementProduk : Screen {
    override val routeName = "ManagementProduk"
    override val showBottomNav = true
    override val icon: Int = R.drawable.ic_box

    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val produkVM = getMainActivity().produkViewModel
        val produks by remember { produkVM.listProduk }
        val isLoading by remember { produkVM.loadingListProduk }

        var search by remember { mutableStateOf("") }
        Box(Modifier.fillMaxSize()) {
            screenLoading(loading = isLoading)
            Column(Modifier.fillMaxSize()) {
                spacerV(height = 16.dp)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                    spacerH(width = 4.dp)
                    IconButton(onClick = { navController.navigate(AddProduk.routeName) }) {
                        Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "")
                    }
                }
                spacerV(height = 8.dp)
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(produks) {
                        spacerV(height = 16.dp)
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    produkVM.selectedproduct.value = it
                                    navController.navigate(DetailProduk.routeName)
                                },
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
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_healthicons_rdt_result_out_stock_outline),
                                            contentDescription = ""
                                        )
                                        spacerH(width = 4.dp)
                                        Text(
                                            text = "Stok ${
                                                if (it.tipe == Produk.Tipe.BARANG) it.stocks?.map { it.jumlah ?: 0 }
                                                    ?.sum()
                                                else "∞"
                                            }", fontSize = 12.sp
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_eos_icons_product_subscriptions),
                                            contentDescription = "",
                                            modifier = Modifier.offset(x = 2.dp, y = 2.5.dp)
                                        )
                                        spacerH(width = 4.dp)
                                        Text(text = "Terjual 10rb+", fontSize = 12.sp)
                                    }
                                }
                                spacerH(width = 14.dp)
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                    item {
                        spacerV(height = 80.dp)
                    }
                }
            }
        }
    }
}