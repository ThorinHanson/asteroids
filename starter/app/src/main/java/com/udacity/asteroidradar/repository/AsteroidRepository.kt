package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.Api
import com.udacity.asteroidradar.api.getSevenDaysFromToday
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDatabaseDao.getAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val today = getToday()
                val nextWeek = getSevenDaysFromToday()
                println("====================${today} - ${nextWeek}====================")
                val asteroidsResponse = Api.retrofitService.getAsteroids(today, nextWeek, API_KEY)
                val asteroids = parseAsteroidsJsonResult(JSONObject(asteroidsResponse))
                database.asteroidDatabaseDao.insertAll(*asteroids.asDatabaseAsteroid())
            } catch (e: Exception) {
                println("Error getting asteroids - ${e.message}")
            }
        }
    }
}