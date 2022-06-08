package com.feri.workshop.component.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.feri.workshop.R
import com.feri.workshop.data.model.Mobil
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.BackgroundColor
import com.feri.workshop.ui.theme.PrimaryColor
import com.feri.workshop.utils.showToast

object PersonSetting : Screen {
    override val routeName = "personsetting"

    @Composable
    override fun screen(navController: NavHostController) {
        val context= LocalContext.current
        val mainVM = getMainActivity().mainViewModel
        val myuser by remember { mainVM.myuser}
        val person by remember { mainVM.selectedPerson }
        var active by remember { mutableStateOf(person?.active ?: false) }
        var level by remember { mutableStateOf(person?.level?:0)}
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = "Person Setting")
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
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                person?.let {
                    Text(text = "Nama", fontWeight = FontWeight.W600)
                    Text(text = it.nama.orEmpty())
                    spacerV(height = 8.dp)
                    Text(text = "Email", fontWeight = FontWeight.W600)
                    Text(text = it.email.orEmpty())
                    spacerV(height = 8.dp)
                    Text(text = "Level", fontWeight = FontWeight.W600)
                    Row (verticalAlignment = Alignment.CenterVertically){
                        IconButton(onClick = { if (level>(myuser?.level?:0)+1) level-- }) {
                            Icon(imageVector = Icons.Default.RemoveCircleOutline, contentDescription = "")
                        }
                        Text(text = "$level")
                        IconButton(onClick = { level++ }) {
                            Icon(imageVector = Icons.Default.AddCircleOutline, contentDescription = "")
                        }
                    }
                    spacerV(height = 8.dp)
                    Text(text = "Status User", fontWeight = FontWeight.W600)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(
                            onClick = { active = true },
                            modifier = Modifier.weight(1f),
                            colors = if (active) ButtonDefaults.buttonColors(
                                PrimaryColor.copy(
                                    alpha = 0.75f
                                )
                            )
                            else ButtonDefaults.buttonColors(BackgroundColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Aktiv")
                        }
                        spacerH(width = 16.dp)
                        Button(
                            onClick = { active = false },
                            modifier = Modifier.weight(1f),
                            colors = if (!active) ButtonDefaults.buttonColors(
                                PrimaryColor.copy(
                                    alpha = 0.75f
                                )
                            )
                            else ButtonDefaults.buttonColors(BackgroundColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Non-Aktiv")
                        }
                    }
                    spacerV(height = 16.dp)
                    Button(onClick = {
                        mainVM.simpanUser(
                            person = person?.copy(active = active,level = level),
                            isLoading = {},
                            onSuccess = { navController.navigateUp() },
                            onFailed = {context.showToast(it)})
                    }, Modifier.fillMaxWidth(),shape = RoundedCornerShape(8.dp)) {
                        Text(text = "Simpan")
                    }
                }
            }
        }
    }
}