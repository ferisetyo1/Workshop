package com.feri.workshop.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.R
import com.feri.workshop.ui.helper.dividerSmall
import com.feri.workshop.ui.helper.spacerV

object Beranda : Screen {
    override val name: String = "Beranda"
    override val icon: Int = R.drawable.ic_home
    override val showBottomNav: Boolean = true

    @ExperimentalAnimationApi
    @Composable
    override fun screen(navController: NavHostController) {
        val activity = LocalContext.current as MainActivity
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            spacerV(height = 16.dp)
            Text(text = "Welcome to Q-workshop!", fontSize = 24.sp, fontWeight = FontWeight.W500)
            spacerV(height = 16.dp)
//            dividerSmall()
            spacerV(height = 16.dp)
            Text(text = "Important Today",fontSize = 18.sp, fontWeight = FontWeight.W700)
            Card(modifier = Modifier.fillMaxWidth(),backgroundColor = Color.Black) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Column (modifier = Modifier.weight(1f)) {
                            Text(text = "Pelanggan Hari Ini")
                            spacerV(height = 16.dp)
                            Text(text = "8")
                        }
                        Column (modifier = Modifier.weight(1f)) {
                            Text(text = "Order Hari Ini")
                            spacerV(height = 16.dp)
                            Text(text = "8")
                        }
                    }
                }
            }
        }
    }


}
