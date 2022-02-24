package com.feri.workshop.component.screen

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
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.component.viewmodel.CustomerViewModel
import com.feri.workshop.repository.model.Customer
import com.feri.workshop.ui.helper.dividerSmallH
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.capitalizeWords
import com.feri.workshop.utils.toFormattedString
import java.util.*

object ListCustomer : Screen {
    override val name = "ListCustomer"

    @Composable
    override fun screen(navController: NavHostController) {
        val activity = LocalContext.current as MainActivity
        val customerVM = activity.customerViewModel
        LaunchedEffect(key1 = true) {
            customerVM.reset()
            customerVM.listCustomer()
        }
        val customers by remember { customerVM.customers }
        var search by remember { mutableStateOf("") }
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                spacerV(height = 16.dp)
                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                    OutlinedTextField(
                        value = search,
                        onValueChange = {
                            search = it
                            customerVM.reset()
                            customerVM.listCustomer(query = it)
                        },
                        placeholder = { Text(text = "Cari Customer", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "")
                        }
                    )
                    IconButton(onClick = { navController.navigate(AddCustomer.name) }) {
                        Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "")
                    }
                    spacerH(width = 8.dp)
                }
                spacerV(height = 8.dp)
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    item { spacerV(height = 16.dp) }
                    items(customers) { customer ->
                        itemCustomer(customer) {
                            customerVM.selectedCustomer.value = customer
                            navController.navigate(DetailCustomer.name)
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