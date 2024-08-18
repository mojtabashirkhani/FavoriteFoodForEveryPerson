package com.example.myapplication.database.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "PersonFoodCrossRef",
    primaryKeys = ["personId", "foodId"],
    foreignKeys = [
        ForeignKey(entity = PersonEntity::class, parentColumns = ["id"], childColumns = ["personId"]),
        ForeignKey(entity = FoodEntity::class, parentColumns = ["id"], childColumns = ["foodId"])
    ]
)
data class PersonFoodCrossRefEntity(
    val personId: Long,
    val foodId: Long
)
