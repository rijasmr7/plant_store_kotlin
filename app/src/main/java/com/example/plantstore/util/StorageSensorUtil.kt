package com.example.plantstore.util

import android.os.Environment
import android.os.StatFs

data class StorageInfo(
    val availableSpace: Long,
    val totalSpace: Long,
    val usedSpace: Long
)

fun getStorageInfo(): StorageInfo {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    
    val blockSize = stat.blockSizeLong
    val totalBlocks = stat.blockCountLong
    val availableBlocks = stat.availableBlocksLong
    
    val total = totalBlocks * blockSize
    val available = availableBlocks * blockSize
    val used = total - available
    
    return StorageInfo(
        availableSpace = available,
        totalSpace = total,
        usedSpace = used
    )
}

fun Long.toGigabytes(): Float {
    return this / (1024f * 1024f * 1024f)
} 