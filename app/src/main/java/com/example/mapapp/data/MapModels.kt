package com.example.mapapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class PlaceResult(
    val name: String,
    val subtitle: String,
    val latitude: Double,
    val longitude: Double
)

data class WeatherInfo(
    val temperatureC: Double,
    val windSpeedKmh: Double,
    val weatherCode: Int,
    val description: String
)

data class RouteSummary(
    val profile: String,
    val distanceMeters: Double,
    val durationSeconds: Double,
    val coordinates: List<Pair<Double, Double>>, // lat, lng
    val steps: List<String>
)

@Serializable
data class PhotonResponse(
    val features: List<PhotonFeature> = emptyList()
)

@Serializable
data class PhotonFeature(
    val geometry: PhotonGeometry,
    val properties: PhotonProperties
)

@Serializable
data class PhotonGeometry(
    val coordinates: List<Double> // [lng, lat]
)

@Serializable
data class PhotonProperties(
    val name: String? = null,
    val city: String? = null,
    val country: String? = null,
    val state: String? = null
)

@Serializable
data class OpenMeteoResponse(
    @SerialName("current_weather")
    val currentWeather: CurrentWeatherDto? = null
)

@Serializable
data class CurrentWeatherDto(
    val temperature: Double = 0.0,
    val windspeed: Double = 0.0,
    val weathercode: Int = 0
)

@Serializable
data class OsrmResponse(
    val routes: List<OsrmRoute> = emptyList()
)

@Serializable
data class OsrmRoute(
    val distance: Double,
    val duration: Double,
    val geometry: OsrmGeometry,
    val legs: List<OsrmLeg> = emptyList()
)

@Serializable
data class OsrmGeometry(
    val coordinates: List<List<Double>> // [lng, lat]
)

@Serializable
data class OsrmLeg(
    val steps: List<OsrmStep> = emptyList()
)

@Serializable
data class OsrmStep(
    val maneuver: OsrmManeuver? = null,
    val name: String? = null,
    val distance: Double = 0.0
)

@Serializable
data class OsrmManeuver(
    val instruction: String? = null,
    val type: String? = null
)
