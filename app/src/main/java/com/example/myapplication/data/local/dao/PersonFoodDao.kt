package com.example.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.data.local.model.FoodEntity
import com.example.myapplication.data.local.model.PersonEntity
import com.example.myapplication.data.local.model.PersonFoodCrossRefEntity
import com.example.myapplication.data.local.model.PersonWithFoods
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonFoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: FoodEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonFoodCrossRef(crossRef: PersonFoodCrossRefEntity)

    @Transaction
    suspend fun insertPersonWithFoods(person: PersonEntity, foods: List<FoodEntity>) {
        val personId = insertPerson(person)
        foods.forEach { food ->
            val foodId = insertFood(food)
            insertPersonFoodCrossRef(PersonFoodCrossRefEntity(personId = personId, foodId = foodId))
        }
    }

    @Transaction
    @Query("SELECT * FROM Person WHERE id = :personId")
    fun getPersonWithFoods(personId: Long): Flow<List<PersonWithFoods>>

    @Transaction
    @Query("SELECT * FROM Person")
    fun getAllPersonsWithFoods(): Flow<List<PersonWithFoods>>

    @Query("SELECT * FROM Food")
    fun getAllFoods(): Flow<List<FoodEntity>>

    @Delete
    suspend fun deletePersonFoodCrossRef(crossRef: PersonFoodCrossRefEntity)

    @Query("DELETE FROM PersonFoodCrossRef WHERE personId = :personId AND foodId = :foodId")
    suspend fun deletePersonFoodCrossRefById(personId: Long, foodId: Long)
}