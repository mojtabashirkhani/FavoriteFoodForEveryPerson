package com.example.myapplication.domain

import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonEntity
import com.example.myapplication.database.model.PersonFoodCrossRef
import com.example.myapplication.repository.PersonFoodRepository
import javax.inject.Inject

class AddPersonUseCase @Inject constructor(private val repository: PersonFoodRepository) {
    suspend operator fun invoke(person: PersonEntity) = repository.insertPerson(person)
}

class AddFoodUseCase @Inject constructor(private val repository: PersonFoodRepository) {
    suspend operator fun invoke(food: FoodEntity) = repository.insertFood(food)
}

class GetAllPersonsWithFoodsUseCase @Inject constructor(private val repository: PersonFoodRepository) {
    suspend operator fun invoke() = repository.getAllPersonsWithFoods()
}

class GetAllFoodsUseCase @Inject constructor(private val repository: PersonFoodRepository) {
    suspend operator fun invoke() = repository.getAllFoods()
}

class AddFavoriteFoodUseCase @Inject constructor(private val repository: PersonFoodRepository) {
    suspend operator fun invoke(personId: Long, foodId: Long) =
        repository.insertPersonFoodCrossRef(PersonFoodCrossRef(personId, foodId))
}

class DeleteFavoriteFoodUseCase @Inject constructor(private val repository: PersonFoodRepository) {
    suspend operator fun invoke(personId: Long, foodId: Long) = repository.deletePersonFoodCrossRefById(personId, foodId)
}