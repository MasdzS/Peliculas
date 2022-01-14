package mx.com.satoritech.appRecoleccionDeBasuraMetepec.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mx.com.satoritech.database.AppDB
import javax.inject.Singleton


/**
 * Crea y provee una instancia de room para todo el proyecto
 */
@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    /**
     * Provee una instancia de room
     */
    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext
        context: Context
    ):AppDB{
        return AppDB.newInstance(context)
    }
}