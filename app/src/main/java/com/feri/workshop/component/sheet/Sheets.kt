package com.feri.workshop.component.sheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.feri.workshop.MainActivity

interface Sheets {
    val routeName: String
    val pageTitle: String
        get() = ""

    @Composable
    fun screen(navController: NavHostController)

    @Composable
    fun getMainActivity(): MainActivity {
        val context= LocalContext.current
        return context as MainActivity
    }

}