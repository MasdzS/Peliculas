package mx.com.satoritech.web

import com.mario.domain.models.MovieList
import mx.com.satoritech.domain.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import java.util.*


interface ApiService {

    /**
     * Obtiene las películas mas populares
     * @param page Página de la lista a obtener
     * @param language Lenguaje del contenido que se solicita a la api
     * @param apiKey Llave para acceder a la api
     */
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("api_key") apiKey: String = APIConstants.apiKey
    ):Response<MovieList>

    /**
     * Obtiene las películas mejor puntuadas
     * @param page Página de la lista a obtener
     * @param language Lenguaje del contenido que se solicita a la api
     * @param apiKey Llave para acceder a la api
     */
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("api_key") apiKey: String = APIConstants.apiKey
    ):Response<MovieList>

    /**
     * Obtiene las películas próximas a salir
     * @param page Página de la lista a obtener
     * @param language Lenguaje del contenido que se solicita a la api
     * @param apiKey Llave para acceder a la api
     */
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("language") language: String = Locale.getDefault().toLanguageTag(),
        @Query("api_key") apiKey: String = APIConstants.apiKey
    ):Response<MovieList>



}