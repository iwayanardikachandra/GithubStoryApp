package com.example.appgithubusers.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appgithubusers.model.UserFavorite

@Dao
interface FavoriteDAO {

    @Query("SELECT * FROM userFavorite")
    fun getFavorite(): LiveData<List<UserFavorite>>

    @Query("SELECT COUNT(*) FROM userFavorite WHERE userFavorite.id = :id")
    suspend fun checkFavorite(id: Int): Int

    @Insert
    suspend fun addToFavorite(userFavorite: UserFavorite)

    @Query("DELETE FROM userFavorite WHERE userFavorite.id = :id")
    suspend fun removeFavorite(id: Int): Int

}