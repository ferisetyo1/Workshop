package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.component.sheet.TambahStock
import com.feri.workshop.data.model.Produk
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.BackgroundColor
import com.feri.workshop.ui.theme.PrimaryColor
import com.feri.workshop.utils.showToast
import com.feri.workshop.utils.toFormattedString
import com.google.accompanist.flowlayout.FlowRow
import java.util.*

object DetailProduk : Screen {
    override val routeName = "dailproduk"
    override val pageTitle = "Produk"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context= LocalContext.current
        val produkVM = getMainActivity().produkViewModel

        val selectedProduk by remember { produkVM.selectedproduct }

        var nama by remember { mutableStateOf(selectedProduk?.nama.orEmpty()) }
        var errorNama by remember { mutableStateOf("") }

        var harga by remember { mutableStateOf((selectedProduk?.harga ?: 0.0).toInt().toString()) }
        var errorHarga by remember { mutableStateOf("") }

        var diskon by remember {
            mutableStateOf(
                (selectedProduk?.diskon ?: 0.0).toInt().toString()
            )
        }
        var errorDiskon by remember { mutableStateOf("") }

        var kategori by remember { mutableStateOf(selectedProduk?.kategori.orEmpty()) }
        var errorKategori by remember { mutableStateOf("") }

        var tipe by remember { mutableStateOf(selectedProduk?.tipe.orEmpty()) }

        var deskripsi by remember { mutableStateOf(selectedProduk?.deskripsi.orEmpty()) }

