package com.example.myapplication.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PersonWithFoods(
    @Embedded val person: PersonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(PersonFoodCrossRefEntity::class, parentColumn = "personId", entityColumn = "foodId")
    )
    val favoriteFoods: List<FoodEntity>
)
