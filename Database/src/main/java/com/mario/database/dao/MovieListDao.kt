package com.mario.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mario.domain.models.ListWithMovies
import com.mario.domain.models.MovieList
import com.mario.domain.models.MoviesListRef

@Dao
interface MovieListDao {

    /**
     * Almacena la información de la lista de peliculas
     * @param Lista de peliculas
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieList:MovieList)

    /**
     * Obtiene una lista de peliculas
     * @param type tipo de la lista (populares, mejor puntuados, proximamente)
     * @param page página de la lista
     */
    @Transaction
    @Query("SELECT * FROM MovieList WHERE type = :type AND page = :page LIMIT 1")
    suspend fun getList(type:Int, page:Int):ListWithMovies?

    /**
     * Obtiene información de listas de peliculas que coincidan con el tipo y la página
     * @param type tipo de la lista (populares, mejor puntuados, proximamente)
     * @param page página de la lista
     */
    @Query("SELECT * FROM MovieList WHERE type = :type AND page = :page")
    suspend fun getLists(type:Int, page:Int):List<MovieList>

    /**
     * Limpia las peliculas de una lista sin eliminar las peliculas
     */
    @Query("DELETE FROM MoviesListRef WHERE listId == :listId")
    suspend fun cleanList(listId:Long)

    /**
     * Elimina la información de una lista de peliculas
     */
    @Query("DELETE FROM MovieList WHERE type == :type AND page == :page")
    suspend fun deleteList(type:Int, page: Int)

    /**
     * Elimina La informacion de una lista de películas
     */
    @Delete
    suspend fun deleteList(movieList: MovieList)

    /**
     * Añade peliculas a una lista
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(moviesListRef: List<MoviesListRef>)

}