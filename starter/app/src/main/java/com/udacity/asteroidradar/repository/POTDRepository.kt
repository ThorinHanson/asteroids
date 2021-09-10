package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class POTDRepository {
    /*
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
     */
    suspend fun getPictureOfTheDay() : PictureOfDay? {
        var pictureOfDay: PictureOfDay? = null

        withContext(Dispatchers.IO) {
            try {
                pictureOfDay = Api.retrofitService.getPictureOfDay(API_KEY)
            } catch (e: Exception) {
                println("Exception :: ${e.message} ::")
            }
            null
        }
        return pictureOfDay
    }
}