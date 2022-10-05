package com.rich.movielistapi.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rich.movielistapi.response.GetUserResponseItem
import com.rich.movielistapi.response.UserResponse
import com.rich.movielistapi.service.APIConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    var livedataGetUser : MutableLiveData<List<GetUserResponseItem>?>
    var livedataRegisterUser : MutableLiveData<UserResponse?>
    var livedataGetUserById : MutableLiveData<GetUserResponseItem?>
    var livedataUpdateUser : MutableLiveData<UserResponse?>

    init {
        livedataGetUser = MutableLiveData()
        livedataRegisterUser = MutableLiveData()
        livedataGetUserById = MutableLiveData()
        livedataUpdateUser = MutableLiveData()
    }

    fun observerLDGetUser() : MutableLiveData<List<GetUserResponseItem>?> {
        return livedataGetUser
    }

    fun observerLDRegisterUser() : MutableLiveData<UserResponse?> {
        return livedataRegisterUser
    }

    fun observerLDGetUserById() : MutableLiveData<GetUserResponseItem?> {
        return livedataGetUserById
    }

    fun observerLDUpdateUser() : MutableLiveData<UserResponse?> {
        return livedataUpdateUser
    }

    fun callGetAllUser(){
        val client = APIConfig.getUserService().getAllUser()
        client.enqueue(object : Callback<List<GetUserResponseItem>>{
            override fun onResponse(
                call: Call<List<GetUserResponseItem>>,
                response: Response<List<GetUserResponseItem>>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null){
                        livedataGetUser.postValue(data)
                    }else{
                        livedataGetUser.postValue(null)
                    }
                }else{
                    Log.d("VM Error : ", response.message())
                    livedataGetUser.postValue(null)
                }
            }

            override fun onFailure(call: Call<List<GetUserResponseItem>>, t: Throwable) {
                Log.d("VM Error : ", t.message!!)
                livedataGetUser.postValue(null)
            }

        })
    }

    fun callRegisterUser(email : String, username : String, password : String){
        val client = APIConfig.getUserService().registerUser(email, username, password)
        client.enqueue(object : Callback<UserResponse>{
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null){
                        livedataRegisterUser.postValue(data)
                    }else{
                        livedataRegisterUser.postValue(null)
                    }
                }else{
                    Log.d("VM Error : ", response.message())
                    livedataRegisterUser.postValue(null)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("VM Error : ", t.message!!)
                livedataRegisterUser.postValue(null)
            }

        })
    }

    fun callGetUserById(id : String){
        val client = APIConfig.getUserService().getUserById(id)
        client.enqueue(object : Callback<GetUserResponseItem>{
            override fun onResponse(
                call: Call<GetUserResponseItem>,
                response: Response<GetUserResponseItem>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null){
                        livedataGetUserById.postValue(data)
                    }else{
                        livedataGetUserById.postValue(null)
                    }
                }else{
                    Log.d("VM Error : ", response.message())
                    livedataGetUserById.postValue(null)
                }
            }

            override fun onFailure(call: Call<GetUserResponseItem>, t: Throwable) {
                Log.d("VM Error : ", t.message!!)
                livedataGetUserById.postValue(null)
            }

        })
    }

    fun callUpdateUser(id : String, email : String, username : String, password : String){
        val client = APIConfig.getUserService().updateUser(id, email, username, password)
        client.enqueue(object : Callback<UserResponse>{
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if(response.isSuccessful){
                    val data = response.body()
                    if(data != null){
                        livedataUpdateUser.postValue(data)
                    }else{
                        livedataUpdateUser.postValue(null)
                    }
                }else{
                    Log.d("VM Error : ", response.message())
                    livedataUpdateUser.postValue(null)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("VM Error : ", t.message!!)
                livedataUpdateUser.postValue(null)
            }

        })
    }
}