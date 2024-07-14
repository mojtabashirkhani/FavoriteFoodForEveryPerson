package com.example.myapplication.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonEntity
import com.example.myapplication.database.model.PersonFoodCrossRef
import com.example.myapplication.database.model.PersonWithFoods

@Dao
interface PersonFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonFoodCrossRef(crossRef: PersonFoodCrossRef)

    @Transaction
    suspend fun insertPersonWithFoods(person: PersonEntity, foods: List<FoodEntity>) {
        val personId = insertPerson(person)
        foods.forEach { food ->
            val foodId = insertFood(food)
            insertPersonFoodCrossRef(PersonFoodCrossRef(personId = personId, foodId = foodId))
        }
    }

    @Transaction
    @Query("SELECT * FROM Person WHERE id = :personId")
    suspend fun getPersonWithFoods(personId: Long): List<PersonWithFoods>

    @Transaction
    @Query("SELECT * FROM Person")
    suspend fun getAllPersonsWithFoods(): List<PersonWithFoods>

    @Query("SELECT * FROM Food")
    suspend fun getAllFoods(): List<FoodEntity>

    @Delete
    suspend fun deletePersonFoodCrossRef(crossRef: PersonFoodCrossRef)

    @Query("DELETE FROM PersonFoodCrossRef WHERE personId = :personId AND foodId = :foodId")
    suspend fun deletePersonFoodCrossRefById(personId: Long, foodId: Long)
}