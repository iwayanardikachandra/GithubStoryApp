package com.example.appgithubusers.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "userFavorite")
data class UserFavorite(
    @PrimaryKey
    val id: Int,
    val login: String,
    val avatarURL: String
) : Serializable


