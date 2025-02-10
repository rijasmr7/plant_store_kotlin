package com.example.plantstore.screen

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun BatteryStatus() {
    val context = LocalContext.current
    var batteryLevel by remember { mutableStateOf(0f) }
    var isCharging by remember { mutableStateOf(false) }
    var temperature by remember { mutableStateOf(0f) }
    var hasShownLowBatteryWarning by remember { mutableStateOf(false) }
    var showLowBatteryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            val batteryInfo = getBatteryInfo(context)
            batteryLevel = batteryInfo.level
            isCharging = batteryInfo.isCharging
            temperature = batteryInfo.temperature

            if (batteryLevel <= 20f && !hasShownLowBatteryWarning && !isCharging) {
                Toast.makeText(
                    context,
                    "Low battery! Please connect your charger",
                    Toast.LENGTH_LONG
                ).show()
                showLowBatteryDialog = true
                hasShownLowBatteryWarning = true
            } else if (batteryLevel > 20f || isCharging) {
                hasShownLowBatteryWarning = false
                showLowBatteryDialog = false
            }
            
            delay(1000)
        }
    }

    //Alert the user when battery is low
    if (showLowBatteryDialog) {
        AlertDialog(
            onDismissRequest = { showLowBatteryDialog = false },
            title = { Text("Low Battery Warning") },
            text = { Text("Your battery level is below 20%. Please connect your charger to continue using the app.") },
            confirmButton = {
                Button(onClick = { showLowBatteryDialog = false }) {
                    Text("OK")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.error,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Battery Status",
            style = MaterialTheme.typography.headlineSmall
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LinearProgressIndicator(
            progress = batteryLevel / 100,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Battery Level: ${(batteryLevel).toInt()}%"
        )
        
        Text(
            text = "Charging Status: ${if (isCharging) "Charging" else "Not Charging"}"
        )
        
        Text(
            text = "Temperature: ${temperature/10}°C"
        )
    }
}

private data class BatteryInfo(
    val level: Float,
    val isCharging: Boolean,
    val temperature: Float
)

private fun getBatteryInfo(context: Context): BatteryInfo {
    val batteryIntent = context.registerReceiver(
        null,
        IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    )

    val level = batteryIntent?.let { intent ->
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        level * 100 / scale.toFloat()
    } ?: 0f

    val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
            status == BatteryManager.BATTERY_STATUS_FULL

    val temperature = batteryIntent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)?.toFloat() ?: 0f

    return BatteryInfo(level, isCharging, temperature)
} 