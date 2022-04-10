package com.feri.workshop.component.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.feri.workshop.MainActivity
import com.feri.workshop.R
import com.feri.workshop.ui.helper.dividerSmallH
import com.feri.workshop.ui.helper.spacerH
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.capitalizeWords

object Profile : Screen {
    override val routeName = "Profile"
    override val icon = R.drawable.ic_profile
    override val showBottomNav = true

    @ExperimentalAnimationApi
    @Composable
    override fun screen(navController: NavHostController) {
        val mainVM = getMainActivity().mainViewModel
        val person by remember { mainVM.myuser }
        Column(Modifier.padding(horizontal = 16.dp)) {
            spacerV(height = 16.dp)
            Row {
                Card(shape = CircleShape) {
                    Column(Modifier.padding(16.dp)) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                }
                spacerH(width = 16.dp)
                Column() {
                    Text(
                        text = person?.nama.orEmpty().capitalizeWords(),
                        fontWeight = FontWeight.W600
                    )
                    Text(text = "Level ${person?.level}")
                    Text(text = person?.email.orEmpty())
                }
            }
            spacerV(height = 16.dp)
            subList(Icons.Default.Group,label = "User Managemen", onClick = {})
            subList(icons = Icons.Default.Logout,label = "Logout", onClick = { mainVM.logOut() })
        }
    }

    @Composable
    fun subList(icons:ImageVector,label: String, onClick: () -> Unit = {}) {
        Column(Modifier.clickable { onClick() }) {
            dividerSmallH()
            spacerV(height = 8.dp)
            Row (verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icons, contentDescription = "")
                spacerH(width = 8.dp)
                Text(text = label)
            }
            spacerV(height = 8.dp)
        }
    }
}