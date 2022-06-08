package com.feri.workshop.component.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.feri.workshop.ui.helper.focusModifier
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.showToast

object UserManage : Screen {
    override val routeName = "usermanage"

    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val mainVM = getMainActivity().mainViewModel
        val persons by remember { mainVM.personList }
        val myuser by remember { mainVM.myuser }
        var search by remember { mutableStateOf("") }
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = "User Managemen")
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
            Column(Modifier.fillMaxSize()) {
                OutlinedTextField(value = search,
                    onValueChange = { search = it },
                    modifier = focusModifier()
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = {
                        Text(
                            text = "Cari nama atau email",color = Color.LightGray
                        )
                    },shape = RoundedCornerShape(16.dp))
                spacerV(height = 16.dp)
                persons.filter {
                    it.nama.orEmpty().contains(search, true) || it.email.orEmpty()
                        .contains(search, true)
                }.forEach { person ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable {
                                if (myuser?.level ?: 0 < person.level ?: 0) {
                                    mainVM.selectedPerson.value = person
                                    navController.navigate(PersonSetting.routeName)
                                } else {
                                    context.showToast("Kamu tidak memiliki izin")
                                }
                            },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = person.nama.orEmpty() + " (${person.email})")
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = ""
                            )
                        }
                    }
                    spacerV(height = 8.dp)
                }
            }
        }
    }
}