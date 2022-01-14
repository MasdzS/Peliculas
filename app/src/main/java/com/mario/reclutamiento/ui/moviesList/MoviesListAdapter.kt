package com.mario.reclutamiento.ui.moviesList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.util.DebugLogger
import com.mario.domain.models.ListWithMovies
import com.mario.domain.models.Movie
import com.mario.reclutamiento.R
import com.mario.reclutamiento.databinding.ItemMovieBinding
import mx.com.satoritech.web.APIConstants
import javax.inject.Inject

/**
 * Adapta las películas a un formato que el recycler view puede interpretar
 */
class MoviesListAdapter: RecyclerView.Adapter<MoviesListAdapter.moviesViewHoder>() {

    var onItemClick:(Movie)->Unit = {}
    var movieList: ListWithMovies? = null
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): moviesViewHoder {
        val inflater = LayoutInflater.from(parent.context)
        val vBind = ItemMovieBinding.inflate(inflater, parent, false)
        return moviesViewHoder(parent.context, vBind)
    }

    override fun onBindViewHolder(holder: moviesViewHoder, position: Int) {
        val item = movieList?.movies?.get(position)?: Movie()
        holder.onBind(item)
    }

    override fun getItemCount(): Int {
        return movieList?.movies?.size?:0
    }

    inner class moviesViewHoder(
        private val context: Context,
        private val vBind: ItemMovieBinding
    ):RecyclerView.ViewHolder(vBind.root){

        fun onBind(movie: Movie){
            vBind.tvMovieName.setText(movie.title)
            vBind.tvMoviePopularity.text = context.getString(R.string.popularity_field, (movie.popularity?:0.0).toString())
            vBind.tvMovieRate.text = context.getString(R.string.rate_field, (movie.voteAverage?:0.0).toString())
            vBind.ivMovieImage.load(APIConstants.imageServerPath+movie.backdropPath){
                this.crossfade(true)
            }
            vBind.root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }
}