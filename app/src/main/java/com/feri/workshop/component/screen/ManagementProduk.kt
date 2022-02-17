package com.feri.workshop.component.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.R
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.SurfaceColor

object ManagementProduk : Screen {
    override val name = "ManagementProduk"
    override val showBottomNav = true
    override val icon: Int = R.drawable.ic_box

    @Composable
    override fun screen(navController: NavHostController) {
        var search by remember { mutableStateOf("") }
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                spacerV(height = 16.dp)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        placeholder = { Text(text = "Cari produk kamu", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "")
                        }
                    )
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "")
                    }
                }
                spacerV(height = 8.dp)
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(10) {
                        spacerV(height = 16.dp)
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Card(
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.size(56.dp)
                                ) {

                                }
                                spacerH(width = 16.dp)
                                Column {
                                    Text(text = "Rem")
                                    Text(text = "Rp8.000")
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_healthicons_rdt_result_out_stock_outline),
                                            contentDescription = ""
                                        )
                                        spacerH(width = 4.dp)
                                        Text(text = "Stok 1999", fontSize = 12.sp)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_eos_icons_product_subscriptions),
                                            contentDescription = "",
                                            modifier = Modifier.offset(x = 2.dp, y = 2.5.dp)
                                        )
                                        spacerH(width = 4.dp)
                                        Text(text = "Terjual 10rb+", fontSize = 12.sp)
                                    }
                                }
                                spacerH(width = 14.dp)
                                Column {
                                    Card(
                                        backgroundColor = SurfaceColor,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.width(width = 50.dp),
                                        border = BorderStroke(width = 1.dp, color = Color.White)
                                    ) {
                                        Box(Modifier.padding(vertical = 4.dp)) {
                                            Text(
                                                text = "Arsipkan",
                                                fontSize = 10.sp,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    }
                                    spacerV(height = 8.dp)
                                    Card(
                                        backgroundColor = SurfaceColor,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.width(width = 50.dp),
                                        border = BorderStroke(width = 1.dp, color = Color.White)
                                    ) {
                                        Box(Modifier.padding(vertical = 4.dp)) {
                                            Text(
                                                text = "Ubah",
                                                fontSize = 10.sp,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item {
                        spacerV(height = 80.dp)
                    }
                }
            }
        }
    }
}