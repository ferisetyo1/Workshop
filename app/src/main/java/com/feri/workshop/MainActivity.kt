package com.feri.workshop

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.feri.workshop.component.screen.*
import com.feri.workshop.component.sheet.BiayaLainnya
import com.feri.workshop.component.sheet.MetodePembayaran
import com.feri.workshop.component.sheet.RentangTanggal
import com.feri.workshop.component.sheet.TambahStock
import com.feri.workshop.component.viewmodel.CustomerViewModel
import com.feri.workshop.component.viewmodel.ProdukViewModel
import com.feri.workshop.component.viewmodel.RentangTanggalViewModel
import com.feri.workshop.component.viewmodel.TransaksiViewModel
import com.feri.workshop.ui.helper.BottomNavigationBar
import com.feri.workshop.ui.helper.permission
import com.feri.workshop.ui.theme.WorkshopTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val transaksiViewModel by viewModels<TransaksiViewModel>()
    val rentangViewModel by viewModels<RentangTanggalViewModel>()
    val mainViewModel by viewModels<MainViewModel>()
    val customerViewModel by viewModels<CustomerViewModel>()
    val produkViewModel by viewModels<ProdukViewModel>()

    @OptIn(ExperimentalPermissionsApi::class)
    @ExperimentalMaterialNavigationApi
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            val navController = rememberAnimatedNavController(bottomSheetNavigator)
            var showBottomBar by remember { mutableStateOf(false) }
            val currentAccount by remember { mainViewModel.currentAccount }
            WorkshopTheme(darkTheme = true) {
                ModalBottomSheetLayout(
                    bottomSheetNavigator = bottomSheetNavigator,
                    scrimColor = Color.Black.copy(alpha = 0.8f),
                    sheetShape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp)
                ) {
                    Scaffold(bottomBar = {
                        if (showBottomBar) BottomNavigationBar(
                            navController = navController,
                            items = listOf(Beranda, ManagementProduk, Profile),
                            onItemClick = {
                                navController.navigate(it.routeName) {
                                    if (it.routeName != Beranda.routeName) {
                                        popUpTo(it.routeName) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }) {
                        permission(
                            list = listOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE
                            )
                        )
                        AnimatedNavHost(
                            navController = navController,
                            startDestination = Splash.routeName,
                        ) {
                            listOf(
                                AddCustomer,
                                AddMobil,
                                AntrianService,
                                Beranda,
                                Checkout,
                                DetailCustomer,
                                DetailProduk,
                                FindCustomer,
                                InfoTransaksi,
                                ListCustomer,
                                ListFinancial,
                                ListProduk,
                                Login,
                                ManagementProduk,
                                Profile,
                                Reminder,
                                RingkasanTransaksi,
                                Splash,
                                SuccessAddCustomer,
                                DetailMobil,
                                AddProduk,
                                AddKategoriProduk,
                                DetailProduk,
                                GantiPassword,
                                Register,
                                SuccessRegister,
                                ForgotPassword,
                                SuccessSentMail,
                                UserManage,
                                PersonSetting
                            ).forEach { screen ->
                                composable(screen.routeName,
                                    enterTransition = {
                                        if (!screen.showBottomNav) {
                                            slideInHorizontally(
                                                initialOffsetX = { it },
                                                animationSpec = tween(
                                                    durationMillis = 200,
                                                    easing = LinearOutSlowInEasing
                                                )
                                            )
                                        } else fadeIn()
                                    },
                                    exitTransition = {
                                        if (!screen.showBottomNav) {
                                            slideOutHorizontally(
                                                targetOffsetX = { -it },
                                                animationSpec = tween(
                                                    durationMillis = 200,
                                                    easing = LinearOutSlowInEasing
                                                )
                                            )
                                        } else fadeOut()
                                    },
                                    popEnterTransition = {
                                        if (!screen.showBottomNav) {
                                            slideInHorizontally(
                                                initialOffsetX = { -it },
                                                animationSpec = tween(
                                                    durationMillis = 200,
                                                    easing = LinearOutSlowInEasing
                                                )
                                            )
                                        } else fadeIn()
                                    },
                                    popExitTransition = {
                                        if (!screen.showBottomNav) {
                                            slideOutHorizontally(
                                                targetOffsetX = { it },
                                                animationSpec = tween(
                                                    durationMillis = 200,
                                                    easing = LinearOutSlowInEasing
                                                )
                                            )
                                        } else fadeOut()
                                    }) {
                                    showBottomBar = screen.showBottomNav
                                    screen.screen(navController)
                                }
                                listOf(
                                    TambahStock,
                                    MetodePembayaran,
                                    BiayaLainnya,
                                    RentangTanggal
                                ).forEach { sheet ->
                                    bottomSheet(sheet.routeName) {
                                        sheet.screen(
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }.also {
                            LaunchedEffect(key1 = currentAccount) {
                                if (currentAccount == null) navController.navigate(Login.routeName) {
                                    navController.backQueue.clear()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

