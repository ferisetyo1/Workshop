package com.feri.workshop.component.screen

import android.text.format.DateUtils
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.R
import com.feri.workshop.data.model.Transaksi
import com.feri.workshop.ui.helper.dividerSmallH
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.PrimaryColor
import com.feri.workshop.utils.isDateInCurrentMonth
import com.feri.workshop.utils.toFormattedString
import java.util.*

object Beranda : Screen {
    override val routeName: String = "Beranda"
    override val icon: Int = R.drawable.ic_home
    override val showBottomNav: Boolean = true

    @Composable
    override fun screen(navController: NavHostController) {
        val transaksiVM = getMainActivity().transaksiViewModel
        val listTransaksi by remember { transaksiVM.listTransaksi }
        val listTransaksiSelesai = listTransaksi.filter { Transaksi.STATUS.Selesai == it.status }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            spacerV(height = 16.dp)
            Text(
                text = "Welcome to Q-workshop!",
                fontSize = 24.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            spacerV(height = 32.dp)
            Text(
                text = "Important Today",
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            spacerV(height = 16.dp)
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { navController.navigate(RingkasanTransaksi.routeName) },
                backgroundColor = PrimaryColor.copy(alpha = 0.75f),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    spacerV(height = 16.dp)
                    Row {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Pelanggan Hari Ini", fontSize = 12.sp)
                            spacerV(height = 16.dp)
                            Text(
                                text = listTransaksiSelesai.filter {
                                    DateUtils.isToday(
                                        it.createdAt ?: 0
                                    )
                                }.distinctBy { it.customer?.id }.size.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W500
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { navController.navigate(AntrianService.routeName) },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Order Hari Ini", fontSize = 12.sp)
                            spacerV(height = 16.dp)
                            Text(text = listTransaksiSelesai.filter {
                                DateUtils.isToday(
                                    it.createdAt ?: 0
                                )
                            }.size.toString(), fontSize = 18.sp, fontWeight = FontWeight.W500)
                        }
                    }
                    val menunggu=listTransaksi.filter {
                        DateUtils.isToday(
                            it.createdAt ?: 0
                        )&&it.status==Transaksi.STATUS.Menunggu
                    }.size
                    if (menunggu>0){
                        spacerV(height = 8.dp)
                        Text(
                            text = "$menunggu orderan menunggu konfirmasi*",
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.align(Alignment.End).padding(horizontal = 16.dp)
                        )
                    }
                    val process=listTransaksi.filter {
                        DateUtils.isToday(
                            it.createdAt ?: 0
                        )&&it.status==Transaksi.STATUS.Menunggu
                    }.size
                    if (process>0){
                        if (menunggu==0) spacerV(height = 8.dp)
                        Text(
                            text = "$process orderan sedang diproses*",
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.align(Alignment.End).padding(horizontal = 16.dp)
                        )
                    }
                    spacerV(height = 8.dp)
                    dividerSmallH(2.dp)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.Assessment, contentDescription = "")
                            spacerH(width = 8.dp)
                            Text(text = "Catatan Finansial Harian")
                        }
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = ""
                        )
                    }
                }
            }
            spacerV(height = 16.dp)
            dividerSmallH(height = 1.dp)
            spacerV(height = 16.dp)
            Text(
                text = "Auto-One",
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            spacerV(height = 16.dp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { navController.navigate(FindCustomer.routeName) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_inventory_black_24dp_1),
                        contentDescription = ""
                    )
                    Text(text = "Buat\nPesanan", textAlign = TextAlign.Center)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Analytics,
                        contentDescription = "",
                        modifier = Modifier.size(35.dp)
                    )
                    Text(text = "Catatan\nFinansial", textAlign = TextAlign.Center)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { navController.navigate(ListCustomer.routeName) }) {
                    Icon(
                        imageVector = Icons.Outlined.PersonAddAlt,
                        contentDescription = "",
                        modifier = Modifier.size(35.dp)
                    )
                    Text(text = "Manage\nCustomer", textAlign = TextAlign.Center)
                }
            }
            spacerV(height = 16.dp)
            dividerSmallH(height = 1.dp)
            spacerV(height = 16.dp)
            Text(
                text = "Laporan Finansial",
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            spacerV(height = 16.dp)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp),
                backgroundColor = PrimaryColor.copy(alpha = 0.75f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    spacerV(height = 16.dp)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Total Penjualan", fontSize = 12.sp)
                            spacerV(height = 8.dp)
                            Text(text = "Rp9.100.000", fontWeight = FontWeight.W700)
                            spacerV(height = 8.dp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = "",
                                    tint = Color.Green
                                )
                                Text(text = " 63,52%")
                            }
                            Text(text = "vs bulan lalu", fontSize = 11.sp)
                        }
                        spacerH(width = 16.dp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Total Pengeluaran", fontSize = 12.sp)
                            spacerV(height = 8.dp)
                            Text(text = "Rp9.100.000", fontWeight = FontWeight.W700)
                            spacerV(height = 8.dp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingDown,
                                    contentDescription = "",
                                    tint = Color.Red
                                )
                                Text(text = " 63,52%")
                            }
                            Text(text = "vs bulan lalu", fontSize = 11.sp)
                        }
                    }
                    spacerV(height = 16.dp)
                    dividerSmallH(2.dp)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.Assessment, contentDescription = "")
                            spacerH(width = 8.dp)
                            Text(text = "Detail Catatan Finansial")
                        }
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = ""
                        )
                    }
                }
            }
            spacerV(height = 16.dp)
            dividerSmallH(height = 1.dp)
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
//            Row {
//                Card(modifier = Modifier.padding(horizontal = 16.dp)) {
//                    Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
//                        Text(text = "Januari ")
//                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
//                    }
//                }
//                Card(modifier = Modifier.padding(horizontal = 16.dp)) {
//                    Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
//                        Text(text = "Barang ")
//                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
//                    }
//                }
//            }
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
                        text = "${listTransaksiSelesai.filter { Date(it.createdAt ?: 0).isDateInCurrentMonth() }.size} Transaksi",
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(horizontal = 12.dp),
                        color = PrimaryColor
                    )
                    spacerV(height = 16.dp)
                    grafik(
                        listTransaksi = listTransaksiSelesai,
                        paddingHorizontal = 12.dp
                    )
                    spacerV(height = 12.dp)
                }
            }
            spacerV(height = 80.dp)
        }
    }

    @Composable
    fun grafik(listTransaksi: List<Transaksi>, paddingHorizontal: Dp) {
        val groupTransaksi =
            listTransaksi.sortedBy { it.createdAt }
                .groupBy { Date(it.createdAt ?: 0).toFormattedString("dd MMM") }.toList()
        val heightMax = 100
        Row {
            spacerH(width = paddingHorizontal)
            groupTransaksi.forEachIndexed { index, pair ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .height(heightMax.dp)
                            .width(20.dp)
                            .background(
                                color = Color.DarkGray,
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                    ) {
                        Text(text = pair.second.size.toString(), fontSize = 11.sp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(((pair.second.size * heightMax) / listTransaksi.size).dp)
                                .background(
                                    color = if (index % 2 == 0 || index == 0) PrimaryColor else Color.Cyan,
                                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                )
                        )
                    }
                    spacerV(height = 8.dp)
                    Text(text = pair.first, fontSize = 11.sp)
                }
                spacerH(width = 8.dp)
            }
            spacerH(width = paddingHorizontal - 8.dp)
        }
    }


}
