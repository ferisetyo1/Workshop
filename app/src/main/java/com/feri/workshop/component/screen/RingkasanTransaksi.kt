package com.feri.workshop.component.screen

import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import jxl.Workbook
import jxl.write.Label
import org.library.worksheet.cellstyles.WorkSheet
import java.io.File
import java.util.*

object RingkasanTransaksi : Screen {
    override val routeName = "RingkasanTransaksi"
    override val pageTitle = "Transaksi Saya"

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun screen(navController: NavHostController) {
        val rentangVM = getMainActivity().rentangViewModel
        val transaksiVM = getMainActivity().transaksiViewModel
        LaunchedEffect(key1 = true) {
            rentangVM.reset()
        }
        val context = LocalContext.current
        var expandMenu by remember { mutableStateOf(false) }
        var type by rememberSaveable { transaksiVM.selectedType }
        var status by rememberSaveable { transaksiVM.selectedStatus }
        val startDate by rememberSaveable { rentangVM.startDate }
        val endDate by rememberSaveable { rentangVM.endDate }
        val onFilterRentang by rememberSaveable { rentangVM.onFilter }
        LaunchedEffect(key1 = onFilterRentang) {
            if (onFilterRentang) transaksiVM.selectedType.value = "Rentang"
        }
        val listTransaksi by remember { transaksiVM.listTransaksi }
        val filterTransaksi = listTransaksi.sortedByDescending { it.createdAt }.filter {
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
        val totalPenjualan = filterTransaksi.sumOf { it.getTotal() }
        val totalPengeluaran =
            filterTransaksi.filter { it.paidValue != it.getTotal() }.sumOf { it.getTotal() }

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
                    IconButton(onClick = {
                        try {
                            val storageDir = File(context.getExternalFilesDir(null), "/workshop")
                            var success = true
                            if (!storageDir.exists()) {
                                success = storageDir.mkdir()
                            }

                            if (success) {
                                val file = File(storageDir, "summary-${Date().toFormattedString("yyyy_MM_dd_HH_mm")}.xls")
                                if (!file.exists()) {
                                    success = file.createNewFile()
                                }
                                val workbook = Workbook.createWorkbook(file)
                                workbook.createSheet("Halo", 0).apply {
                                    addCell(Label(0, 0, "NO"))
                                    addCell(Label(1, 0, "ID TRANSAKSI"))
                                    addCell(Label(2, 0, "NAMA"))
                                    addCell(Label(3, 0, "TELP"))
                                    addCell(Label(4, 0, "NOPOL"))
                                    addCell(Label(5, 0, "OLD KM"))
                                    addCell(Label(6, 0, "NEW KM"))
                                    addCell(Label(7, 0, "KETERANGAN"))
                                    addCell(Label(8, 0, "JUMLAH DIBAYAR"))
                                    addCell(Label(9, 0, "TOTAL PEMBAYARAN"))
                                    addCell(Label(10, 0, "STATUS"))
                                    addCell(Label(11, 0, "DIBUAT PADA"))
                                    addCell(Label(12, 0, "DIBUAT OLEH"))
                                    addCell(Label(13, 0, "DIPROSES OLEH"))
                                    addCell(Label(14, 0, "DI SELESAIKAN OLEH"))
                                    addCell(Label(15, 0, "DI BATALKAN OLEH"))
                                    filterTransaksi.forEachIndexed { index, transaksi ->
                                        addCell(Label(0, index + 1, (index + 1).toString()))
                                        addCell(Label(1, index + 1, transaksi.id))
                                        addCell(
                                            Label(
                                                2,
                                                index + 1,
                                                transaksi.customer?.nama.orEmpty()
                                            )
                                        )
                                        addCell(
                                            Label(
                                                3,
                                                index + 1,
                                                transaksi.customer?.notelp.orEmpty()
                                            )
                                        )
                                        addCell(
                                            Label(
                                                4,
                                                index + 1,
                                                transaksi.mobil?.nopol.orEmpty()
                                            )
                                        )
                                        addCell(
                                            Label(
                                                5,
                                                index + 1,
                                                transaksi.mobil?.oldKM.toString()
                                            )
                                        )
                                        addCell(
                                            Label(
                                                6,
                                                index + 1,
                                                transaksi.mobil?.newKM.toString()
                                            )
                                        )
                                        addCell(
                                            Label(
                                                7,
                                                index + 1,
                                                transaksi.keterangan.toString()
                                            )
                                        )
                                        addCell(Label(8, index + 1, transaksi.paidValue.toString()))
                                        addCell(
                                            Label(
                                                9,
                                                index + 1,
                                                transaksi.getTotal().toString()
                                            )
                                        )
                                        addCell(
                                            Label(
                                                10,
                                                index + 1,
                                                transaksi.status.toString().uppercase()
                                            )
                                        )
                                        addCell(
                                            Label(
                                                11,
                                                index + 1,
                                                Date(
                                                    transaksi.createdAt ?: 0
                                                ).toFormattedString("dd MMM yyyy HH:mm")
                                            )
                                        )
                                        addCell(Label(12, index + 1, transaksi.createdBy.orEmpty()))
                                        addCell(
                                            Label(
                                                13,
                                                index + 1,
                                                transaksi.procededBy.orEmpty()
                                            )
                                        )
                                        addCell(
                                            Label(
                                                14,
                                                index + 1,
                                                transaksi.finishedBy.orEmpty()
                                            )
                                        )
                                        addCell(
                                            Label(
                                                15,
                                                index + 1,
                                                transaksi.canceledBy.orEmpty()
                                            )
                                        )
                                    }
                                }
                                workbook.write()
                                workbook.close()
                                context.showToast("Berhasil membuat file export")
                                println("path $success ${file.absolutePath}")
                            }
                            println("path $success ${storageDir.absolutePath}")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                    Box {
                        IconButton(onClick = { expandMenu = true }) {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                        spacerH(width = 8.dp)
                        DropdownMenu(
                            expanded = expandMenu,
                            onDismissRequest = { expandMenu = false }) {
                            DropdownMenuItem(onClick = {
                                transaksiVM.selectedType.value = "Semua"
                                expandMenu = false
                            }) {
                                Text(text = "Semua")
                            }
                            DropdownMenuItem(onClick = {
                                transaksiVM.selectedType.value = "Hari ini"
                                expandMenu = false
                            }) {
                                Text(text = "Hari ini")
                            }
                            DropdownMenuItem(onClick = {
                                transaksiVM.selectedType.value = "Minggu ini"
                                expandMenu = false
                            }) {
                                Text(text = "Minggu ini")
                            }
                            DropdownMenuItem(onClick = {
                                transaksiVM.selectedType.value = "Bulan ini"
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
            ) {
                spacerV(height = 16.dp)
                Row(
                    Modifier
                        .selectableGroup()
                        .horizontalScroll(rememberScrollState())
                ) {
                    spacerH(width = 16.dp)
                    Chip(
                        onClick = { transaksiVM.selectedStatus.value = "Semua" },
                        border = BorderStroke(
                            color = if (status == "Semua") PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Semua")
                    }
                    spacerH(width = 8.dp)
                    Chip(
                        onClick = { transaksiVM.selectedStatus.value = Transaksi.STATUS.Menunggu },
                        border = BorderStroke(
                            color = if (status == Transaksi.STATUS.Menunggu) PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Menunggu")
                    }
                    spacerH(width = 8.dp)
                    Chip(
                        onClick = { transaksiVM.selectedStatus.value = Transaksi.STATUS.Diproses },
                        border = BorderStroke(
                            color = if (status == Transaksi.STATUS.Diproses) PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Diproses")
                    }
                    spacerH(width = 8.dp)
                    Chip(
                        onClick = { transaksiVM.selectedStatus.value = Transaksi.STATUS.Selesai },
                        border = BorderStroke(
                            color = if (status == Transaksi.STATUS.Selesai) PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Selesai")
                    }
                    spacerH(width = 8.dp)
                    Chip(
                        onClick = { transaksiVM.selectedStatus.value = Transaksi.STATUS.Dibatalkan },
                        border = BorderStroke(
                            color = if (status == Transaksi.STATUS.Dibatalkan) PrimaryColor else SurfaceColor,
                            width = 1.dp
                        )
                    ) {
                        Text(text = "Dibatalkan")
                    }
                    spacerH(width = 16.dp)
                }
                if (type != "Semua") spacerV(height = 8.dp)
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
                if (type != "Semua") spacerV(height = 16.dp)
                val totalOrder = filterTransaksi.size
                val totalCustomer = filterTransaksi.distinctBy { it.customer?.id }.size
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { navController.navigate(AntrianService.routeName) },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(text = "Total Order", fontSize = 11.sp)
                            spacerV(height = 8.dp)
                            Text(text = totalOrder.toString(), fontWeight = FontWeight.W600)
                        }
                    }
                    spacerH(width = 8.dp)
                    Card(
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(text = "Total Pelanggan", fontSize = 11.sp)
                            spacerV(height = 8.dp)
                            Text(
                                text = totalCustomer.toString(),
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                    spacerH(width = 8.dp)
                    Card(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(text = "Keuntungan", fontSize = 11.sp)
                            spacerV(height = 8.dp)
                            Text(
                                text = filterTransaksi.map {
                                    it.carts.orEmpty().map {
                                        (it.jumlah ?: 0) * ((it.produk?.harga
                                            ?: 0.0) - (it.produk?.hargaPokok ?: 0.0))
                                    }.sum()
                                }.sum().toRupiahCurrency(),
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                }
                spacerV(height = 16.dp)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(text = "Total Penjualan", fontSize = 11.sp)
                            spacerV(height = 8.dp)
                            Text(
                                text = totalPenjualan.toRupiahCurrency(),
                                fontWeight = FontWeight.W600,
                                color = Color.Green
                            )
                        }
                    }
                    spacerH(width = 8.dp)
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(text = "Total Credit", fontSize = 11.sp)
                            spacerV(height = 8.dp)
                            Text(
                                text = totalPengeluaran.toRupiahCurrency(),
                                fontWeight = FontWeight.W600,
                                color = Color.Red
                            )
                        }
                    }
                    spacerH(width = 8.dp)
                    Card(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(text = "Tingkat Konversi", fontSize = 11.sp)
                            spacerV(height = 8.dp)
                            Text(
                                text = ((totalOrder.toDouble() / totalCustomer.toDouble()) * 100.0).prettyNum() + "%",
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                }
                spacerV(height = 16.dp)
                Text(
                    text = "Grafik Finansial",
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = "Ketahui perkembangan transaksi",
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                spacerV(height = 16.dp)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        spacerV(height = 16.dp)
                        Text(
                            text = "${filterTransaksi.size} Transaksi",
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(horizontal = 12.dp),
                            color = PrimaryColor
                        )
                        spacerV(height = 16.dp)
                        Beranda.grafik(
                            listTransaksi = filterTransaksi,
                            paddingHorizontal = 12.dp
                        )
                        spacerV(height = 12.dp)
                    }
                }
            }
        }
    }
}