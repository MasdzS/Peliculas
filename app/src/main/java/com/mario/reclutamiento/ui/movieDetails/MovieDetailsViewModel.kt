package com.mario.reclutamiento.ui.movieDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mario.domain.models.Movie
import com.mario.reclutamiento.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
): ViewModel() {
    private val _movie = MutableLiveData<Movie>()

    val movie:LiveData<Movie> = _movie

    /**
     * Obtiene los datos de la pelicula desde el repositorio
     * @param movieId Id de la pelicula
     */
    fun getMovie(movieId:Long) = viewModelScope.launch(Dispatchers.IO){
        moviesRepository.getMovie(movieId).collect{
            _movie.postValue(it)
        }
    }
}