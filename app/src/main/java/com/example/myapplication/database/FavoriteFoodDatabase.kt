package com.example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.DB_VERSION
import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonEntity

@Database(
    entities = [PersonEntity::class, FoodEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
abstract class FavoriteFoodDatabase: RoomDatabase() {

}