package com.feri.workshop.component.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.R
import com.feri.workshop.ui.helper.dividerSmallH
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.PrimaryColor

object Beranda : Screen {
    override val name: String = "Beranda"
    override val icon: Int = R.drawable.ic_home
    override val showBottomNav: Boolean = true

    @Composable
    override fun screen(navController: NavHostController) {
        val activity = LocalContext.current as MainActivity
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
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp),
                backgroundColor = PrimaryColor.copy(alpha = 0.75f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    spacerV(height = 16.dp)
                    Row {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Pelanggan Hari Ini",fontSize = 12.sp)
                            spacerV(height = 16.dp)
                            Text(text = "8",fontSize = 18.sp,fontWeight = FontWeight.W500)
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Order Hari Ini",fontSize = 12.sp)
                            spacerV(height = 16.dp)
                            Text(text = "8",fontSize = 18.sp,fontWeight = FontWeight.W500)
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
                        Row (verticalAlignment = Alignment.CenterVertically){
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
                Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.clickable { navController.navigate(FindCustomer.name) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_inventory_black_24dp_1),
                        contentDescription = ""
                    )
                    Text(text = "Buat\nPesanan",textAlign = TextAlign.Center)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Analytics,
                        contentDescription = "",
                        modifier = Modifier.size(35.dp)
                    )
                    Text(text = "Catatan\nFinansial",textAlign = TextAlign.Center)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.clickable { navController.navigate(ListCustomer.name) }) {
                    Icon(
                        imageVector = Icons.Outlined.PersonAddAlt,
                        contentDescription = "",
                        modifier = Modifier.size(35.dp)
                    )
                    Text(text = "Manage\nCustomer",textAlign = TextAlign.Center)
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
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)){
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Total Penjualan",fontSize = 12.sp)
                            spacerV(height = 8.dp)
                            Text(text = "Rp9.100.000",fontWeight = FontWeight.W700)
                            spacerV(height = 8.dp)
                            Row (verticalAlignment = Alignment.CenterVertically){
                                Icon(imageVector = Icons.Default.TrendingUp, contentDescription = "",tint = Color.Green)
                                Text(text = " 63,52%")
                            }
                            Text(text = "vs bulan lalu",fontSize = 11.sp)
                        }
                        spacerH(width = 16.dp)
                        Column(modifier = Modifier.weight(1f)){
                            Text(text = "Total Pengeluaran",fontSize = 12.sp)
                            spacerV(height = 8.dp)
                            Text(text = "Rp9.100.000",fontWeight = FontWeight.W700)
                            spacerV(height = 8.dp)
                            Row (verticalAlignment = Alignment.CenterVertically){
                                Icon(imageVector = Icons.Default.TrendingDown, contentDescription = "",tint = Color.Red)
                                Text(text = " 63,52%")
                            }
                            Text(text = "vs bulan lalu",fontSize = 11.sp)
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
                        Row (verticalAlignment = Alignment.CenterVertically){
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
            Text(text = "Ketahui perkembangan transaksi",color = Color.LightGray,modifier = Modifier.padding(horizontal = 16.dp))
            spacerV(height = 16.dp)
            Row {
                Card (modifier = Modifier.padding(horizontal = 16.dp)){
                    Row (Modifier.padding(horizontal = 8.dp,vertical = 4.dp)){
                        Text(text = "Januari ")
                        Icon(imageVector = Icons.Default.KeyboardArrowDown,contentDescription = "")
                    }
                }
                Card (modifier = Modifier.padding(horizontal = 16.dp)){
                    Row (Modifier.padding(horizontal = 8.dp,vertical = 4.dp)){
                        Text(text = "Barang ")
                        Icon(imageVector = Icons.Default.KeyboardArrowDown,contentDescription = "")
                    }
                }
            }
            spacerV(height = 80.dp)
        }
    }


}
