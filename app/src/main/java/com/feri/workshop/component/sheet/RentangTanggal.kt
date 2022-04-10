package com.feri.workshop.component.sheet

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.feri.workshop.ui.helper.spacerV
import com.feri.workshop.utils.toDateorNull
import com.feri.workshop.utils.toFormattedString
import java.util.*

object RentangTanggal : Sheets {
    override val routeName = "rentangtanggal"

    @ExperimentalComposeUiApi
    @Composable
    override fun screen(navController: NavHostController) {
        val context = LocalContext.current
        val rentangTanggalViewModel = getMainActivity().rentangViewModel
        val startDate by remember { rentangTanggalViewModel.startDate }
        var endDate by remember { rentangTanggalViewModel.endDate }
        val datePickerDialog = DatePickerDialog(context)

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
            Text(text = "Pilih Rentang Tanggal", fontSize = 16.sp, fontWeight = FontWeight.W600)
            spacerV(height = 12.dp)
            Text(text = "Tanggal mulai : ")
            spacerV(height = 8.dp)
            OutlinedTextField(
                value = startDate.toFormattedString("dd MMM yyyy"),
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            val calendar = Calendar.getInstance()
                            calendar.time = startDate
                            datePickerDialog.datePicker.init(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ) { _, year, month, dayOfMonth ->
                                val cal = Calendar.getInstance()
                                cal.set(year, month, dayOfMonth)
                                cal.set(Calendar.HOUR, 0)
                                cal.set(Calendar.MINUTE, 0)
                                rentangTanggalViewModel.startDate.value = cal.time
                            }
                            datePickerDialog.show()
                        }) {
                        Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "")
                    }
                })
            spacerV(height = 12.dp)
            Text(text = "Tanggal Selesai : ")
            spacerV(height = 8.dp)
            OutlinedTextField(
                value = endDate.toFormattedString("dd MMM yyyy"),
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            val calendar = Calendar.getInstance()
                            calendar.time = endDate
                            datePickerDialog.datePicker.init(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ) { _, year, month, dayOfMonth ->
                                val cal = Calendar.getInstance()
                                cal.set(year, month, dayOfMonth)
                                cal.set(Calendar.HOUR, 23)
                                cal.set(Calendar.MINUTE, 59)
                                rentangTanggalViewModel.endDate.value = cal.time
                            }
                            datePickerDialog.show()
                        }) {
                        Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "")
                    }
                })
            spacerV(height = 16.dp)
            Button(
                onClick = {
                    rentangTanggalViewModel.onFilter.value = true
                    navController.navigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(text = "Simpan")
            }
            spacerV(height = 16.dp)
        }
    }

}
