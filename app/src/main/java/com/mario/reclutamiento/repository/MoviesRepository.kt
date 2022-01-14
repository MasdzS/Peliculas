package com.mario.reclutamiento.repository

import com.mario.domain.models.ListWithMovies
import com.mario.domain.models.Movie
import com.mario.domain.models.MovieList
import kotlinx.coroutines.flow.Flow
import mx.com.satoritech.web.NetworkResult

interface MoviesRepository {

    fun getList(type:Int, page:Int):Flow<ListWithMovies?>
    fun updateListbyApi(type:Int, page:Int):Flow<NetworkResult<MovieList>>
    fun getMovie(movieId:Long):Flow<Movie?>
}