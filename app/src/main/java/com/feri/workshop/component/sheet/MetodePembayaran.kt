package com.feri.workshop.component.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.ui.theme.PrimaryColor

object MetodePembayaran : Sheets {
    override val routeName = "metodepembayaran"

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun screen(navController: NavHostController) {
        val createTransaksiViewModel = getMainActivity().transaksiViewModel
        val selectedOption by remember { createTransaksiViewModel.selectedMetodePembayaran }
        val paidValue by remember { createTransaksiViewModel.paidValue}
        val keyoboard=LocalSoftwareKeyboardController.current
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            spacerV(height = 8.dp)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(8.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.CenterHorizontally)
            )
            spacerV(height = 16.dp)
            Text(text = "Pilih Metode Pembayaran", fontSize = 16.sp, fontWeight = FontWeight.W600)
            spacerV(height = 12.dp)
            Column(Modifier.selectableGroup()) {
                listOf(
                    "Tunai - Cash",
                    "Tunai - BCA",
                    "Tunai - Mandiri",
                    "Sebagian"
                ).forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    val subtotal = createTransaksiViewModel.selectedProduk.value.map {
                                        it.jumlah!! * it.produk!!.getFixHarga()
                                    }.sum()
                                    val subOthersPayment = createTransaksiViewModel.othersPayment.value.map { it.value ?: 0.0 }.sum()
                                    val ppn = if (createTransaksiViewModel.enablePPn.value) ((subtotal + subOthersPayment) * createTransaksiViewModel.ppnValue.value) / 100 else 0.0
                                    val total = subtotal + subOthersPayment + ppn
                                    createTransaksiViewModel.paidValue.value = total.toInt()
                                    createTransaksiViewModel.selectedMetodePembayaran.value = text
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null, // null recommended for accessibility with screenreaders
                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryColor)
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
                if (selectedOption=="Sebagian") OutlinedTextField(
                    value = paidValue.toString(),
                    onValueChange = {
                        createTransaksiViewModel.paidValue.value = it.toIntOrNull()?:0
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardActions = KeyboardActions(onDone = {keyoboard?.hide()}),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        }
    }
}