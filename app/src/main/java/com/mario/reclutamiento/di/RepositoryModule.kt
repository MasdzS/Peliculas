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

/**
 * Provee Objetos de
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     *  Une la interfaz moviesRepository con una implementación
     *  @param moviesRepositoryImpl Implementación de MoviesRepository
     *  @return Objeto de MoviesRepository
     */
    @Binds
    abstract fun bindMovieRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    /**
     *  Une la interfaz LocationRespository con una implementación
     *  @param locationRepositoryImpl Implementación de LocationRespository
     *  @return Objeto de LocationRepository
     */
    @Binds
    abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}