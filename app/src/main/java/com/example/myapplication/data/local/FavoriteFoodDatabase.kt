package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.DB_VERSION
import com.example.myapplication.data.local.dao.PersonFoodDao
import com.example.myapplication.data.local.model.FoodEntity
import com.example.myapplication.data.local.model.PersonEntity
import com.example.myapplication.data.local.model.PersonFoodCrossRefEntity

@Database(
    entities = [PersonEntity::class, FoodEntity::class, PersonFoodCrossRefEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
abstract class FavoriteFoodDatabase: RoomDatabase() {
    abstract fun personFoodDao(): PersonFoodDao
}