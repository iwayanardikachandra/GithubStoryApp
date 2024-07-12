package com.example.appgithubusers.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.appgithubusers.model.UserFavorite

@Database(entities = [UserFavorite::class], version = 1)
abstract class FavoriteDB : RoomDatabase() {

    abstract fun favoriteUserDao(): FavoriteDAO

    companion object {
        private var INSTANCES: FavoriteDB? = null

        fun getDatabase(context: Context): FavoriteDB? {
            if (INSTANCES == null) {
                synchronized(FavoriteDB::class) {
                    INSTANCES = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteDB::class.java,
                        "user database"
                    ).build()
                }
            }
            return INSTANCES
        }
    }

}