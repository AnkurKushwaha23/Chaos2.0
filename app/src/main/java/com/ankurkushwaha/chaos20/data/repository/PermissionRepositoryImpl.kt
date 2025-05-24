package com.ankurkushwaha.chaos20.data.repository

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
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
     * Checks if the app has external storage write permissions
     * @return Boolean indicating if external storage write permissions are granted
     */
    override fun hasExternalStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11+ (API 30+), check MANAGE_EXTERNAL_STORAGE
            Environment.isExternalStorageManager()
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            // For Android 9 and below (API 28 and below), check WRITE_EXTERNAL_STORAGE
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // For Android 10 (API 29), scoped storage is default, no special permission needed
            true
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

        // Add external storage write permission based on Android version
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (!hasExternalStoragePermission()) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
        // Note: MANAGE_EXTERNAL_STORAGE requires special intent, not regular permission request

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

    /**
     * Checks if MANAGE_EXTERNAL_STORAGE permission is needed and not granted
     * @return Boolean indicating if MANAGE_EXTERNAL_STORAGE intent should be launched
     */
    override fun needsManageExternalStoragePermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()
    }

    /**
     * Creates intent to request MANAGE_EXTERNAL_STORAGE permission
     * @return Intent to launch manage external storage settings
     */
    override fun getManageExternalStorageIntent(): Intent? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.parse("package:${context.packageName}")
            }
        } else {
            null
        }
    }
}