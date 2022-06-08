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
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.feri.workshop.component.sheet.TambahStock
import com.feri.workshop.data.model.Produk
import com.feri.workshop.ui.helper.focusModifier
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.BackgroundColor
import com.feri.workshop.ui.theme.PrimaryColor
import com.feri.workshop.utils.removedat
import com.feri.workshop.utils.showToast
import com.feri.workshop.utils.toFormattedString
import com.google.accompanist.flowlayout.FlowRow
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

object DetailProduk : Screen {
    override val routeName = "dailproduk"
    override val pageTitle = "Produk"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val produkVM = getMainActivity().produkViewModel

        val selectedProduk by remember { produkVM.selectedproduct }

        var nama by remember { mutableStateOf(selectedProduk?.nama.orEmpty()) }
        var errorNama by remember { mutableStateOf("") }

        var hargaPokok by remember { mutableStateOf((selectedProduk?.hargaPokok ?: 0.0).toInt()) }
        var errorHargaPokok by remember { mutableStateOf("") }
        var harga by remember { mutableStateOf((selectedProduk?.harga ?: 0.0).toInt()) }
        var errorHarga by remember { mutableStateOf("") }

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
                    if (selectedProduk?.deletedBy == null) IconButton(onClick = {
                        selectedProduk?.let {
                            produkVM.removeProduck(
                                selectedProduk = it,
                                onSuccess = { navController.navigateUp() })
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                    else IconButton(onClick = {
                        selectedProduk?.let {
                            produkVM.editProduk(
                                produk = it.copy(deletedBy = null, deletedAt = Date().time),
                                onSuccess = { navController.navigateUp() })
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
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
                spacerV(height = 8.dp)
                selectedProduk?.let { produk ->
                    produk.createdBy?.let {
                        spacerV(height = 8.dp)
                        Text(
                            text = "Dibuat oleh : ${it.removedat()} (${
                                Date(produk.createdAt ?: 0).toFormattedString(
                                    "dd/MM/yyyy HH:mm"
                                )
                            })", fontSize = 12.sp
                        )
                    }
                    produk.modifiedBy?.let {
                        spacerV(height = 8.dp)
                        Text(
                            text = "Terakhir diubah oleh : ${it.removedat()} (${
                                Date(produk.modifiedAt ?: 0).toFormattedString(
                                    "dd/MM/yyyy HH:mm"
                                )
                            })", fontSize = 12.sp
                        )
                    }
                    produk.deletedBy?.let {
                        spacerV(height = 8.dp)
                        Text(
                            text = "Dihapus oleh : ${it.removedat()} (${
                                Date(produk.deletedAt ?: 0).toFormattedString(
                                    "dd/MM/yyyy HH:mm"
                                )
                            })", fontSize = 12.sp
                        )
                    }
                }
                spacerV(height = 8.dp)
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
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
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
                    value = hargaPokok.toString(),
                    onValueChange = {
                        errorHarga = ""
                        if (it.isDigitsOnly()) hargaPokok = it.toIntOrNull() ?: 0
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
                        if (it.isDigitsOnly()) harga = it.toIntOrNull() ?: 0
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
                    listKategory.forEach { kname ->
                        val index = kategori.indexOfFirst { it.id == kname.id }
                        Button(
                            onClick = {
                                kategori = ArrayList(kategori).apply {
                                    if (index == -1) {
                                        add(
                                            Produk.Kategori(
                                                id = kname.id,
                                                harga = 0.0,
                                                nama = kname.nama
                                            )
                                        )
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
                            Text(text = kname.nama.orEmpty())
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
                                    set(index, data.copy(harga = it.toDoubleOrNull() ?: 0.0))
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

                if (selectedProduk?.tipe != tipe ||
                    selectedProduk?.nama != nama ||
                    (selectedProduk?.hargaPokok ?: 0.0).toInt() != hargaPokok ||
                    (selectedProduk?.harga ?: 0.0).toInt() != harga ||
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

                            selectedProduk?.copy(
                                nama = nama,
                                tipe = tipe,
                                hargaPokok = hargaPokok.toDouble(),
                                harga = harga.toDouble(),
                                kategori = kategori,
                                deskripsi = deskripsi,
                                modifiedBy = Firebase.auth.currentUser?.email
                            )?.let { it1 ->
                                produkVM.editProduk(
                                    it1,
                                    isLoading = { isLoading = it },
                                    onSuccess = { context.showToast("Berhasil mengubah produk") },
                                    onFailed = { context.showToast("Gagal mengubah produk") }
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
                            if (stock.isJual == false) IconButton(onClick = {
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