package com.rich.movielistapi.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rich.movielistapi.fragment.MovieListFragment
import com.rich.movielistapi.response.MovieResult
import com.rich.movielistapi.response.PopularMovieResponse
import com.rich.movielistapi.service.APIConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {
    lateinit var liveDataMovie : MutableLiveData<List<MovieResult>>

    init {
        liveDataMovie = MutableLiveData()
    }

    fun getLDMovie() : MutableLiveData<List<MovieResult>> = liveDataMovie

    fun callGetPopularMovieApi() {
        val client = APIConfig.getMovieService().getPopularMovies()
        client.enqueue(object : Callback<PopularMovieResponse> {
            override fun onResponse(
                call: Call<PopularMovieResponse>,
                response: Response<PopularMovieResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        liveDataMovie.postValue(data.results as List<MovieResult>?)
                    }
                } else {
                    Log.e("Error : ", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PopularMovieResponse>, t: Throwable) {
                Log.e("Error ; ", "onFailure: ${t.message}")
            }
        })
    }
}