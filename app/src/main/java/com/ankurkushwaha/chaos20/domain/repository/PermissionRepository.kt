package com.ankurkushwaha.chaos20.domain.repository

interface PermissionRepository {
    fun hasRequiredPermissions(): Boolean
    fun hasStoragePermission(): Boolean
    fun hasNotificationPermission(): Boolean
    fun hasBluetoothPermission(): Boolean
    fun getRequiredPermissions(): Array<String>
}