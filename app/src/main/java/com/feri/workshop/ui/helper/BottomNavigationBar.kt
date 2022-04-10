package com.feri.workshop.ui.helper

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.feri.workshop.component.screen.Screen
import com.feri.workshop.ui.theme.PrimaryColor

@Composable
fun BottomNavigationBar(
    items: List<Screen>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (Screen) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier.clip(RoundedCornerShape(topStart = 16.dp,topEnd = 16.dp)),
        backgroundColor = Color.Black,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.routeName == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = PrimaryColor,
                unselectedContentColor = Color.White,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.routeName
                        )
                    }
                })
        }
    }
}