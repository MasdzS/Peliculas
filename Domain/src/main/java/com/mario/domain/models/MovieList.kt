package com.mario.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class MovieList(

    @PrimaryKey(autoGenerate = true)
    @Expose(serialize = false, deserialize = false)
    var listId: Long = 0,

    var page: Int? = null,

    @Expose(serialize = false, deserialize = false)
    var type: Int? = null,

    @Ignore
    @SerializedName("results")
    var movies: List<Movie>? = null,

    @SerializedName("total_pages")
    var totalPages: Int? = null,

    @SerializedName("total_results")
    var totalResults: Int? = null

):Parcelable{
    companion object{
        const val TYPE_POPULAR = 1
        const val TYPE_TOP_RATE = 2
        const val TYPE_UNCOMMING = 3
    }
}