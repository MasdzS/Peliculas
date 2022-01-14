package com.mario.domain.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListWithMovies(
    @Embedded
    val list: MovieList,
    @Relation(
        parentColumn = "listId",
        entityColumn = "movieId",
        associateBy = Junction(MoviesListRef::class)
    )
    val movies:List<Movie>
):Parcelable
