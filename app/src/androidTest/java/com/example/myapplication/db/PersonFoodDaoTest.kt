package com.example.myapplication.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.database.FavoriteFoodDatabase
import com.example.myapplication.database.dao.PersonFoodDao
import com.example.myapplication.database.model.FoodEntity
import com.example.myapplication.database.model.PersonEntity
import com.example.myapplication.database.model.PersonFoodCrossRefEntity
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PersonFoodDaoTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FavoriteFoodDatabase
    private lateinit var personFoodDao: PersonFoodDao


    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FavoriteFoodDatabase::class.java
        ).allowMainThreadQueries().build()
        personFoodDao = database.personFoodDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testInsertPerson() = runTest {
        val person = PersonEntity(id = 1, name = "John Doe")
        val personId = personFoodDao.insertPerson(person)
        assertEquals(1L, personId)
    }

    @Test
    fun testInsertFood() = runTest {
        val food = FoodEntity(id = 1, name = "Apple")
        val foodId = personFoodDao.insertFood(food)
        assertEquals(1L, foodId)
    }

    @Test
    fun testInsertPersonFoodCrossRef() = runTest {
        val person = PersonEntity(id = 1, name = "John Doe")
        val food = FoodEntity(id = 1, name = "Apple")
        val crossRef = PersonFoodCrossRefEntity(personId = 1, foodId = 1)

        personFoodDao.insertPerson(person)
        personFoodDao.insertFood(food)
        personFoodDao.insertPersonFoodCrossRef(crossRef)

        val result = personFoodDao.getPersonWithFoods(1).first()
        assertEquals(1, result.size)
        assertEquals("John Doe", result[0].person.name)
        assertEquals(1, result[0].favoriteFoods.size)
        assertEquals("Apple", result[0].favoriteFoods[0].name)
    }

    @Test
    fun testInsertPersonWithFoods() = runTest {
        val person = PersonEntity(id = 1, name = "John Doe")
        val foods = listOf(FoodEntity(id = 1, name = "Apple"), FoodEntity(id = 2, name = "Banana"))

        personFoodDao.insertPersonWithFoods(person, foods)

        val result = personFoodDao.getPersonWithFoods(1).first()
        assertEquals(1, result.size)
        assertEquals("John Doe", result[0].person.name)
        assertEquals(2, result[0].favoriteFoods.size)
    }

    @Test
    fun testGetAllPersonsWithFoods() = runTest {
        val person1 = PersonEntity(id = 1, name = "John Doe")
        val person2 = PersonEntity(id = 2, name = "Jane Doe")
        val foods = listOf(FoodEntity(id = 1, name = "Apple"), FoodEntity(id = 2, name = "Banana"))

        personFoodDao.insertPersonWithFoods(person1, foods)
        personFoodDao.insertPersonWithFoods(person2, foods)

        val result = personFoodDao.getAllPersonsWithFoods().first()
        assertEquals(2, result.size)
    }

    @Test
    fun testGetAllFoods() = runTest {
        val foods = listOf(FoodEntity(id = 1, name = "Apple"), FoodEntity(id = 2, name = "Banana"))
        foods.forEach { personFoodDao.insertFood(it) }

        val result = personFoodDao.getAllFoods().first()
        assertEquals(2, result.size)
    }

    @Test
    fun testDeletePersonFoodCrossRef() = runTest {
        val person = PersonEntity(id = 1, name = "John Doe")
        val food = FoodEntity(id = 1, name = "Apple")
        val crossRef = PersonFoodCrossRefEntity(personId = 1, foodId = 1)

        personFoodDao.insertPerson(person)
        personFoodDao.insertFood(food)
        personFoodDao.insertPersonFoodCrossRef(crossRef)
        personFoodDao.deletePersonFoodCrossRef(crossRef)

        val result = personFoodDao.getPersonWithFoods(1).first()
        assertEquals(1, result.size)
    }

    @Test
    fun testDeletePersonFoodCrossRefById() = runTest {
        val person = PersonEntity(id = 1, name = "John Doe")
        val food = FoodEntity(id = 1, name = "Apple")
        val crossRef = PersonFoodCrossRefEntity(personId = 1, foodId = 1)

        personFoodDao.insertPerson(person)
        personFoodDao.insertFood(food)
        personFoodDao.insertPersonFoodCrossRef(crossRef)
        personFoodDao.deletePersonFoodCrossRefById(person.id, food.id)

        val result = personFoodDao.getPersonWithFoods(1).first()
        assertEquals(1, result.size)
    }

}