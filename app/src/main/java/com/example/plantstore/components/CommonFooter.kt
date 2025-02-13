package com.example.plantstore.components

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.BatteryManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.plantstore.screen.BatteryStatus
import com.example.plantstore.screen.BrightnessStatus
import com.example.plantstore.util.NetworkConnectivity
import kotlinx.coroutines.delay

@Composable
fun CommonFooter() {
    var isExpanded by remember { mutableStateOf(false) }
    var showLowBatteryDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var isOnline by remember { mutableStateOf(NetworkConnectivity.isNetworkAvailable(context)) }
    var showTemporaryStatus by remember { mutableStateOf(false) }
    var previousOnlineState by remember { mutableStateOf(isOnline) }
    
    LaunchedEffect(Unit) {
        while(true) {
            val newOnlineState = NetworkConnectivity.isNetworkAvailable(context)
            if (newOnlineState != previousOnlineState) {
                isOnline = newOnlineState
                previousOnlineState = newOnlineState
                showTemporaryStatus = true
                delay(6000)
                showTemporaryStatus = false
            }
            delay(1000)
        }
    }

    Column {
        if (showLowBatteryDialog && !isExpanded) {
            AlertDialog(
                onDismissRequest = { showLowBatteryDialog = false },
                title = { Text("Low Battery Warning") },
                text = { Text("Your device's battery is running low. Please connect to a charger.") },
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

        if (showTemporaryStatus) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isOnline) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isOnline) Icons.Default.Check
                                 else Icons.Default.Close,
                    contentDescription = "Network Status",
                    tint = if (isOnline) MaterialTheme.colorScheme.primary
                          else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (isOnline) "Online" else "Offline",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isOnline) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp)
        ) {
            if (isExpanded) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isOnline) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                            )
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isOnline) Icons.Default.Check
                                         else Icons.Default.Close,
                            contentDescription = "Network Status",
                            tint = if (isOnline) MaterialTheme.colorScheme.primary
                                  else MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isOnline) "Online" else "Offline",
                                style = MaterialTheme.typography.titleSmall,
                                color = if (isOnline) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = if (isOnline) "Connected to network"
                                       else "No network connection",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    BatteryStatus()
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    BrightnessStatus()
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))
                    TemperatureStatus()
                }
            } else {
                CompactSensorStatus(
                    onLowBattery = { showLowBatteryDialog = true }
                )
            }
        }
    }
}

@Composable
private fun CompactSensorStatus(
    onLowBattery: () -> Unit
) {
    val context = LocalContext.current
    var batteryLevel by remember { mutableStateOf(0f) }
    var isCharging by remember { mutableStateOf(false) }
    var lightLevel by remember { mutableStateOf(0f) }
    var hasShownLowBatteryWarning by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_LIGHT) {
                    lightLevel = event.values[0]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        lightSensor?.let {
            sensorManager.registerListener(
                sensorListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        while (true) {
            val batteryInfo = getBatteryInfo(context)
            batteryLevel = batteryInfo.level
            isCharging = batteryInfo.isCharging

            if (batteryLevel <= 20f && !hasShownLowBatteryWarning && !isCharging) {
                Toast.makeText(
                    context,
                    "Low battery! Please connect your charger",
                    Toast.LENGTH_LONG
                ).show()
                onLowBattery()
                hasShownLowBatteryWarning = true
            } else if (batteryLevel > 20f || isCharging) {
                hasShownLowBatteryWarning = false
            }

            if (lightSensor == null) lightLevel = 940f
            
            delay(1000)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Battery: ${batteryLevel.toInt()}% ${if (isCharging) "⚡" else ""}"
            )
            Text(
                text = "Light: ${String.format("%.0f", lightLevel)} lux"
            )
        }
        Text(
            text = "Tap for details",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun TemperatureStatus() {
    val context = LocalContext.current
    var ambientTemp by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        
        val tempSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    ambientTemp = event.values[0]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        
        temperatureSensor?.let {
            sensorManager.registerListener(
                tempSensorListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Temperature Status",
            style = MaterialTheme.typography.headlineSmall
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (ambientTemp != 0f) {
            Text(
                text = "Ambient Temperature: ${String.format("%.1f", ambientTemp)}°C",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = when {
                    ambientTemp < 18 -> "Cold Environment"
                    ambientTemp < 25 -> "Comfortable Temperature"
                    else -> "Warm Environment"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    ambientTemp < 18 -> MaterialTheme.colorScheme.primary
                    ambientTemp < 25 -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.error
                }
            )
        } else {
            Text(
                text = "Temperature sensor not available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
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