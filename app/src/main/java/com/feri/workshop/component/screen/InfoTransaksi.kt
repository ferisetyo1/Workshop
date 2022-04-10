package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.data.model.Transaksi
import com.feri.workshop.ui.helper.dividerSmallH
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.capitalizeWords
import com.feri.workshop.utils.toFormattedString
import com.feri.workshop.utils.toRupiahCurrency
import java.util.*

object InfoTransaksi : Screen {
    override val routeName = "InfoTransaksi"
    override val pageTitle = "Transaksi"

    @Composable
    override fun screen(navController: NavHostController) {
        val transaksiVM = getMainActivity().transaksiViewModel
        val transaksi by remember { transaksiVM.selectedTransaksi }
        var isLoading by remember { mutableStateOf(false) }
        val cart = transaksi?.carts.orEmpty().sumOf { it.getTotal() }
        val other = transaksi?.otherPayment.orEmpty().sumOf { it.value ?: 0.0 }
        val ppn =
            if (transaksi?.enablePPn == true) (cart * (transaksi?.ppnValue ?: 0)) / 100 else 0.0
        val total = cart + other + ppn
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = pageTitle)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                actions = {

                },
                backgroundColor = Color.Transparent,
                contentColor = Color.Transparent,
                elevation = 0.dp,
            )
        }) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        spacerV(height = 16.dp)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.WatchLater,
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                spacerH(width = 8.dp)
                                Text(
                                    text = Date(
                                        transaksi?.createdAt ?: 0
                                    ).toFormattedString("dd MMM yyyy | HH:mm"), fontSize = 12.sp
                                )
                            }
                            spacerH(width = 8.dp)
                            Text(
                                text = "USER ID ${transaksi?.customer?.notelp.orEmpty()}",
                                fontSize = 12.sp
                            )
                        }
                        spacerV(height = 16.dp)
                        dividerSmallH()
                        spacerV(height = 16.dp)
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "No. Transaksi : ${transaksi?.id.orEmpty()}",
                                fontSize = 12.sp
                            )
                            spacerV(height = 8.dp)
                            Text(text = "Status  : ${transaksi?.status}", fontSize = 12.sp)
                            spacerV(height = 8.dp)
                            Text(
                                text = "Nomor Polisi  : ${transaksi?.mobil?.nopol.orEmpty()}",
                                fontSize = 12.sp
                            )
                        }
                        spacerV(height = 16.dp)
                        dividerSmallH()
                        spacerV(height = 16.dp)
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "",
                                    tint = Color.Green
                                )
                                spacerH(width = 8.dp)
                                Text(
                                    text = "Transaksi Berhasi",
                                    fontSize = 12.sp,
                                    color = Color.LightGray
                                )
                            }
                            Text(text = "Pembayaran melalui ${transaksi?.metodePembayaran.orEmpty()}")

                        }
                        spacerV(height = 16.dp)
                        dividerSmallH()
                        spacerV(height = 16.dp)
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Total Pesanan : (${transaksi?.carts.orEmpty().size} Produk)",
                                fontSize = 12.sp
                            )
                            spacerV(height = 8.dp)
                            transaksi?.carts?.forEach {
                                Checkout.ringkasanBiayaItem(
                                    item = it.produk?.nama.orEmpty() + " ${it.jumlah}x" to it.getTotal()
                                        .toRupiahCurrency()
                                )
                                spacerV(height = 4.dp)
                            }
                            spacerV(height = 4.dp)
                            dividerSmallH()
                            spacerV(height = 4.dp)
                            Checkout.ringkasanBiayaItem("Subtotal" to cart.toRupiahCurrency())
                            spacerV(height = 4.dp)
                            if (transaksi?.enablePPn == true) Checkout.ringkasanBiayaItem("PPN" to ppn.toRupiahCurrency())
                            spacerV(height = 4.dp)
                            transaksi?.otherPayment?.forEach {
                                Checkout.ringkasanBiayaItem(
                                    item = it.name.orEmpty()
                                        .capitalizeWords() to it.value.toRupiahCurrency()
                                )
                                spacerV(height = 4.dp)
                            }
                            Checkout.ringkasanBiayaItem(
                                "Total" to total.toRupiahCurrency(),
                                fontWeight = FontWeight.W700,
                                fontSize = 16.sp
                            )

                        }
                        spacerV(height = 16.dp)
                    }
                }
                spacerV(height = 16.dp)
                when (transaksi?.status) {
                    Transaksi.STATUS.Menunggu -> {
                        Button(
                            onClick = {
                                transaksiVM.updateTransaksi(
                                    transaksi?.copy(status = Transaksi.STATUS.Diproses),
                                    isLoading = { isLoading = it })
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Proses")
                        }
                        OutlinedButton(
                            onClick = {
                                transaksiVM.updateTransaksi(
                                    transaksi?.copy(status = Transaksi.STATUS.Dibatalkan),
                                    isLoading = { isLoading = it })
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Batalkan")
                        }
                    }
                    Transaksi.STATUS.Diproses -> {
                        Button(
                            onClick = {
                                transaksiVM.updateTransaksi(
                                    transaksi?.copy(status = Transaksi.STATUS.Selesai),
                                    isLoading = { isLoading = it })
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Selesai")
                        }
                        OutlinedButton(
                            onClick = {
                                transaksiVM.updateTransaksi(
                                    transaksi?.copy(status = Transaksi.STATUS.Dibatalkan),
                                    isLoading = { isLoading = it })
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Batalkan")
                        }
                    }
                    else -> {
                    }
                }
            }
            screenLoading(loading = isLoading)
        }
    }
}

