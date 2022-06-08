package com.feri.workshop.component.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.R
import com.feri.workshop.data.model.Transaksi
import com.feri.workshop.ui.helper.*
import com.feri.workshop.utils.capitalizeWords
import com.feri.workshop.utils.removedat
import com.feri.workshop.utils.toFormattedString
import com.feri.workshop.utils.toRupiahCurrency
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.ArrayList

object InfoTransaksi : Screen {
    override val routeName = "InfoTransaksi"
    override val pageTitle = "Transaksi"

    @Composable
    override fun screen(navController: NavHostController) {
        val transaksiVM = getMainActivity().transaksiViewModel
        val transaksi by remember { transaksiVM.selectedTransaksi }
        var isLoading by remember { mutableStateOf(false) }
        var tambah by remember { mutableStateOf(false) }
        val cart = transaksi?.carts.orEmpty().sumOf { it.getTotal() }
        val other = transaksi?.otherPayment.orEmpty().sumOf { it.value ?: 0.0 }
        val ppn =
            if (transaksi?.enablePPn == true) (cart * (transaksi?.ppnValue ?: 0)) / 100 else 0.0
        val total = cart + other + ppn
        var bayar by remember { mutableStateOf("") }
        val context= LocalContext.current
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
                            spacerV(height = 8.dp)
                            Text(
                                text = "KM Tercatat  : ${transaksi?.mobil?.newKM} KM",
                                fontSize = 12.sp
                            )
                            if (transaksi?.createdBy != null) {
                                spacerV(height = 8.dp)
                                Text(
                                    text = "Dibuat oleh  : ${transaksi?.createdBy.removedat()}",
                                    fontSize = 12.sp
                                )
                            }
                            if (transaksi?.procededBy != null) {
                                spacerV(height = 8.dp)
                                Text(
                                    text = "Diproses oleh  : ${transaksi?.procededBy.removedat()}",
                                    fontSize = 12.sp
                                )
                            }
                            if (transaksi?.canceledBy != null) {
                                spacerV(height = 8.dp)
                                Text(
                                    text = "Dibatalkan oleh  : ${transaksi?.canceledBy.removedat()}",
                                    fontSize = 12.sp
                                )
                            }
                            if (transaksi?.finishedBy != null) {
                                spacerV(height = 8.dp)
                                Text(
                                    text = "Diselesaikan oleh  : ${transaksi?.finishedBy.removedat()}",
                                    fontSize = 12.sp
                                )
                            }
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
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    Text(text = "Pembayaran ${transaksi?.metodePembayaran.orEmpty()}")
                                    if (transaksi?.paidValue == transaksi?.getTotal()) Text(text = " (Lunas)")
                                }
                                if (transaksi?.paidValue != transaksi?.getTotal()) Image(
                                    painter = painterResource(
                                        id = R.drawable.ic_wa
                                    ),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(25.dp)
                                        .clickable {
                                            var number = transaksi?.customer?.notelp.orEmpty()
                                            val firstchar = number[0]
                                            if (firstchar.toString() == "0") {
                                                number = "+62" + number.substring(1)
                                            }
                                            val text =
                                                "Selamat siang, Yth. Bapak/Ibu/Saudara/i ${transaksi?.customer?.nama?.capitalizeWords()}\n\nAnda memiliki tagihan sebesar ${((transaksi?.getTotal() ?: 0.0) - (transaksi?.paidValue ?: 0.0)).toRupiahCurrency()} dengan No. Transaksi ${transaksi?.id} yang pada ${
                                                    Date(transaksi?.createdAt ?: 0).toFormattedString(
                                                        "dd MMM yyyy, HH:mm"
                                                    )
                                                }\n\nTerima Kasih"
                                            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                                                setData(
                                                    Uri.parse(
                                                        "https://api.whatsapp.com/send?phone=${
                                                            URLEncoder.encode(
                                                                number,
                                                                StandardCharsets.UTF_8.toString()
                                                            )
                                                        }&text=${
                                                            URLEncoder.encode(
                                                                text,
                                                                StandardCharsets.UTF_8.toString()
                                                            )
                                                        }"
                                                    )
                                                )
                                            })
                                        })
                            }
                            if (transaksi?.metodePembayaran == "Sebagian") {
                                transaksi?.updateInfo?.forEach {
                                    Text(text = it, fontSize = 12.sp, color = Color.LightGray)
                                }
                                if (!tambah && transaksi?.paidValue != transaksi?.getTotal()) Button(
                                    onClick = { tambah = true }) {
                                    Text(text = "Tambah")
                                }
                                if (tambah && transaksi?.paidValue != transaksi?.getTotal()) {
                                    OutlinedTextField(
                                        value = bayar,
                                        onValueChange = {
                                            bayar = it
                                            if (bayar.toIntOrNull() != null) {
                                                val number = it.toIntOrNull() ?: 0
                                                val paidValue = transaksi?.paidValue ?: 0.0
                                                val total = transaksi?.getTotal() ?: 0.0
                                                if ((total - paidValue) < number) {
                                                    bayar = (total - paidValue).toInt().toString()
                                                }
                                            }
                                        },
                                        modifier = focusModifier()
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        keyboardOptions = KeyboardOptions(
                                            imeAction = ImeAction.Next,
                                            keyboardType = KeyboardType.Number
                                        )
                                    )
                                    Button(onClick = {
                                        val updateinfo = ArrayList(transaksi?.updateInfo.orEmpty())
                                        updateinfo.add(
                                            Date().toFormattedString("yyyy/MM/dd") + " " + bayar.toDoubleOrNull()
                                                .toRupiahCurrency() + " (" + Firebase.auth.currentUser?.email + ")"
                                        )
                                        transaksiVM.updateTransaksi(
                                            transaksi = transaksi?.copy(
                                                updateInfo = updateinfo,
                                                paidValue = ((transaksi?.paidValue
                                                    ?: 0.0) + (bayar.toDoubleOrNull() ?: 0.0))
                                            ), isLoading = { isLoading = it })
                                    }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                        Text(text = "Simpan")
                                    }
                                }
                            }
                        }
                        spacerV(height = 16.dp)
                        dividerSmallH()
                        spacerV(height = 16.dp)
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)) {
                            Text(text = "Keterangan",fontWeight = FontWeight.W600)
                            spacerV(height = 8.dp)
                            Text(text = transaksi?.keterangan.let { return@let if (it.isNullOrEmpty()) "-" else it })
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
                                    item = it.produk?.nama.orEmpty() +" (${it.kategori?.nama.orEmpty()})"+ " ${it.jumlah}x" to it.getTotal()
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
                            if (transaksi?.enableDiskon == true) Checkout.ringkasanBiayaItem("Diskon" to transaksi?.diskonValue.toRupiahCurrency())
                            spacerV(height = 4.dp)
                            transaksi?.otherPayment?.forEach {
                                Checkout.ringkasanBiayaItem(
                                    item = it.name.orEmpty()
                                        .capitalizeWords() to it.value.toRupiahCurrency()
                                )
                                spacerV(height = 4.dp)
                            }
                            Checkout.ringkasanBiayaItem(
                                "Total" to transaksi?.getTotal().toRupiahCurrency(),
                                fontWeight = FontWeight.W700,
                                fontSize = 16.sp
                            )
                            Checkout.ringkasanBiayaItem(
                                "DIbayarkan" to transaksi?.paidValue.toRupiahCurrency(),
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
                                    transaksi?.copy(
                                        status = Transaksi.STATUS.Diproses,
                                        procededBy = Firebase.auth.currentUser?.email
                                    ),
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
                                    transaksi?.copy(
                                        status = Transaksi.STATUS.Dibatalkan,
                                        canceledBy = Firebase.auth.currentUser?.email
                                    ),
                                    isLoading = { isLoading = it }, onSuccess = { bayar = "" })
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
                                    transaksi?.copy(
                                        status = Transaksi.STATUS.Selesai,
                                        finishedBy = Firebase.auth.currentUser?.email
                                    ),
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
                                    transaksi?.copy(
                                        status = Transaksi.STATUS.Dibatalkan,
                                        canceledBy = Firebase.auth.currentUser?.email
                                    ),
                                    isLoading = { isLoading = it })
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Batalkan")
                        }
                    }
                    Transaksi.STATUS.Selesai->{
                        OutlinedButton(
                            onClick = {
                                transaksiVM.updateTransaksi(
                                    transaksi?.copy(
                                        status = Transaksi.STATUS.Dibatalkan,
                                        canceledBy = Firebase.auth.currentUser?.email
                                    ),
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

