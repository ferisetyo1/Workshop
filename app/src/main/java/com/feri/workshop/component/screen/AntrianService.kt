package com.feri.workshop.component.screen

import android.text.format.DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CarRepair
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.component.sheet.RentangTanggal
import com.feri.workshop.data.model.Transaksi
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.PrimaryColor
import com.feri.workshop.ui.theme.SurfaceColor
import com.feri.workshop.utils.*
import java.util.*

object AntrianService : Screen {
    override val routeName = "AntrianService"
    override val pageTitle = "Antrian Servis"

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun screen(navController: NavHostController) {
        val rentangVM = getMainActivity().rentangViewModel
        val transaksiVM = getMainActivity().transaksiViewModel
        LaunchedEffect(key1 = true) {
            rentangVM.reset()
        }
        var search by rememberSaveable { mutableStateOf("") }
        var expandMenu by rememberSaveable { mutableStateOf(false) }
        var type by rememberSaveable { mutableStateOf("Hari ini") }
        var status by rememberSaveable { mutableStateOf(Transaksi.STATUS.Menunggu) }
        val startDate by rememberSaveable { rentangVM.startDate }
        val endDate by rememberSaveable { rentangVM.endDate }
        val onFilterRentang by rememberSaveable { rentangVM.onFilter }
        LaunchedEffect(key1 = onFilterRentang) {
            if (onFilterRentang) type = "Rentang"
        }
        val listTransaksi by remember { transaksiVM.listTransaksi }
        val filterTransaksi = listTransaksi.sortedByDescending { it.createdAt }.filter {
            (it.customer?.nama?.lowercase().orEmpty().contains(search.lowercase()) ||
                    it.id?.lowercase().orEmpty().contains(search.lowercase()) ||
                    it.mobil?.nopol?.lowercase().orEmpty().contains(search.lowercase())) &&
                    when (type) {
                        "Semua" -> true
                        "Hari ini" -> DateUtils.isToday(it.createdAt ?: 0)
                        "Minggu ini" -> Date(it.createdAt ?: 0).isDateInCurrentWeek()
                        "Bulan ini" -> Date(it.createdAt ?: 0).isDateInCurrentMonth()
                        "Rentang" -> {
                            println("Rentang Start ${startDate.toFormattedString("dd MMM yyy HH:mm")}")
                            println("Rentang End ${endDate.toFormattedString("dd MMM yyy HH:mm")}")
                            startDate.time < (it.createdAt ?: 0) &&
                                    (it.createdAt ?: 0) < endDate.time
                        }
                        else -> true
                    } &&
                    when (status) {
                        "Semua" -> true
                        Transaksi.STATUS.Menunggu -> it.status == Transaksi.STATUS.Menunggu
                        Transaksi.STATUS.Diproses -> it.status == Transaksi.STATUS.Diproses
                        Transaksi.STATUS.Dibatalkan -> it.status == Transaksi.STATUS.Dibatalkan
                        else -> it.status == Transaksi.STATUS.Selesai
                    }
        }

        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                spacerV(height = 16.dp)
                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    spacerH(width = 8.dp)
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                    spacerH(width = 4.dp)
                    OutlinedTextField(
                        value = search,
                        onValueChange = {
                            search = it
                        },
                        placeholder = { Text(text = "Cari Transaksi", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "")
                        }
                    )
                    spacerH(width = 4.dp)
                    Box {
                        IconButton(onClick = { expandMenu = true }) {
                            Icon(
                                imageVector = Icons.Outlined.FilterAlt,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                        spacerH(width = 8.dp)
                        DropdownMenu(
                            expanded = expandMenu,
                            onDismissRequest = { expandMenu = false }) {
                            DropdownMenuItem(onClick = {
                                type = "Semua"
                                expandMenu = false
                            }) {
                                Text(text = "Semua")
                            }
                            DropdownMenuItem(onClick = {
                                type = "Hari ini"
                                expandMenu = false
                            }) {
                                Text(text = "Hari ini")
                            }
                            DropdownMenuItem(onClick = {
                                type = "Minggu ini"
                                expandMenu = false
                            }) {
                                Text(text = "Minggu ini")
                            }
                            DropdownMenuItem(onClick = {
                                type = "Bulan ini"
                                expandMenu = false
                            }) {
                                Text(text = "Bulan ini")
                            }
                            DropdownMenuItem(onClick = {
                                expandMenu = false
                                navController.navigate(RentangTanggal.routeName)
                            }) {
                                Text(text = "Rentang")
                            }
                        }
                    }
                    spacerH(width = 8.dp)
                }
                spacerV(height = 16.dp)
                Row(
                    Modifier
                        .selectableGroup()
                        .horizontalScroll(rememberScrollState())
                ) {
                    spacerH(width = 16.dp)
                    Chip(
                        onClick = { status = "Semua" },
                        border = BorderStroke(
                            color = if (status == "Semua") PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Semua ${if (status == "Semua") "(${filterTransaksi.size})" else ""}")
                    }
                    spacerH(width = 8.dp)
                    Chip(
                        onClick = { status = Transaksi.STATUS.Menunggu },
                        border = BorderStroke(
                            color = if (status == Transaksi.STATUS.Menunggu) PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Menunggu ${if (status == Transaksi.STATUS.Menunggu) "(${filterTransaksi.size})" else ""}")
                    }
                    spacerH(width = 8.dp)
                    Chip(
                        onClick = { status = Transaksi.STATUS.Diproses },
                        border = BorderStroke(
                            color = if (status == Transaksi.STATUS.Diproses) PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Diproses ${if (status == Transaksi.STATUS.Diproses) "(${filterTransaksi.size})" else ""}")
                    }
                    spacerH(width = 8.dp)
                    Chip(
                        onClick = { status = Transaksi.STATUS.Selesai },
                        border = BorderStroke(
                            color = if (status == Transaksi.STATUS.Selesai) PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Selesai ${if (status == Transaksi.STATUS.Selesai) "(${filterTransaksi.size})" else ""}")
                    }
                    spacerH(width = 8.dp)
                    Chip(
                        onClick = { status = Transaksi.STATUS.Dibatalkan },
                        border = BorderStroke(
                            color = if (status == Transaksi.STATUS.Dibatalkan) PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Dibatalkan ${if (status == Transaksi.STATUS.Dibatalkan) "(${filterTransaksi.size})" else ""}")
                    }
                    spacerH(width = 16.dp)
                }
                if (type != "Semua") spacerV(height = 16.dp)
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = when (type) {
                            "Semua" -> ""
                            "Rentang" -> "**${startDate.toFormattedString("dd MMM yyy")} - ${
                                endDate.toFormattedString(
                                    "dd MMM yyy"
                                )
                            }"
                            else -> "**${type.capitalizeWords()} "
                        },
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
                spacerV(height = 8.dp)
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filterTransaksi) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    transaksiVM.selectedTransaksi.value = it
                                    navController.navigate(InfoTransaksi.routeName)
                                },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CarRepair,
                                    contentDescription = ""
                                )
                                spacerH(width = 8.dp)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = it.id.orEmpty(),
                                        fontSize = 10.sp,
                                        color = Color.LightGray
                                    )
                                    spacerV(height = 8.dp)
                                    Text(
                                        text = it.customer?.nama.orEmpty().capitalizeWords(),
                                        fontWeight = FontWeight.W600
                                    )
                                    spacerV(height = 4.dp)
                                    Text(
                                        text = it.mobil?.nopol.orEmpty()
                                            .uppercase() + " | " + it.customer?.notelp.orEmpty(),
                                        fontSize = 12.sp
                                    )
                                    spacerV(height = 4.dp)
                                    Text(
                                        text = Date(
                                            it.createdAt ?: 0
                                        ).toFormattedString("dd MMM yyyy | HH:mm"),
                                        fontSize = 10.sp,
                                        color = Color.LightGray
                                    )
                                }
                                spacerH(width = 8.dp)
                                Text(
                                    text = it.status.orEmpty().capitalizeWords(),
                                    color = when (it.status) {
                                        Transaksi.STATUS.Selesai->Color.Green
                                        Transaksi.STATUS.Dibatalkan->Color.Red
                                        else -> PrimaryColor
                                    },
                                    fontWeight = FontWeight.W600
                                )
                            }
                        }
                        spacerV(height = 16.dp)
                    }
                }
            }
        }
    }
}