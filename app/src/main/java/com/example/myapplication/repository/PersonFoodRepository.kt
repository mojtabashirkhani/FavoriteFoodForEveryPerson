package com.example.myapplication.repository

import com.example.myapplication.database.dao.PersonFoodDao
import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonEntity
import com.example.myapplication.database.model.PersonFoodCrossRef
import javax.inject.Inject

class PersonFoodRepository @Inject constructor(private val personFoodDao: PersonFoodDao) {
    suspend fun insertPerson(person: PersonEntity) = personFoodDao.insertPerson(person)

    suspend fun insertFood(food: FoodEntity) = personFoodDao.insertFood(food)

    fun getAllPersonsWithFoods() = personFoodDao.getAllPersonsWithFoods()

    fun getAllFoods() = personFoodDao.getAllFoods()

    suspend fun insertPersonFoodCrossRef(crossRef: PersonFoodCrossRef) = personFoodDao.insertPersonFoodCrossRef(crossRef)

    suspend fun deletePersonFoodCrossRef(crossRef: PersonFoodCrossRef) {
        personFoodDao.deletePersonFoodCrossRef(crossRef)
    }

    suspend fun deletePersonFoodCrossRefById(personId: Long, foodId: Long) {
        personFoodDao.deletePersonFoodCrossRefById(personId, foodId)
    }
}