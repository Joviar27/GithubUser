package com.example.githubuser

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel : ViewModel() {

    companion object{
        private const val DEFAULT_NAME = "A"
    }

    private val _userList = MutableLiveData<List<User>>()
    val userList : LiveData<List<User>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        getUserList(DEFAULT_NAME)
    }

    fun getUserList(searchQuery: String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getUserList(searchQuery)

        client.enqueue(object : Callback<UserListResponse>{
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userList.value = responseBody.items
                    }
                } else{
                    Log.e(TAG, "Error : ${response.message()}")
                }

            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Gagal : ${t.message.toString()}")
            }
        })
    }
}