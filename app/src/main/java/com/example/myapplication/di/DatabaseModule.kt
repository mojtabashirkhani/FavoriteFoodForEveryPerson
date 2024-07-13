package com.example.myapplication.di

import android.app.Application
import androidx.room.Room
import com.example.myapplication.DB_NAME
import com.example.myapplication.database.FavoriteFoodDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(application: Application): FavoriteFoodDatabase {
        return Room
            .databaseBuilder(application, FavoriteFoodDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    /*@Provides
    fun provideProductDao(db: ProductDatabase): ProductDao {
        return db.productDao()
    }*/
}