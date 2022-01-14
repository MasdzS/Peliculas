package mx.com.satoritech.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mario.database.converters.DBTypeConverters
import com.mario.database.dao.MovieDao
import com.mario.database.dao.MovieListDao
import com.mario.domain.models.Movie
import com.mario.domain.models.MovieList
import com.mario.domain.models.MoviesListRef

@Database(entities = [Movie::class, MovieList::class, MoviesListRef::class], version = 1)
@TypeConverters(DBTypeConverters::class)
abstract class AppDB: RoomDatabase() {

    abstract fun movieDao():MovieDao

    abstract fun movieListDao():MovieListDao

    companion object{
        @JvmStatic
        fun newInstance(context:Context): AppDB {
            return Room.databaseBuilder(context,AppDB::class.java,"AppDB")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}