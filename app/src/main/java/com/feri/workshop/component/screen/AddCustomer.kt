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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.data.model.Customer
import com.feri.workshop.data.model.Mobil
import com.feri.workshop.ui.helper.focusModifier
import com.feri.workshop.ui.helper.screenLoading
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.BackgroundColor
import com.feri.workshop.ui.theme.PrimaryColor
import com.feri.workshop.utils.showToast
import java.util.regex.Pattern
import kotlin.collections.ArrayList

object AddCustomer : Screen {
    override val routeName = "Tambah Pelanggan"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val activity = context as MainActivity
        val customerVM = activity.customerViewModel
        val mainVM = activity.mainViewModel

        val user by remember { mainVM.currentAccount }
        var namapelanggan by remember { mutableStateOf("") }
        var errorNamaPelanggan by remember { mutableStateOf("") }

        var nomortelfon by remember { mutableStateOf("") }
        var errorPhoneNumber by remember { mutableStateOf("") }

        var alamat by remember { mutableStateOf("") }
        var errorAlamat by remember { mutableStateOf("") }

        var merk by remember { mutableStateOf("") }
        var errorMerk by remember { mutableStateOf("") }

        var nomorpolisi by remember { mutableStateOf(arrayListOf("", "", "")) }
        var errorNomorPolisi by remember { mutableStateOf("") }

        var tipe by remember { mutableStateOf(Mobil.TipeMobil.automatic) }

        var tahun by remember { mutableStateOf("") }

        var silinder by remember { mutableStateOf("") }

        var warna by remember { mutableStateOf("") }

        var noRangka by remember { mutableStateOf("") }

        var noMesin by remember { mutableStateOf("") }

        var keterangan by remember { mutableStateOf("") }

        var isLoading by remember { mutableStateOf(false) }

        val keyboard = LocalSoftwareKeyboardController.current
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = routeName)
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
            screenLoading(loading = isLoading)
            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
