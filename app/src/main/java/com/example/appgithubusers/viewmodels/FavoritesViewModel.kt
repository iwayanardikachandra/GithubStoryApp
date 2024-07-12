package com.example.appgithubusers.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.appgithubusers.database.FavoriteDAO
import com.example.appgithubusers.database.FavoriteDB
import com.example.appgithubusers.model.UserFavorite

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: FavoriteDAO?
    private var userDb: FavoriteDB?

    init {
        userDb = FavoriteDB.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUsers(): LiveData<List<UserFavorite>>? {
        return userDao?.getFavorite()
    }
}