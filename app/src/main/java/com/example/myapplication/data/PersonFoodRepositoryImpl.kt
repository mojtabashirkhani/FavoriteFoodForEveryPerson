package com.example.myapplication.data

import com.example.myapplication.database.dao.PersonFoodDao
import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonEntity
import com.example.myapplication.database.model.PersonFoodCrossRefEntity
import com.example.myapplication.domain.Food
import com.example.myapplication.domain.Person
import com.example.myapplication.domain.PersonFoodCrossRef
import com.example.myapplication.domain.PersonFoodRepository
import com.example.myapplication.domain.PersonWithFoods
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PersonFoodRepositoryImpl @Inject constructor(private val personFoodDao: PersonFoodDao): PersonFoodRepository {
    override suspend fun insertPerson(person: Person) {
        val personEntity = PersonEntity(name = person.name)
        personFoodDao.insertPerson(personEntity)
    }

    override suspend fun insertFood(food: Food) {
        val foodEntity = FoodEntity(name = food.name)
        personFoodDao.insertFood(foodEntity)
    }

    override fun getAllPersonsWithFoods(): Flow<List<PersonWithFoods>> {
        return personFoodDao.getAllPersonsWithFoods().map { entities ->
            entities.map { entity ->
                PersonWithFoods(
                    person = Person(entity.person.id, entity.person.name),
                    foods = entity.favoriteFoods.map { Food(it.id, it.name) }
                )
            }
        }    }

    override fun getAllFoods(): Flow<List<Food>> {
        return personFoodDao.getAllFoods().map { foods ->
            foods.map { Food(it.id ,it.name) }
        }
    }

    override suspend fun insertPersonFoodCrossRef(crossRef: PersonFoodCrossRef) {
        personFoodDao.insertPersonFoodCrossRef(
            PersonFoodCrossRefEntity(
                personId = crossRef.personId,
                foodId = crossRef.foodId
            )
        )
    }

    override suspend fun deletePersonFoodCrossRefById(personId: Long, foodId: Long) {
        personFoodDao.deletePersonFoodCrossRefById(personId, foodId)
    }
}