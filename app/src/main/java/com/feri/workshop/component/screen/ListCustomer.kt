package com.feri.workshop.component.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.R
import com.feri.workshop.data.model.Customer
import com.feri.workshop.ui.helper.*
import com.feri.workshop.utils.capitalizeWords
import com.feri.workshop.utils.toFormattedString
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit

object ListCustomer : Screen {
    override val routeName = "ListCustomer"

    @Composable
    override fun screen(navController: NavHostController) {
        val activity = LocalContext.current as MainActivity
        val customerVM = activity.customerViewModel
        val customers by remember { customerVM.customers }
        var search by remember { mutableStateOf("") }
        val isLoading by remember { customerVM.isLoadingList }
        var expand by remember { mutableStateOf(false) }
        var sort by remember { mutableStateOf("alpha-asc") }
        val context = LocalContext.current
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
                        placeholder = { Text(text = "Cari Customer", color = Color.Gray) },
                        modifier = focusModifier()
                            .weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "")
                        }
                    )
                    spacerH(width = 4.dp)
                    IconButton(onClick = { navController.navigate(AddCustomer.routeName) }) {
                        Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "")
                    }
                    Box {
                        IconButton(onClick = { expand = !expand }) {
                            Icon(imageVector = Icons.Default.Sort, contentDescription = "")
                        }
                        DropdownMenu(
                            expanded = expand,
                            onDismissRequest = { expand = false }) {
                            DropdownMenuItem(onClick = {
                                sort = "alpha-asc"
                                expand = false
                            }) {
                                Text(text = "Nama (A-Z)")
                            }
                            DropdownMenuItem(onClick = {
                                sort = "alpha-desc"
                                expand = false
                            }) {
                                Text(text = "Nama (Z-A)")
                            }
                            DropdownMenuItem(onClick = {
                                sort = "service-asc"
                                expand = false
                            }) {
                                Text(text = "Service ↓")
                            }
                            DropdownMenuItem(onClick = {
                                sort = "service-desc"
                                expand = false
                            }) {
                                Text(text = "Service ↑")
                            }
                        }
                    }
                    spacerH(width = 8.dp)
                }
                spacerV(height = 8.dp)
                screenLoading(loading = isLoading)
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    item { spacerV(height = 16.dp) }
                    items(when (sort) {
                        "alpha-desc" -> customers.sortedByDescending { it.nama }
                        "service-asc" -> customers.sortedBy { it.lastTransaksion }
                        "service-desc" -> customers.sortedByDescending { it.lastTransaksion }
                        else -> customers.sortedBy { it.nama }
                    }.filter {
                        it.nama.orEmpty().contains(search, true) || it.alamat.orEmpty()
                            .contains(search, true) || it.notelp.orEmpty()
                            .contains(search, true)
                    }) { customer ->
                        itemCustomer(customer) {
                            customerVM.selectedCustomer.value = customer
                            navController.navigate(DetailCustomer.routeName)
                        }
                        spacerV(height = 8.dp)
                    }
                    item { spacerV(height = 8.dp) }
                }
            }
        }
    }

    @Composable
    fun itemCustomer(customer: Customer, onClickCard: () -> Unit) {
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClickCard() },
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
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
                            customer.createdAt ?: 0
                        ).toFormattedString("dd MMMM yyyy, HH:mm"),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                spacerV(height = 8.dp)
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PersonOutline,
                                contentDescription = ""
                            )
                            spacerH(width = 8.dp)
                            Text(text = customer.nama.orEmpty().capitalizeWords())
                        }
                        spacerV(height = 8.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Phone,
                                contentDescription = ""
                            )
                            spacerH(width = 8.dp)
                            Text(text = customer.notelp.orEmpty())
                        }
                        spacerV(height = 8.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Map,
                                contentDescription = ""
                            )
                            spacerH(width = 8.dp)
                            Text(text = customer.alamat.orEmpty())
                        }
                        spacerV(height = 8.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CarRepair,
                                contentDescription = ""
                            )
                            spacerH(width = 8.dp)
                            Text(
                                text = Date(
                                    customer.lastTransaksion ?: 0
                                ).toFormattedString("dd MMMM yyyy, HH:mm")
                            )
                        }
                    }

                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_wa
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                var number = customer.notelp.orEmpty()
                                val firstchar = number[0]
                                if (firstchar.toString() == "0") {
                                    number = "+62" + number.substring(1)
                                }
                                val text =
                                    "Selamat siang, Yth. Bapak/Ibu/Saudara/i ${customer.nama?.capitalizeWords()}\n\nKami ingin mengingatkan bahwa anda belum melakukan servis selama ${
                                        TimeUnit.MILLISECONDS.toDays(Date().time - (customer.lastTransaksion ?: 0))
                                    } hari, yuk segera service mobil kesayangan anda!\n\nTerima Kasih"
                                context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                                    setData(
                                        Uri.parse(
                                            "https://api.whatsapp.com/send?phone=${
                                                URLEncoder.encode(
                                                    number,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                            }&text=${
                                                URLEncoder.encode(
                                                    text,
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                            }"
                                        )
                                    )
                                })
                            })
                    spacerH(width = 12.dp)
                }
                spacerV(height = 8.dp)
                dividerSmallH()
                spacerV(height = 8.dp)
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
            }
        }
    }
}