//                Text(text = "Data Pelanggan", fontSize = 24.sp, fontWeight = FontWeight.W700)
//                spacerV(height = 24.dp)
                Row {
                    Text(text = "Nama")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = namapelanggan,
                    onValueChange = {
                        errorNamaPelanggan = ""
                        namapelanggan = it.uppercase()
                    },
                    modifier = focusModifier()
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
                    modifier = focusModifier()
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
                    modifier = focusModifier()
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
                spacerV(height = 16.dp)
                Row {
                    Text(text = "Merk")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = merk,
                    onValueChange = {
                        errorMerk = ""
                        merk = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = errorMerk.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                if (errorMerk.isNotEmpty()) Text(
                    text = errorMerk,
                    color = Color.Red,
                    fontSize = 12.sp
                )
                spacerV(height = 16.dp)
                Row {
                    Text(text = "Nomor Polisi")
                    Text(text = "*", color = Color.Red)
                }
                spacerV(height = 8.dp)
                Row(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = nomorpolisi.get(0),
                        onValueChange = {
                            if (!Pattern.compile("[A-Za-z]+").matcher(it)
                                    .find() && it.isNotEmpty()
                            ) return@OutlinedTextField
                            if (it.length > 2) return@OutlinedTextField
                            errorNomorPolisi = ""
                            val _nomorpolisi = ArrayList(nomorpolisi)
                            _nomorpolisi.set(0, it.uppercase())
                            nomorpolisi = _nomorpolisi
                        },
                        modifier = focusModifier(FocusDirection.Next)
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        isError = errorNomorPolisi.isNotEmpty(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    spacerH(width = 8.dp)
                    OutlinedTextField(
                        value = nomorpolisi.get(1),
                        onValueChange = {
                            if (!Pattern.compile("[0-9]+").matcher(it)
                                    .find() && it.isNotEmpty()
                            ) return@OutlinedTextField
                            if (it.length > 4) return@OutlinedTextField
                            errorNomorPolisi = ""
                            val _nomorpolisi = ArrayList(nomorpolisi)
                            _nomorpolisi.set(1, it.uppercase())
                            nomorpolisi = _nomorpolisi
                        },
                        modifier = focusModifier(FocusDirection.Next)
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        isError = errorNomorPolisi.isNotEmpty(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Number
                        )
                    )
                    spacerH(width = 8.dp)
                    OutlinedTextField(
                        value = nomorpolisi.get(2),
                        onValueChange = {
                            if (!Pattern.compile("[A-Za-z]+").matcher(it)
                                    .find() && it.isNotEmpty()
                            ) return@OutlinedTextField
                            if (it.length > 3) return@OutlinedTextField
                            errorNomorPolisi = ""
                            val _nomorpolisi = ArrayList(nomorpolisi)
                            _nomorpolisi.set(2, it.uppercase())
                            nomorpolisi = _nomorpolisi
                        },
                        modifier = focusModifier()
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        isError = errorNomorPolisi.isNotEmpty(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                }
                if (errorNomorPolisi.isNotEmpty()) Text(
                    text = errorNomorPolisi,
                    color = Color.Red,
                    fontSize = 12.sp
                )
                spacerV(height = 16.dp)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = { tipe = Mobil.TipeMobil.automatic },
                        modifier = Modifier.weight(1f),
                        colors = if (tipe == Mobil.TipeMobil.automatic) buttonColors(
                            PrimaryColor.copy(
                                alpha = 0.75f
                            )
                        )
                        else buttonColors(BackgroundColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Automatic")
                    }
                    spacerH(width = 16.dp)
                    Button(
                        onClick = { tipe = Mobil.TipeMobil.manual },
                        modifier = Modifier.weight(1f),
                        colors = if (tipe == Mobil.TipeMobil.manual) buttonColors(
                            PrimaryColor.copy(
                                alpha = 0.75f
                            )
                        )
                        else buttonColors(BackgroundColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Manual")
                    }
                }
                spacerV(height = 16.dp)
                Text(text = "Tahun")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = tahun,
                    onValueChange = {
                        tahun = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                spacerV(height = 16.dp)
                Text(text = "Silinder")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = silinder,
                    onValueChange = {
                        silinder = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                spacerV(height = 16.dp)
                Text(text = "Warna")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = warna,
                    onValueChange = {
                        warna = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                spacerV(height = 16.dp)
                Text(text = "No. Rangka")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = noRangka,
                    onValueChange = {
                        noRangka = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                spacerV(height = 16.dp)
                Text(text = "No. Mesin")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = noMesin,
                    onValueChange = {
                        noMesin = it
                    },
                    modifier = focusModifier()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                    )
                )
                spacerV(height = 16.dp)
                Text(text = "Keterangan")
                spacerV(height = 8.dp)
                OutlinedTextField(
                    value = keterangan,
                    onValueChange = {
                        keterangan = it
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
                        if (namapelanggan.isEmpty()) {
                            errorNamaPelanggan = "Nama tidak boleh kosong."
                            return@Button
                        }
                        if (nomortelfon.isEmpty()) {
                            errorPhoneNumber = "No. Telfon tidak boleh kosong."
                            return@Button
                        }
                        if (alamat.isEmpty()) {
                            errorAlamat = "Alamat tidak boleh kosong."
                            return@Button
                        }
                        if (merk.isEmpty()) {
                            errorMerk = "Merk tidak boleh kosong."
                            return@Button
                        }
                        if (nomorpolisi.contains("")) {
                            errorNomorPolisi = "No. Polisi tidak boleh kosong."
                            return@Button
                        }
                        customerVM.addCustomer(
                            customer = Customer(
                                nama = namapelanggan.lowercase(),
                                notelp = nomortelfon,
                                alamat = alamat,
                                createdBy = "Feri",
                            ),
                            mobil = Mobil(
                                merk = merk.trim(),
                                nopol = nomorpolisi.joinToString(" ").trim(),
                                tipe = tipe.trim(),
                                tahun = tahun.trim(),
                                silinder = silinder.trim(),
                                warna = warna.trim(),
                                norangka = noRangka.trim(),
                                nomesin = noMesin.trim(),
                                keterangan = keterangan.trim(),
                                createdBy = user?.email,
                            ),
                            isLoading = { isLoading = it },
                            onSuccess = {
                                context.showToast("Berhasil menambahkan customer")
                                navController.navigateUp()
                            },
                            onFailed = { context.showToast(it) }
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
        }
    }
}