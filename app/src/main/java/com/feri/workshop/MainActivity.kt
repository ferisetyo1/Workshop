package com.feri.workshop

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.feri.workshop.component.screen.*
import com.feri.workshop.component.viewmodel.CustomerViewModel
import com.feri.workshop.ui.helper.BottomNavigationBar
import com.feri.workshop.ui.theme.WorkshopTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mainViewModel by viewModels<MainViewModel>()
    val customerViewModel by viewModels<CustomerViewModel>()

    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            val navController = rememberAnimatedNavController()
            var showBottomBar by remember { mutableStateOf(false) }
            val currentAccount by remember { mainViewModel.currentAccount }
            WorkshopTheme(darkTheme = true) {
                Scaffold(bottomBar = {
                    if (showBottomBar) BottomNavigationBar(
                        navController = navController,
                        items = listOf(Beranda, ManagementProduk, Reminder, Profile),
                        onItemClick = {
                            navController.navigate(it.name) {
                                if (it.name != Beranda.name) {
                                    popUpTo(it.name) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }) {
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Splash.name
                    ) {
                        composable(Beranda.name) {
                            showBottomBar = Beranda.showBottomNav
                            Beranda.screen(navController)
                        }
                        composable(Profile.name) {
                            showBottomBar = Profile.showBottomNav
                            Profile.screen(navController)
                        }
                        composable(Reminder.name) {
                            showBottomBar = Reminder.showBottomNav
                            Reminder.screen(navController)
                        }
                        composable(ManagementProduk.name) {
                            showBottomBar = ManagementProduk.showBottomNav
                            ManagementProduk.screen(navController)
                        }
                        composable(Splash.name) {
                            showBottomBar = Splash.showBottomNav
                            Splash.screen(navController)
                        }
                        composable(ListProduk.name) {
                            showBottomBar = ListProduk.showBottomNav
                            ListProduk.screen(navController)
                        }
                        composable(DetailProduk.name) {
                            showBottomBar = DetailProduk.showBottomNav
                            DetailProduk.screen(navController)
                        }
                        composable(AddCustomer.name) {
                            showBottomBar = AddCustomer.showBottomNav
                            AddCustomer.screen(navController)
                        }
                        composable(SuccessAddCustomer.name) {
                            showBottomBar = SuccessAddCustomer.showBottomNav
                            SuccessAddCustomer.screen(navController)
                        }
                        composable(FindCustomer.name) {
                            showBottomBar = FindCustomer.showBottomNav
                            FindCustomer.screen(navController)
                        }
                        composable(ListCustomer.name) {
                            showBottomBar = ListCustomer.showBottomNav
                            ListCustomer.screen(navController)
                        }
                        composable(AntrianService.name) {
                            showBottomBar = AntrianService.showBottomNav
                            AntrianService.screen(navController)
                        }
                        composable(Checkout.name) {
                            showBottomBar = Checkout.showBottomNav
                            Checkout.screen(navController)
                        }
                        composable(Login.name) {
                            showBottomBar = Login.showBottomNav
                            Login.screen(navController)
                        }
                        composable(InfoTransaksi.name) {
                            showBottomBar = InfoTransaksi.showBottomNav
                            InfoTransaksi.screen(navController)
                        }
                        composable(RingkasanTransaksi.name) {
                            showBottomBar = RingkasanTransaksi.showBottomNav
                            RingkasanTransaksi.screen(navController)
                        }
                        composable(ListFinancial.name) {
                            showBottomBar = ListFinancial.showBottomNav
                            ListFinancial.screen(navController)
                        }
                        composable(DetailCustomer.name) {
                            showBottomBar = DetailCustomer.showBottomNav
                            DetailCustomer.screen(navController)
                        }
                        composable(AddMobil.name) {
                            showBottomBar = AddMobil.showBottomNav
                            AddMobil.screen(navController)
                        }
                    }.also {
                        LaunchedEffect(key1 = currentAccount) {
                            if (currentAccount == null) navController.navigate(Login.name) {
                                navController.backQueue.clear()
                            }
                        }
                    }
                }
            }
        }
    }

}

