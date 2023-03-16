package com.example.githubuser

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _userList = MutableLiveData<List<User>?>()
    val userList : MutableLiveData<List<User>?> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun getFollow(user : String, type : Int){
        _isLoading.value = true

        val client=  when(type){
            1 -> ApiConfig.getApiService().getFollowers(user)
            2 -> ApiConfig.getApiService().getFollowing(user)
            else -> {
                ApiConfig.getApiService().getFollowers(user)
            }
        }

        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    Log.d(TAG, "Response Body : $responseBody")
                    if (responseBody != null) {
                        _userList.value = responseBody
                    }
                } else{
                    Log.e(TAG, "Error : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Gagal : ${t.message.toString()}")
            }
        })
    }
}