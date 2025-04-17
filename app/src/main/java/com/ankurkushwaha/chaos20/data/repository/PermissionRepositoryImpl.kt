package com.ankurkushwaha.chaos20.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.ankurkushwaha.chaos20.domain.repository.PermissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PermissionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PermissionRepository {

    /**
     * Checks if the app has the necessary permissions for music playback
     * @return Boolean indicating if all required permissions are granted
     */
    override fun hasRequiredPermissions(): Boolean {
        return hasStoragePermission() && hasNotificationPermission()
    }

    /**
     * Checks if the app has the necessary storage permissions
     * @return Boolean indicating if storage permissions are granted
     */
    override fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Checks if the app has notification permissions (required for Android 13+)
     * @return Boolean indicating if notification permissions are granted
     */
    override fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Notification permission not required for Android 12 and below
            true
        }
    }

    /**
     * Checks if the app has Bluetooth permissions
     * @return Boolean indicating if Bluetooth permissions are granted
     */
    override fun hasBluetoothPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Get list of permissions that need to be requested
     * @return Array of permission strings that need to be requested
     */
    override fun getRequiredPermissions(): Array<String> {
        val permissions = mutableListOf<String>()

        // Add storage permission based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasStoragePermission()) {
                permissions.add(Manifest.permission.READ_MEDIA_AUDIO)
            }
        } else {
            if (!hasStoragePermission()) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        // Add notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission()) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Add Bluetooth permission if needed
        if (!hasBluetoothPermission()) {
            permissions.add(Manifest.permission.BLUETOOTH)
        }

        return permissions.toTypedArray()
    }
}