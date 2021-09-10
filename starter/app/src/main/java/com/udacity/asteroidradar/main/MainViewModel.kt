package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Api
import com.udacity.asteroidradar.api.AsteroidSelection
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

enum class NetworkStatus { LOADING, ERROR, DONE }
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application)
    private val repository = AsteroidRepository(database)

    private val _status = MutableLiveData<NetworkStatus>()
    val status: LiveData<NetworkStatus>
    get() = _status

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
    get() = _pictureOfDay

    private val _filter = MutableLiveData<AsteroidSelection>(AsteroidSelection.ALL)
    val filter: LiveData<AsteroidSelection>
    get() = _filter

    private val _shouldNavigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val shouldNavigateToAsteroidDetails: LiveData<Asteroid>
    get() = _shouldNavigateToAsteroidDetails

    fun navigateToAsteroidDetails(asteroid: Asteroid) {
        _shouldNavigateToAsteroidDetails.value = asteroid
    }

    fun finishedNavigatingToAsteroidDetails() {
        _shouldNavigateToAsteroidDetails.value = null
    }

    fun setFilter(asteroidSelection: AsteroidSelection) {
        _filter.value = asteroidSelection
    }

    val asteroids = repository.asteroids



    init {
        getPictureOfDay()
        getAsteroids()
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            _status.value = NetworkStatus.LOADING
            try {
                _pictureOfDay.value = Api.retrofitService.getPictureOfDay(API_KEY)
                _status.value = NetworkStatus.DONE
            } catch(e: HttpException) {
                _status.value = NetworkStatus.ERROR
            }
        }
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            _status.value = NetworkStatus.LOADING
            val repository = AsteroidRepository(database)
            try {
                repository.refreshAsteroids()
                _status.value = NetworkStatus.DONE
            } catch (e: HttpException) {
                _status.value = NetworkStatus.ERROR
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}