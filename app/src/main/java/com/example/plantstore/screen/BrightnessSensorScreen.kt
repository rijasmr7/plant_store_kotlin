package com.example.plantstore.screen

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun BrightnessStatus() {
    val context = LocalContext.current
    var lightLevel by remember { mutableStateOf(0f) }
    
    LaunchedEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        
        if (lightSensor == null) {
            //if there is no sensor this is the default value
            lightLevel = 940f
        } else {
            val lightSensorListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    if (event.sensor.type == Sensor.TYPE_LIGHT) {
                        lightLevel = event.values[0]
                    }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }
            
            sensorManager.registerListener(
                lightSensorListener,
                lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Brightness Status",
            style = MaterialTheme.typography.headlineSmall
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = (lightLevel / 1000f).coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Light Level: ${String.format("%.1f", lightLevel)} lux (${String.format("%.0f", (lightLevel/1000f * 100))}%)"
        )
        
        Text(
            text = when {
                lightLevel < 100 -> "Low Light"
                lightLevel < 500 -> "Normal Light"
                else -> "Bright Light"
            }
        )
    }
} 