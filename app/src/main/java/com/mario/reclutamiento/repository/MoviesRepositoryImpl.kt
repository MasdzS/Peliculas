package com.mario.reclutamiento.repository

import com.mario.domain.models.ListWithMovies
import com.mario.domain.models.Movie
import com.mario.domain.models.MovieList
import com.mario.domain.models.MoviesListRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import mx.com.satoritech.database.AppDB
import mx.com.satoritech.web.ApiService
import mx.com.satoritech.web.BaseApiResponse
import mx.com.satoritech.web.NetworkResult
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    val db: AppDB,
    val apiService: ApiService,
) : MoviesRepository, BaseApiResponse() {
    override fun getList(type: Int, page: Int): Flow<ListWithMovies?> {
        return flow {
            this.emit(db.movieListDao().getList(type, page))
        }
    }

    override fun updateListbyApi(type: Int, page: Int): Flow<NetworkResult<MovieList>> {
        return safeApiCall{
            when(type){
                MovieList.TYPE_POPULAR -> apiService.getPopularMovies(page)
                MovieList.TYPE_TOP_RATE -> apiService.getTopRatedMovies(page)
                MovieList.TYPE_UNCOMMING -> apiService.getUpcomingMovies(page)
                else -> {
                    apiService.getPopularMovies(page)
                }
            }
        }.filter {
            it.data?.let { movieList ->
                //delete list
                db.movieListDao().getLists(type,page).forEach {
                    db.movieListDao().cleanList(it.listId?:0)
                    db.movieListDao().deleteList(it)
                }
                //insert new list
                db.movieListDao().insert(movieList.apply {
                    this.type = type
                })
                //fill list
                db.movieDao().insert(movieList.movies?: listOf())
                val listId = db.movieListDao().getList(type, page)?.list?.listId?:0
                val movieListRefs = movieList.movies?.map { movie ->
                    MoviesListRef(
                        listId = listId,
                        movieId =  movie.movieId
                    )
                }
                db.movieListDao().addMovies(movieListRefs?: listOf())
            }
            return@filter true
        }
    }

    override fun getMovie(movieId: Long): Flow<Movie?> {
        return flow {
            emit(db.movieDao().getMovie(movieId))
        }
    }
}