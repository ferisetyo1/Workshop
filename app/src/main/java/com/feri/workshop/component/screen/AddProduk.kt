package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.data.model.Produk
import com.feri.workshop.data.model.Stock
import com.feri.workshop.ui.helper.focusModifier
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.BackgroundColor
import com.feri.workshop.ui.theme.PrimaryColor
import com.feri.workshop.utils.showToast
import com.google.accompanist.flowlayout.FlowRow
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object AddProduk : Screen {
    override val routeName = "addproduk"
    override val pageTitle = "Tambah Produk"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val activity = context as MainActivity
        val produkVM = activity.produkViewModel

        var nama by remember { mutableStateOf("") }
        var errorNama by remember { mutableStateOf("") }

        var hargapokok by remember { mutableStateOf(0) }
        var errorHargaPokok by remember { mutableStateOf("") }
        var harga by remember { mutableStateOf(0) }
        var errorHarga by remember { mutableStateOf("") }

        var stock by remember { mutableStateOf("") }
        var errorStock by remember { mutableStateOf("") }

        var kategori by remember { mutableStateOf<ArrayList<Produk.Kategori>>(arrayListOf()) }
        var errorKategori by remember { mutableStateOf("") }

        var tipe by remember { mutableStateOf(Produk.Tipe.BARANG) }

        var deskripsi by remember { mutableStateOf("") }

        var isLoading by remember { mutableStateOf(false) }

        val listKategory by remember { produkVM.kategoriProduk }

        val keyboard = LocalSoftwareKeyboardController.current
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
                        nama = it.uppercase()
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = errorNama.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
                if (errorNama.isNotEmpty()) Text(
                    text = errorNama,
                    color = Color.Red,
                    fontSize = 12.sp
                )
                spacerV(height = 16.dp)
                Row {
                    Text(text = "Harga Pokok")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = hargapokok.toString(),
                    onValueChange = {
                        errorHargaPokok = ""
                        if (it.isDigitsOnly()) hargapokok = it.toIntOrNull()?:0
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = errorHargaPokok.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                if (errorHargaPokok.isNotEmpty()) Text(
                    text = errorHargaPokok,
                    color = Color.Red,
                    fontSize = 12.sp
                )
                spacerV(height = 16.dp)
                Row {
                    Text(text = "Harga Jual")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = harga.toString(),
                    onValueChange = {
                        errorHarga = ""
                        if (it.isDigitsOnly()) harga = it.toIntOrNull()?:0
                    },
                    modifier = focusModifier()
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
                    Text(text = "Kategori")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                FlowRow(mainAxisSpacing = 8.dp) {
                    listKategory.forEach { kategor ->
                        val index = kategori.indexOfFirst { it.id == kategor.id }
                        Button(
                            onClick = {
                                kategori = ArrayList(kategori).apply {
                                    if (index == -1) {
                                        add(Produk.Kategori(id=kategor.id,harga = 0.0, nama = kategor.nama))
                                    } else {
                                        removeAt(index)
                                    }
                                }
                            },
                            colors = if (index != -1) buttonColors(
                                PrimaryColor.copy(
                                    alpha = 0.75f
                                )
                            )
                            else buttonColors(BackgroundColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = kategor.nama.orEmpty())
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
                kategori.forEachIndexed { index, data ->
                    Text(text = "Harga untuk kategori ${data.nama}")
                    spacerV(height = 8.dp)
                    OutlinedTextField(
                        value = data.harga?.toInt().toString(),
                        onValueChange = {
                            if (it.isDigitsOnly()) {
                                kategori = ArrayList(kategori).apply {
                                    set(index, data.copy(harga = it.toDoubleOrNull()?:0.0))
                                }
                            }
                        },
                        modifier = focusModifier()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    spacerV(height = 16.dp)
                }
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
                if (tipe == Produk.Tipe.BARANG) {
                    spacerV(height = 16.dp)
                    Row {
                        Text(text = "Stock")
                        Text(text = "*", color = Color.Red)
                    }
                    spacerV(height = 8.dp)
                    OutlinedTextField(
                        value = stock,
                        onValueChange = {
                            errorStock = ""
                            stock = it
                        },
                        modifier = focusModifier()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        isError = errorStock.isNotEmpty(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    if (errorStock.isNotEmpty()) Text(
                        text = errorStock,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                spacerV(height = 16.dp)
                Text(text = "Deskripsi")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = {
                        deskripsi = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                spacerV(height = 16.dp)
                Button(
                    onClick = {
                        if (nama.isEmpty()) {
                            errorNama = "Nama tidak boleh kosong."
                            return@Button
                        }
                        if (stock.isEmpty() && tipe == Produk.Tipe.BARANG) {
                            errorStock = "Stock tidak boleh kosong."
                            return@Button
                        }

                        produkVM.addProduk(
                            produk = Produk(
                                nama = nama,
                                harga = harga.toDouble(),
                                hargaPokok= hargapokok.toDouble(),
                                kategori = kategori,
                                stocks = listOf(Stock(jumlah = stock.toLongOrNull() ?: 0)),
                                deskripsi = deskripsi,
                                tipe = tipe,
                                createdBy = Firebase.auth.currentUser?.email
                            ),
                            isLoading = { isLoading = it },
                            onSuccess = {
                                navController.navigateUp()
                                context.showToast("Berhasil menambahkan produk")
                            }, onFailed = {
                                context.showToast(it)
                            }
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Simpan")
                }
                spacerV(height = 16.dp)
            }
            screenLoading(loading = isLoading)
        }
    }
}