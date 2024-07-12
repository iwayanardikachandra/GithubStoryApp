package com.example.appgithubusers.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.appgithubusers.api.ApiConfig
import com.example.appgithubusers.model.ItemsItem
import com.example.appgithubusers.model.ResponseUser
import com.example.appgithubusers.model.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    fun getThemeSetting(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    private val itemLiveData = MutableLiveData<List<ItemsItem>>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val apiService = ApiConfig.apiConnect

    val itemLive: LiveData<List<ItemsItem>> = itemLiveData
    val loadingLive: LiveData<Boolean> = loadingLiveData

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        fetchData("dicoding")
    }

    class MainFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(pref) as T
    }

    fun findUser(username: String) {
        loadingLiveData.value = true
        apiService.getUsers(username).enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                loadingLiveData.value = false
                if (response.isSuccessful) {
                    val items = response.body()?.items ?: emptyList()
                    itemLiveData.value = items.filterNotNull()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                loadingLiveData.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun fetchData(username: String) {
        findUser(username)
    }

    fun searchUser(username: String) {
        loadingLiveData.value = true
        apiService.getUsers(username).enqueue(object : Callback<ResponseUser> {
            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                loadingLiveData.value = false
                if (response.isSuccessful) {
                    val items = response.body()?.items ?: emptyList()
                    itemLiveData.value = items.filterNotNull()
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                loadingLiveData.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getSearchUser(): LiveData<List<ItemsItem>> = itemLive
}
