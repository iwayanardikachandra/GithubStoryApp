package com.example.appgithubusers.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appgithubusers.api.ApiConfig
import com.example.appgithubusers.model.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val listFollowing = MutableLiveData<List<ItemsItem>>()

    fun fetchFollowing (username : String){
        _isLoading.value = true
        ApiConfig.apiConnect
            .getFollowing(username)
            .enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "OnFailure : ${t.message.toString()}")
                }
            })
    }

    fun getListFollowing() : LiveData<List<ItemsItem>> {
        return listFollowing
    }

    companion object{
        private const val TAG = "Following ViewModel"
    }

}


