package com.ankurkushwaha.chaos20.domain.repository

import android.content.Intent

interface PermissionRepository {
    fun hasRequiredPermissions(): Boolean
    fun hasStoragePermission(): Boolean
    fun hasExternalStoragePermission(): Boolean
    fun hasNotificationPermission(): Boolean
    fun hasBluetoothPermission(): Boolean
    fun getRequiredPermissions(): Array<String>
    fun needsManageExternalStoragePermission(): Boolean
    fun getManageExternalStorageIntent(): Intent?
}