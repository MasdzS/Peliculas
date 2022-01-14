package com.mario.reclutamiento.ui.movieDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.mario.domain.models.Movie
import com.mario.reclutamiento.R
import com.mario.reclutamiento.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import mx.com.satoritech.web.APIConstants


/**
 * Muestra los detalles de la película seleccionada
 */
@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var vBind: FragmentMovieDetailsBinding
    private val viewModel:MovieDetailsViewModel by viewModels()
    private val args: MovieDetailsFragmentArgs by navArgs()
    private var movieId:Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vBind = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        initData()
        initUpdates()
        return vBind.root
    }

    /**
     * Obtiene el id de la película enviado por la pantalla anterior
     */
    private fun initData(){
        movieId = args.movieId
    }

    /**
     * Configura la comunicación con el viewModel permitiendo obtener los datos de la pelicula
     */
    private fun initUpdates(){
        viewModel.getMovie(movieId)
        viewModel.movie.observe(viewLifecycleOwner){
            showMovie(it?: Movie())
        }
    }

    /**
     * Muestra los datos de la película
     * @param Datos de la película a mostrar
     */
    private fun showMovie(movie:Movie){
        vBind.ivMovieImage.load(APIConstants.imageServerPath + movie.backdropPath){
            this.crossfade(true)
        }
        vBind.tvMovieName.text = movie.title?:""
        vBind.tvMovieOriginalName.text = movie.originalTitle?:""
        vBind.tvMovieOverview.text = movie.overview?:""
        vBind.tvMovieReleaseDate.text = getString(R.string.release_date_field,movie.releaseDate?:"")
        vBind.tvMovieOriginalLanguage.text = getString(R.string.original_language_field, movie.originalLanguage)
        vBind.tvMovieRate.text = getString(R.string.rate_field,(movie.voteAverage?:0.0).toString())
        vBind.tvMoviePopularity.text = getString(R.string.popularity_field, (movie.popularity?:0.0)?:"")
    }

}