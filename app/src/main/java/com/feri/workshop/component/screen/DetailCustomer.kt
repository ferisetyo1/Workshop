package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.repository.model.Mobil
import com.feri.workshop.ui.helper.dividerSmallH
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.capitalizeWords
import com.feri.workshop.utils.showToast
import com.feri.workshop.utils.toFormattedString
import java.util.*

object DetailCustomer : Screen {
    override val name = "Detail Customer"

    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val activity = context as MainActivity
        val customerVM = activity.customerViewModel
        val customer by remember { customerVM.selectedCustomer }
        var mobils by remember { mutableStateOf<List<Mobil>>(emptyList()) }
        LaunchedEffect(key1 = customer) {
            customerVM.getMobil(
                customerid = customer?.id,
                onSuccess = { mobils = it },
                onFailed = { context.showToast(it) })
        }
        var namapelanggan by remember { mutableStateOf(customer?.nama.orEmpty().capitalizeWords()) }
        var errorNamaPelanggan by remember { mutableStateOf("") }

        var nomortelfon by remember { mutableStateOf(customer?.notelp.orEmpty()) }
        var errorPhoneNumber by remember { mutableStateOf("") }

        var alamat by remember { mutableStateOf(customer?.alamat.orEmpty()) }
        var errorAlamat by remember { mutableStateOf("") }
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = name)
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
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    Column(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        spacerV(height = 16.dp)
                        Text(text = "Informasi", fontSize = 16.sp, fontWeight = FontWeight.W700)
                        spacerV(height = 8.dp)
                        Text(
                            text = "Data ini dibuat oleh ${
                                customer?.createdBy.orEmpty().capitalizeWords()
                            } pada tanggal " + Date(
                                customer?.createdAt ?: 0
                            ).toFormattedString("dd MMMM yyyy, HH:mm") + " WIB"
                        )
                        spacerV(height = 16.dp)
                        Text(text = "Data Pribadi", fontSize = 16.sp, fontWeight = FontWeight.W700)
                        spacerV(height = 16.dp)
                        Row {
                            Text(text = "Nama")
                            Text(text = "*", color = Color.Red)
                        }
                        spacerV(height = 8.dp)
                        OutlinedTextField(
                            value = namapelanggan,
                            onValueChange = {
                                errorNamaPelanggan = ""
                                namapelanggan = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            isError = errorNamaPelanggan.isNotEmpty(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        if (errorNamaPelanggan.isNotEmpty()) Text(
                            text = errorNamaPelanggan,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                        spacerV(height = 16.dp)
                        Row {
                            Text(text = "Nomor Telepon")
                            Text(text = "*", color = Color.Red)
                        }
                        spacerV(height = 8.dp)
                        OutlinedTextField(
                            value = nomortelfon,
                            onValueChange = {
                                errorPhoneNumber = ""
                                nomortelfon = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            isError = errorPhoneNumber.isNotEmpty(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                                keyboardType = KeyboardType.Number
                            )
                        )
                        if (errorPhoneNumber.isNotEmpty()) Text(
                            text = errorPhoneNumber,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                        spacerV(height = 16.dp)
                        Row {
                            Text(text = "Alamat")
                            Text(text = "*", color = Color.Red)
                        }
                        spacerV(height = 8.dp)
                        OutlinedTextField(
                            value = alamat,
                            onValueChange = {
                                errorAlamat = ""
                                alamat = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            isError = errorAlamat.isNotEmpty(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                        if (errorAlamat.isNotEmpty()) Text(
                            text = errorAlamat,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                        if (customer?.nama?.lowercase() != namapelanggan.lowercase() || customer?.alamat?.lowercase() != alamat.lowercase() || customer?.notelp?.lowercase() != nomortelfon.lowercase()) {
                            spacerV(height = 16.dp)
                            Button(
                                onClick = { /*TODO*/ },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = "Simpan Perubahan")
                            }
                        }
                        spacerV(height = 16.dp)
                        Text(text = "List Mobil", fontSize = 16.sp, fontWeight = FontWeight.W700)
                        spacerV(height = 16.dp)
                    }
                }

                itemsIndexed(mobils) { _, item ->
                    itemMobil(item)
                    spacerV(height = 16.dp)
                }
                item {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (mobils.size < 5) {
                            Button(onClick = { /*TODO*/ }, shape = RoundedCornerShape(8.dp)) {
                                Text(text = "Tambah Mobil")
                            }
                        }
                    }
                    spacerV(height = 16.dp)
                }
            }
        }
    }

    @Composable
    fun itemMobil(item: Mobil) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.WatchLater,
                            contentDescription = "",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                        spacerH(width = 4.dp)
                        Text(
                            text = Date(
                                item.createdAt ?: 0
                            ).toFormattedString("dd MMMM yyyy, HH:mm"),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    spacerV(height = 8.dp)
                    Text(text = "Mobil", fontSize = 12.sp)
                    spacerV(height = 8.dp)
                    Text(
                        text = item.merk.orEmpty().lowercase().capitalizeWords()+" • "+ item.nopol.orEmpty().lowercase().capitalizeWords()+" • "+item.tahun.orEmpty().lowercase().capitalizeWords(),
                        fontSize = 16.sp, fontWeight = FontWeight.W600
                    )
                    spacerV(height = 12.dp)
                    Text(text = "Warna", fontSize = 12.sp)
                    spacerV(height = 8.dp)
                    Text(
                        text = item.warna.orEmpty().lowercase().capitalizeWords(),
                        fontSize = 16.sp, fontWeight = FontWeight.W600
                    )
                }
                spacerV(height = 12.dp)
                dividerSmallH()
                spacerV(height = 12.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Lihat Selengkapnya")
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = ""
                    )
                }
                spacerV(height = 12.dp)
            }
        }
    }
}