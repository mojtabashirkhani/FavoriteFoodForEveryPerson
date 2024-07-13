package com.example.myapplication.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Food")
data class FoodEntity(
    @PrimaryKey
    @ColumnInfo("id") val id: Long = 0L,
    @ColumnInfo("name") val name: String = "",
)
