package com.feri.workshop.component.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.component.sheet.BiayaLainnya
import com.feri.workshop.component.sheet.MetodePembayaran
import com.feri.workshop.data.model.Produk
import com.feri.workshop.ui.helper.dividerSmallH
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.PrimaryColor
import com.feri.workshop.utils.capitalizeWords
import com.feri.workshop.utils.showToast
import com.feri.workshop.utils.toFormattedString
import com.feri.workshop.utils.toRupiahCurrency
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_TEXT
import java.util.*

object Checkout : Screen {
    override val routeName = "checkout"
    override val pageTitle = "Checkout"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val customerVM = getMainActivity().customerViewModel
        val createTransaksiViewModel = getMainActivity().transaksiViewModel
        val oldKM by remember { createTransaksiViewModel.oldKM }
        val newKM by remember { createTransaksiViewModel.newKM }
        val selectedproduk by remember { createTransaksiViewModel.selectedProduk }
        val keterangan by remember { createTransaksiViewModel.keterangan }
        val metodePembayaran by remember { createTransaksiViewModel.selectedMetodePembayaran }
        val otherpayment by remember { createTransaksiViewModel.othersPayment }
        val enablePPn by remember { createTransaksiViewModel.enablePPn }
        val enableDiskon by remember { createTransaksiViewModel.enableDiskon }
        val ppnValue by remember { createTransaksiViewModel.ppnValue }
        val diskon by remember { createTransaksiViewModel.diskonValue }
        val keyboard = LocalSoftwareKeyboardController.current
        val subtotal = selectedproduk.map { it.getTotal() }.sum()
        val subOthersPayment = otherpayment.map { it.value ?: 0.0 }.sum()
        val ppn = if (enablePPn) ((subtotal + subOthersPayment) * ppnValue) / 100 else 0.0
        val total = subtotal + subOthersPayment + ppn - diskon
        val listmethod = listOf(
            "Tunai - Cash",
            "Tunai - BCA",
            "Tunai - Mandiri"
        )
        LaunchedEffect(key1 = total) {
            if (listmethod.contains(metodePembayaran)) createTransaksiViewModel.paidValue.value =
                total.toInt()
        }
        var isLoading by remember { mutableStateOf(false) }
        val timeMilis by remember { createTransaksiViewModel.createAt }
        var mCalendar = Calendar.getInstance()
        mCalendar.time = Date(timeMilis)
        val mHour = mCalendar[Calendar.HOUR_OF_DAY]
        val mMinute = mCalendar[Calendar.MINUTE]
        val mDay = mCalendar[Calendar.DAY_OF_MONTH]
        val mMonth = mCalendar[Calendar.MONTH]
        val mYear = mCalendar[Calendar.YEAR]

