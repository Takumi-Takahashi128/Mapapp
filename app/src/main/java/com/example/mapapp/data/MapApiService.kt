package com.example.mapapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotonApi {
    @GET("api")
    suspend fun searchPlaces(
        @Query("q") query: String,
        @Query("limit") limit: Int = 6
    ): PhotonResponse
}

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true
    ): OpenMeteoResponse
}

interface OsrmApi {
    @GET("route/v1/{profile}/{coords}")
    suspend fun getRoute(
        @Path("profile") profile: String,
        @Path("coords", encoded = true) coords: String,
        @Query("overview") overview: String = "full",
        @Query("geometries") geometries: String = "geojson",
        @Query("steps") steps: Boolean = true
    ): OsrmResponse
}

object NetworkModule {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val photonApi: PhotonApi by lazy {
        createRetrofit("https://photon.komoot.io/").create(PhotonApi::class.java)
    }

    val openMeteoApi: OpenMeteoApi by lazy {
        createRetrofit("https://api.open-meteo.com/").create(OpenMeteoApi::class.java)
    }

    val osrmApi: OsrmApi by lazy {
        createRetrofit("https://router.project-osrm.org/").create(OsrmApi::class.java)
    }
}
