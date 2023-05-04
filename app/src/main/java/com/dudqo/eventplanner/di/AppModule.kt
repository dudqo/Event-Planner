package com.dudqo.eventplanner.di

import android.app.Application
import androidx.room.Room
import com.dudqo.eventplanner.domain.repository.AuthRepository
import com.dudqo.eventplanner.data.AuthRepositoryImpl
import com.dudqo.eventplanner.data.EventDatabase
import com.dudqo.eventplanner.data.EventRepositoryImpl
import com.dudqo.eventplanner.domain.repository.EventRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideEventDatabase(app: Application): EventDatabase {
        return Room.databaseBuilder(
            app,
            EventDatabase::class.java,
            "Events.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideEventRepository(db: EventDatabase): EventRepository {
        return EventRepositoryImpl(db.dao)
    }

    @Singleton
    @Provides
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }
}

