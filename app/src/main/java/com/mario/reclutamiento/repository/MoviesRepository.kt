package com.mario.reclutamiento.repository

import com.mario.domain.models.ListWithMovies
import com.mario.domain.models.Movie
import com.mario.domain.models.MovieList
import kotlinx.coroutines.flow.Flow
import mx.com.satoritech.web.NetworkResult

interface MoviesRepository {
    /**
     * Get movies list from Database
     * @param type Type of list (popular, top rate or uncoming)
     * @param page Page of the list to show
    **/
    fun getList(type:Int, page:Int):Flow<ListWithMovies?>

    /**
     * Update database movies by api
     * @param type Type of list (popular, top rate or uncoming)
     * @param page Page of the list to show
     **/
    fun updateListbyApi(type:Int, page:Int):Flow<NetworkResult<MovieList>>

    /**
     * Get a movie from DB by id
     * @param movieId Id of movie to get
     **/
    fun getMovie(movieId:Long):Flow<Movie?>
}