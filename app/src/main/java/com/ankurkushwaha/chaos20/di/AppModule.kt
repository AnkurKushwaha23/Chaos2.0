package com.ankurkushwaha.chaos20.di

import android.content.Context
import androidx.work.WorkManager
import com.ankurkushwaha.chaos20.data.repository.MusicDBRepositoryImpl
import com.ankurkushwaha.chaos20.data.repository.MusicRepositoryImpl
import com.ankurkushwaha.chaos20.data.repository.PermissionRepositoryImpl
import com.ankurkushwaha.chaos20.domain.repository.MusicDBRepository
import com.ankurkushwaha.chaos20.domain.repository.MusicRepository
import com.ankurkushwaha.chaos20.domain.repository.PermissionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMusicRepository(musicRepositoryImpl: MusicRepositoryImpl): MusicRepository {
        return musicRepositoryImpl
    }

    @Singleton
    @Provides
    fun providePermissionRepository(permissionRepositoryImpl: PermissionRepositoryImpl): PermissionRepository {
        return permissionRepositoryImpl
    }

    @Singleton
    @Provides
    fun provideMusicDBRepository(musicDBRepositoryImpl: MusicDBRepositoryImpl): MusicDBRepository {
        return musicDBRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}