package com.example.appgithubusers.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appgithubusers.api.ApiConfig
import com.example.appgithubusers.database.FavoriteDAO
import com.example.appgithubusers.database.FavoriteDB
import com.example.appgithubusers.model.ResponseDetail
import com.example.appgithubusers.model.UserFavorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(app: Application) : AndroidViewModel(app) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _user = MutableLiveData<ResponseDetail>()
    val user: LiveData<ResponseDetail> get() = _user

    private val userDao: FavoriteDAO?
    private val appContext: Application = app

    init {
        userDao = FavoriteDB.getDatabase(app)?.favoriteUserDao()
    }

    fun detailUser(username: String) {
        _isLoading.value = true
        ApiConfig.apiConnect.getUsersDetail(username)
            .enqueue(object : Callback<ResponseDetail> {
                override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure : ${t.message.toString()}")
                }

                override fun onResponse(
                    call: Call<ResponseDetail>,
                    response: Response<ResponseDetail>,
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _user.value = response.body()
                    }
                }
            })
    }

    fun addToFavorite(id: Int, username: String, avatarURL: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserFavorite(id, username, avatarURL)
            userDao?.addToFavorite(user)
            showToast("Added to favorites")
        }
    }

    suspend fun checkForFavorite(id: Int) = userDao?.checkFavorite(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFavorite(id)
            showToast("Removed from favorites")
        }
    }

    private suspend fun showToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}
