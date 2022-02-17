package com.feri.workshop.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.feri.workshop.ui.helper.spacerV

object ListCustomer : Screen {
    override val name = "ListCustomer"

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
                        placeholder = { Text(text = "Cari Customer", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "")
                        }
                    )
                    IconButton(onClick = { navController.navigate(AddCustomer.name)}) {
                        Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "")
                    }
                }
                spacerV(height = 8.dp)
            }
        }
    }
}