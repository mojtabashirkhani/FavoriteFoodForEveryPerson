package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Food
import com.example.myapplication.domain.model.Person
import com.example.myapplication.domain.model.PersonFoodCrossRef
import com.example.myapplication.domain.model.PersonWithFoods
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