        var isLoading by remember { mutableStateOf(false) }
        val keyboard = LocalSoftwareKeyboardController.current
        val listKategory by remember { produkVM.kategoriProduk }

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
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Row {
                    Text(text = "Nama")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = nama,
                    onValueChange = {
                        errorNama = ""
                        nama = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = errorNama.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                if (errorNama.isNotEmpty()) Text(
                    text = errorNama,
                    color = Color.Red,
                    fontSize = 12.sp
                )
                spacerV(height = 16.dp)
                Row {
                    Text(text = "Harga")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = harga,
                    onValueChange = {
                        errorHarga = ""
                        if (it.length < 12) harga = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = errorHarga.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                if (errorHarga.isNotEmpty()) Text(
                    text = errorHarga,
                    color = Color.Red,
                    fontSize = 12.sp
                )
                spacerV(height = 16.dp)
                Row {
                    Text(text = "Diskon")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = diskon,
                    onValueChange = {
                        errorDiskon = ""
                        diskon = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = errorDiskon.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                if (errorDiskon.isNotEmpty()) Text(
                    text = errorDiskon,
                    color = Color.Red,
                    fontSize = 12.sp
                )
                spacerV(height = 16.dp)
                Row {
                    Text(text = "Kategori")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                FlowRow(mainAxisSpacing = 8.dp) {
                    listKategory.map { it.nama.orEmpty() }.forEach {
                        Button(
                            onClick = { kategori = it },
                            colors = if (kategori == it) buttonColors(
                                PrimaryColor.copy(
                                    alpha = 0.75f
                                )
                            )
                            else buttonColors(BackgroundColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = it)
                        }
                    }
                    Button(
                        onClick = { navController.navigate(AddKategoriProduk.routeName) },
                        shape = RoundedCornerShape(8.dp),
                        colors = buttonColors(
                            PrimaryColor.copy(
                                alpha = 0.75f
                            )
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "",
                            tint = Color.White
                        )
                        spacerH(width = 8.dp)
                        Text(text = "Tambah")
                    }
                }
                if (errorKategori.isNotEmpty()) Text(
                    text = errorKategori,
                    color = Color.Red,
                    fontSize = 12.sp
                )
                spacerV(height = 16.dp)
                Text(text = "Barang/Jasa")
                spacerV(height = 8.dp)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = { tipe = Produk.Tipe.BARANG },
                        modifier = Modifier.weight(1f),
                        colors = if (tipe == Produk.Tipe.BARANG) buttonColors(
                            PrimaryColor.copy(
                                alpha = 0.75f
                            )
                        )
                        else buttonColors(BackgroundColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Barang")
                    }
                    spacerH(width = 16.dp)
                    Button(
                        onClick = { tipe = Produk.Tipe.JASA },
                        modifier = Modifier.weight(1f),
                        colors = if (tipe == Produk.Tipe.JASA) buttonColors(
                            PrimaryColor.copy(
                                alpha = 0.75f
                            )
                        )
                        else buttonColors(BackgroundColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Jasa")
                    }
                }
                spacerV(height = 16.dp)
                Text(text = "Deskripsi")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = {
                        deskripsi = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )

                if (selectedProduk?.tipe != tipe ||
                    selectedProduk?.nama != nama ||
                    (selectedProduk?.harga ?: 0.0).toInt().toString() != harga ||
                    (selectedProduk?.diskon ?: 0.0).toInt().toString() != diskon ||
                    selectedProduk?.kategori != kategori ||
                    selectedProduk?.deskripsi != deskripsi
                ) {
                    spacerV(height = 16.dp)
                    Button(
                        onClick = {
                            if (nama.isEmpty()) {
                                errorNama = "Nama tidak boleh kosong."
                                return@Button
                            }
                            if (harga.isEmpty()) {
                                errorHarga = "Harga tidak boleh kosong."
                                return@Button
                            }
                            if (diskon.isEmpty()) {
                                errorDiskon = "Merk tidak boleh kosong."
                                return@Button
                            }
                            if (kategori.isEmpty()) {
                                errorKategori = "Pilih salah satu kategori"
                                return@Button
                            }

                            if ((diskon.toDoubleOrNull()?:0.0)>(harga.toDoubleOrNull()?:0.0)) {
                                errorDiskon = "Diskon tidak boleh lebih besar dari harga"
                                return@Button
                            }

                            selectedProduk?.copy(
                                nama = nama,
                                tipe = tipe,
                                harga = harga.toDoubleOrNull() ?: 0.0,
                                diskon = diskon.toDoubleOrNull() ?: 0.0,
                                kategori = kategori,
                                deskripsi = deskripsi
                            )?.let { it1 ->
                                produkVM.editProduk(
                                    it1,
                                    isLoading = {isLoading=it},
                                    onSuccess = {context.showToast("Berhasil mengubah produk")},
                                    onFailed = {context.showToast("Gagal mengubah produk")}
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Simpan")
                    }
                }
                spacerV(height = 16.dp)
                if (tipe == Produk.Tipe.BARANG && selectedProduk?.tipe == tipe) {
                    Text(text = "Riwayat Stock", fontSize = 16.sp, fontWeight = FontWeight.W700)
                    spacerV(height = 8.dp)
                    selectedProduk?.stocks?.sortedByDescending { it.createdAt }?.forEach { stock ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = Date(
                                        stock.createdAt ?: 0
                                    ).toFormattedString("dd MMM yyyy, HH:mm"),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (stock.jumlah ?: 0 > 0) {
                                        Text(
                                            text = "+ ${stock.jumlah}",
                                            fontSize = 16.sp,
                                            color = Color.Green,
                                            fontWeight = FontWeight.W600
                                        )
                                        Text(text = " stocks telah ditambahkan.")
                                    } else {
                                        Text(
                                            text = "${stock.jumlah}",
                                            fontSize = 16.sp,
                                            color = Color.Red,
                                            fontWeight = FontWeight.W600
                                        )
                                        Text(text = " stocks telah digunakan.")
                                    }
                                }
                                Text(text = stock.deskripsi.orEmpty())
                            }
                            IconButton(onClick = {
                                produkVM.removeStock(stock)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                        }
                        spacerV(height = 8.dp)
                    }
                    spacerV(height = 8.dp)
                    Button(
                        onClick = { navController.navigate(TambahStock.routeName) },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Tambah Stocks")
                    }
                    spacerV(height = 16.dp)
                }
            }
            screenLoading(loading = isLoading)
        }
    }
}