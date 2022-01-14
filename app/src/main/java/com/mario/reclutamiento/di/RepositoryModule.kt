package mx.com.satoritech.appRecoleccionDeBasuraMetepec.di

import com.mario.reclutamiento.repository.LocationRepository
import com.mario.reclutamiento.repository.LocationRepositoryImpl
import com.mario.reclutamiento.repository.MoviesRepository
import com.mario.reclutamiento.repository.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    @Binds
    abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}