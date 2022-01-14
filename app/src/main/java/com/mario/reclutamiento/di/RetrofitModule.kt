package mx.com.satoritech.appRecoleccionDeBasuraMetepec.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mario.web.serializers.BooleanDeserializer
import com.mario.web.serializers.BooleanSerializer
import com.mario.web.serializers.DateDeserializaer
import com.mario.web.serializers.DateSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mx.com.satoritech.web.APIConstants
import mx.com.satoritech.web.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Se encarga de crear y proveer una instancia de retrofit
 */
@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    /**
     * Provee una instancia de retrofit para todo el proyecto
     */
    @Singleton
    @Provides
    fun provideRetrofit(): ApiService{
        return Retrofit.Builder()
            .baseUrl(APIConstants.serverPath)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson()))
            .client(provideHttpClient())
            .build()
            .create(ApiService::class.java)
    }

    /**
     * Añade un logginInterceptor el cual se encarga de mostrar los datos obtenidos
     * por retrofit en el log
     */
    private fun loggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor

    }

    /**
     * Genera una instancia de OkHttpClient el cual va actuar de cliente para las peticiones
     * http
     */
    private fun provideHttpClient(): OkHttpClient{
        return OkHttpClient
            .Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor())
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + APIConstants.token)
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    /**
     * Genera una instancia de Gson el cual se encarga de serializar y deserializar el contenido
     * de las consultas a la api
     */
    private fun gson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, BooleanSerializer())
            .registerTypeAdapter(Boolean::class.java, BooleanDeserializer())
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanSerializer())
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanDeserializer())
            .registerTypeAdapter(Date::class.java, DateSerializer())
            .registerTypeAdapter(Date::class.java, DateDeserializaer())
            .create()
    }
}