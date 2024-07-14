package com.example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.DB_VERSION
import com.example.myapplication.database.dao.PersonFoodDao
import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonEntity
import com.example.myapplication.database.model.PersonFoodCrossRef

@Database(
    entities = [PersonEntity::class, FoodEntity::class, PersonFoodCrossRef::class],
    version = DB_VERSION,
    exportSchema = false
)
abstract class FavoriteFoodDatabase: RoomDatabase() {
    abstract fun personFoodDao(): PersonFoodDao
}