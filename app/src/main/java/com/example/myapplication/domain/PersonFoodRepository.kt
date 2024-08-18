package com.example.myapplication.domain

import com.example.myapplication.database.model.PersonFoodCrossRefEntity
import kotlinx.coroutines.flow.Flow

interface PersonFoodRepository  {
    suspend fun insertPerson(person: Person)

    suspend fun insertFood(food: Food)

    fun getAllPersonsWithFoods(): Flow<List<PersonWithFoods>>
    fun getAllFoods(): Flow<List<Food>>
    suspend fun insertPersonFoodCrossRef(crossRef: PersonFoodCrossRef)
    suspend fun deletePersonFoodCrossRefById(personId: Long, foodId: Long)

   /* fun getAllPersonsWithFoods() = personFoodDao.getAllPersonsWithFoods()

    fun getAllFoods() = personFoodDao.getAllFoods()

    suspend fun insertPersonFoodCrossRef(crossRef: PersonFoodCrossRef) = personFoodDao.insertPersonFoodCrossRef(crossRef)

    suspend fun deletePersonFoodCrossRefById(personId: Long, foodId: Long) {
        personFoodDao.deletePersonFoodCrossRefById(personId, foodId)
    }*/
}