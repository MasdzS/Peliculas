package com.mario.domain.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(primaryKeys = ["movieId","listId"])
@Parcelize
data class MoviesListRef(
    val movieId:Long = 0,
    val listId:Long = 0
):Parcelable