        val timePicker = TimePickerDialog(context, { _, hour, minute ->
            mCalendar.set(Calendar.HOUR_OF_DAY, hour)
            mCalendar.set(Calendar.MINUTE, minute)
            createTransaksiViewModel.createAt.value = mCalendar.timeInMillis
        }, mHour, mMinute, true)
        val datePicker = DatePickerDialog(context, { _, year, month, dayOfmonth ->
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfmonth)
            mCalendar.set(Calendar.MONTH, month)
            mCalendar.set(Calendar.YEAR, year)
            createTransaksiViewModel.createAt.value = mCalendar.timeInMillis
        }, mYear, mMonth, mDay)
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
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "",
                        modifier = Modifier.size(12.dp)
                    )
                    spacerH(width = 8.dp)
                    Text(
                        text = "Pastikan pesanan sudah sesuai dengan yang pelanggan inginkan",
                        fontSize = 12.sp
                    )
                }
                spacerV(height = 16.dp)
                Text(text = "Total Pesanan (${selectedproduk.size}):")
                spacerV(height = 8.dp)
                selectedproduk.forEach {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (it.produk?.tipe == Produk.Tipe.BARANG) Icon(
                                painterResource(id = com.feri.workshop.R.drawable.ic_box),
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
                                    text = it.produk?.nama.orEmpty().capitalizeWords(),
                                    fontWeight = FontWeight.W600
                                )
                                Text(
                                    text = it.kategori?.nama.orEmpty()
                                )
                                Text(
                                    text = (it.kategori?.harga ?: (it.produk?.harga
                                        ?: 0.0)).toRupiahCurrency()
                                )
                                if (it.produk?.tipe == Produk.Tipe.BARANG) Text(
                                    text = "Qty ${it.produk.getJumlahStock()}"
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (it.produk?.tipe == Produk.Tipe.BARANG) {
                                IconButton(onClick = {
                                    it.kategori?.let { it1 ->
                                        createTransaksiViewModel.addProduk(
                                            jumlah = it.jumlah!! - 1,
                                            produk = it.produk,
                                            kategori = it1
                                        )
                                    }
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
                                            it.kategori?.let { it1 ->
                                                createTransaksiViewModel.addProduk(
                                                    jumlah = text.toIntOrNull() ?: 0,
                                                    produk = it.produk,
                                                    kategori = it1
                                                )
                                            }
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
                                    it.kategori?.let { it1 ->
                                        createTransaksiViewModel.addProduk(
                                            jumlah = it.jumlah!! + 1,
                                            produk = it.produk,
                                            kategori = it1
                                        )
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.AddCircleOutline,
                                        contentDescription = ""
                                    )
                                }
                            } else {
                                Text(text = "1x")
                                IconButton(onClick = {
                                    it.kategori?.let { it1 ->
                                        createTransaksiViewModel.addProduk(
                                            jumlah = 0,
                                            produk = it.produk!!,
                                            kategori = it1
                                        )
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.RemoveShoppingCart,
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                    }
                    spacerV(height = 16.dp)
                }
                spacerV(height = 16.dp)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Tanggal Transaksi",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        spacerV(height = 8.dp)
                        Row {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        datePicker.show()
                                    }) {
                                Text(text = Date(timeMilis).toFormattedString("dd MMM yyyy"))
                                spacerH(8.dp)
                                Icon(imageVector = Icons.Default.DateRange, contentDescription = "")
                            }
                            spacerH(16.dp)
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        timePicker.show()
                                    }) {
                                Text(text = Date(timeMilis).toFormattedString("HH:mm"))
                                spacerH(8.dp)
                                Icon(imageVector = Icons.Default.Timer, contentDescription = "")
                            }
                        }
                        spacerV(height = 8.dp)
                    }
                }
                spacerV(height = 16.dp)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "KM Mobil",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        spacerV(height = 8.dp)
                        Text(
                            text = "Riwayat KM tercatat : ${oldKM} KM",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
                        )
                        spacerV(height = 8.dp)
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (newKM == 0) Text(
                                text = "isi KM sekarang disini",
                                style = TextStyle(
                                    color = Color.DarkGray,
                                    fontStyle = FontStyle.Italic
                                )
                            )
                            BasicTextField(
                                value = if (newKM == 0) "" else newKM.toString(),
                                onValueChange = { text ->
                                    createTransaksiViewModel.newKM.value = text.toIntOrNull() ?: 0
                                },
                                textStyle = TextStyle(
                                    color = Color.White,
                                    textAlign = TextAlign.Start
                                ),
                                cursorBrush = SolidColor(value = Color.White),
                                modifier = Modifier.fillMaxWidth(),
                                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                            )
                        }
                    }
                }
                spacerV(height = 16.dp)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Keterangan/Keluhan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        spacerV(height = 8.dp)
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (keterangan.isEmpty()) Text(
                                text = "isi keterangan disini",
                                style = TextStyle(
                                    color = Color.DarkGray,
                                    fontStyle = FontStyle.Italic
                                )
                            )
                            BasicTextField(
                                value = keterangan,
                                onValueChange = { text ->
                                    createTransaksiViewModel.keterangan.value = text
                                },
                                textStyle = TextStyle(
                                    color = Color.White,
                                    textAlign = TextAlign.Start
                                ),
                                cursorBrush = SolidColor(value = Color.White),
                                modifier = Modifier.fillMaxWidth(),
                                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                            )
                        }
                    }
                }
                spacerV(height = 16.dp)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Metode Pembayaran",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        spacerV(height = 8.dp)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Pilih Metode Pembayaran", fontSize = 12.sp)
                                spacerV(height = 12.dp)
                                if (metodePembayaran.isNotEmpty()) Text(text = metodePembayaran)
                            }
                            IconButton(onClick = {
                                navController.navigate(MetodePembayaran.routeName)
                                keyboard?.hide()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = ""
                                )
                            }
                        }

                    }
                }
                spacerV(height = 16.dp)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Biaya Lainnya",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600
                                )
                                spacerV(height = 8.dp)
                                Text(text = "Tambah biaya lainnya", fontSize = 12.sp)
                            }
                            IconButton(onClick = { navController.navigate(BiayaLainnya.routeName) }) {
                                Icon(
                                    imageVector = Icons.Default.AddCircleOutline,
                                    contentDescription = ""
                                )
                            }
                        }
                        spacerV(height = 8.dp)
                        otherpayment.forEachIndexed { index, pair ->
                            spacerV(height = 4.dp)
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(Modifier.weight(1f)) {
                                    ringkasanBiayaItem(item = pair.name.orEmpty() to pair.value.toRupiahCurrency())
                                }
                                spacerH(width = 16.dp)
                                IconButton(onClick = {
                                    createTransaksiViewModel.removeBiayaLainnya(
                                        index
                                    )
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                    }
                }
                spacerV(height = 16.dp)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Diskon",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        spacerV(height = 8.dp)
                        Text(text = "Nilai Diskon (Rp)", fontSize = 12.sp)
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Rp. ")
                                BasicTextField(
                                    value = diskon.toInt().toString(),
                                    onValueChange = { text ->
                                        if ((text.toDoubleOrNull() ?: 0.0) < subtotal)
                                            createTransaksiViewModel.diskonValue.value =
                                                text.toDoubleOrNull() ?: 0.0
                                        else createTransaksiViewModel.diskonValue.value =
                                            subtotal - 1
                                    },
                                    textStyle = TextStyle(
                                        color = Color.White,
                                        textAlign = TextAlign.Start
                                    ),
                                    cursorBrush = SolidColor(value = Color.White),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                            Switch(
                                checked = enableDiskon,
                                onCheckedChange = {
                                    createTransaksiViewModel.enableDiskon.value = it
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = PrimaryColor,
                                    uncheckedThumbColor = Color.White,
                                    checkedTrackColor = PrimaryColor,
                                    uncheckedTrackColor = Color.White
                                )
                            )
                        }
                    }
                }
                spacerV(height = 16.dp)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "PPN",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        spacerV(height = 8.dp)
                        Text(text = "Nilai PPN (%)", fontSize = 12.sp)
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    if (ppnValue > 1)
                                        createTransaksiViewModel.ppnValue.value = ppnValue - 1
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.RemoveCircleOutline,
                                        contentDescription = ""
                                    )
                                }
                                BasicTextField(
                                    value = ppnValue.toString(),
                                    onValueChange = { text ->
                                        if (ppnValue in 2..99)
                                            createTransaksiViewModel.ppnValue.value =
                                                text.toIntOrNull() ?: 0
                                    },
                                    textStyle = TextStyle(
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    ),
                                    cursorBrush = SolidColor(value = Color.White),
                                    modifier = Modifier.width(50.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                IconButton(onClick = {
                                    if (ppnValue < 100)
                                        createTransaksiViewModel.ppnValue.value = ppnValue + 1
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.AddCircleOutline,
                                        contentDescription = ""
                                    )
                                }
                            }
                            Switch(
                                checked = enablePPn,
                                onCheckedChange = { createTransaksiViewModel.enablePPn.value = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = PrimaryColor,
                                    uncheckedThumbColor = Color.White,
                                    checkedTrackColor = PrimaryColor,
                                    uncheckedTrackColor = Color.White
                                )
                            )
                        }
                    }
                }
                spacerV(height = 16.dp)
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Ringkasan Biaya",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        spacerV(height = 8.dp)
                        selectedproduk.forEach {
                            ringkasanBiayaItem(
                                item = "${it.produk?.nama?.capitalizeWords()} (${it.kategori?.nama.orEmpty()}) ${it.jumlah}x" to it.getTotal()
                                    .toRupiahCurrency()
                            )
                            spacerV(height = 4.dp)
                        }
                        dividerSmallH()
                        spacerV(height = 4.dp)
                        ringkasanBiayaItem(item = "Subtotal" to subtotal.toRupiahCurrency())
                        spacerV(height = 4.dp)
                        otherpayment.forEach {
                            ringkasanBiayaItem(item = it.name.orEmpty() to it.value.toRupiahCurrency())
                            spacerV(height = 4.dp)
                        }
                        if (enableDiskon) {
                            ringkasanBiayaItem(item = "Diskon" to diskon.toRupiahCurrency())
                            spacerV(height = 4.dp)
                        }
                        if (enablePPn) {
                            ringkasanBiayaItem(item = "PPN ($ppnValue%)" to ppn.toRupiahCurrency())
                            spacerV(height = 4.dp)
                        }
                        dividerSmallH()
                        spacerV(height = 4.dp)
                        ringkasanBiayaItem(item = "Total" to total.toRupiahCurrency())
                        ringkasanBiayaItem(
                            item = "Dibayarkan" to createTransaksiViewModel.paidValue.value.toDouble()
                                .toRupiahCurrency()
                        )
                    }
                }
                spacerV(height = 16.dp)
                Button(
                    onClick = {
                        if (total < 0) {
                            context.showToast("Total terlalu kecil, periksa lagi biaya yang anda inputkan!")
                            return@Button
                        }
                        if (metodePembayaran.isEmpty()) {
                            context.showToast("Metode pembayaran masih kosong")
                            return@Button
                        }
                        createTransaksiViewModel.createTransaksi(
                            isLoading = { isLoading = it },
                            onSuccess = {
                                customerVM.getMobil(customerid = createTransaksiViewModel.selectedCustomer.value?.id)
                                navController.popBackStack(routeName, true, false)
                                navController.popBackStack(ListProduk.routeName, true, false)
                                navController.navigate(InfoTransaksi.routeName)
                            },
                            onFailed = { context.showToast(it) })
                    }, modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Checkout")
                }
                spacerV(height = 16.dp)
            }
            screenLoading(loading = isLoading)
        }
    }

    @Composable
    fun ringkasanBiayaItem(
        item: Pair<String, String>,
        fontWeight: FontWeight = FontWeight.Normal,
        fontSize: TextUnit = 14.sp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = item.first,
                modifier = Modifier.weight(1f),
                fontWeight = fontWeight,
                fontSize = fontSize
            )
            Text(
                text = item.second,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontWeight = fontWeight,
                fontSize = fontSize
            )
        }
    }
}