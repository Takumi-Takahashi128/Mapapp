package com.example.mapapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapapp.data.NetworkModule
import com.example.mapapp.data.PlaceResult
import com.example.mapapp.data.RouteSummary
import com.example.mapapp.data.WeatherInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MapUiState(
    val searchQuery: String = "",
    val searchResults: List<PlaceResult> = emptyList(),
    val selectedPlace: PlaceResult? = null,
    val weather: WeatherInfo? = null,
    val route: RouteSummary? = null,
    val routingProfile: String = "driving",
    val isLoadingWeather: Boolean = false,
    val isLoadingRoute: Boolean = false,
    val statusMessage: String? = null
)

class MapViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    val presetCities = listOf(
        PlaceResult("Tokyo", "Japan", 35.6762, 139.6503),
        PlaceResult("New York", "United States", 40.7128, -74.0060),
        PlaceResult("London", "United Kingdom", 51.5072, -0.1276),
        PlaceResult("Paris", "France", 48.8566, 2.3522),
        PlaceResult("Seoul", "South Korea", 37.5665, 126.9780)
    )

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchJob?.cancel()
        if (query.trim().length < 2) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }
        searchJob = viewModelScope.launch {
            delay(350)
            try {
                val response = NetworkModule.photonApi.searchPlaces(query.trim())
                val mapped = response.features.mapNotNull { feature ->
                    val coords = feature.geometry.coordinates
                    if (coords.size < 2) return@mapNotNull null
                    val lng = coords[0]
                    val lat = coords[1]
                    val title = feature.properties.name
                        ?: feature.properties.city
                        ?: "Selected Location"
                    val sub = listOfNotNull(
                        feature.properties.city.takeIf { it != title },
                        feature.properties.state,
                        feature.properties.country
                    ).joinToString(", ")
                    PlaceResult(title, sub, lat, lng)
                }
                _uiState.value = _uiState.value.copy(searchResults = mapped)
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(statusMessage = "Search unavailable right now.")
            }
        }
    }

    fun selectPlace(place: PlaceResult, originLatLng: Pair<Double, Double>? = null) {
        _uiState.value = _uiState.value.copy(
            selectedPlace = place,
            searchResults = emptyList(),
            searchQuery = place.name,
            statusMessage = null
        )
        fetchWeather(place.latitude, place.longitude)
        if (originLatLng != null) {
            fetchRoute(originLatLng, place.latitude to place.longitude)
        }
    }

    fun inspectCoordinates(lat: Double, lng: Double) {
        val place = PlaceResult(
            name = "Pinned Location",
            subtitle = String.format("%.4f, %.4f", lat, lng),
            latitude = lat,
            longitude = lng
        )
        _uiState.value = _uiState.value.copy(
            selectedPlace = place,
            searchResults = emptyList(),
            statusMessage = null
        )
        fetchWeather(lat, lng)
    }

    fun setRoutingProfile(profile: String, originLatLng: Pair<Double, Double>?) {
        _uiState.value = _uiState.value.copy(routingProfile = profile)
        val dest = _uiState.value.selectedPlace
        if (dest != null && originLatLng != null) {
            fetchRoute(originLatLng, dest.latitude to dest.longitude, profile)
        }
    }

    fun fetchRoute(
        origin: Pair<Double, Double>,
        destination: Pair<Double, Double>,
        profile: String = _uiState.value.routingProfile
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingRoute = true, statusMessage = null)
            try {
                val coordParam = "${origin.second},${origin.first};${destination.second},${destination.first}"
                val response = NetworkModule.osrmApi.getRoute(profile = profile, coords = coordParam)
                val route = response.routes.firstOrNull()
                if (route != null) {
                    val latLngs = route.geometry.coordinates.mapNotNull { pair ->
                        if (pair.size >= 2) pair[1] to pair[0] else null
                    }
                    val steps = route.legs.firstOrNull()?.steps?.mapNotNull { step ->
                        val instr = step.maneuver?.instruction
                        if (!instr.isNullOrBlank()) instr
                        else if (!step.name.isNullOrBlank()) "Continue on ${step.name}"
                        else null
                    }?.take(8) ?: emptyList()

                    _uiState.value = _uiState.value.copy(
                        isLoadingRoute = false,
                        route = RouteSummary(
                            profile = profile,
                            distanceMeters = route.distance,
                            durationSeconds = route.duration,
                            coordinates = latLngs,
                            steps = steps
                        )
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoadingRoute = false,
                        route = null,
                        statusMessage = "No route found for this mode."
                    )
                }
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingRoute = false,
                    statusMessage = "Unable to calculate route."
                )
            }
        }
    }

    fun fetchWeather(lat: Double, lng: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingWeather = true)
            try {
                val response = NetworkModule.openMeteoApi.getCurrentWeather(lat, lng)
                val current = response.currentWeather
                if (current != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoadingWeather = false,
                        weather = WeatherInfo(
                            temperatureC = current.temperature,
                            windSpeedKmh = current.windspeed,
                            weatherCode = current.weathercode,
                            description = describeWeatherCode(current.weathercode)
                        )
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoadingWeather = false)
                }
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(isLoadingWeather = false)
            }
        }
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(
            selectedPlace = null,
            weather = null,
            route = null,
            statusMessage = null
        )
    }

    private fun describeWeatherCode(code: Int): String = when (code) {
        0 -> "Clear sky"
        1, 2, 3 -> "Partly cloudy"
        45, 48 -> "Foggy"
        51, 53, 55, 61, 63, 65 -> "Rain"
        71, 73, 75, 77 -> "Snow"
        80, 81, 82 -> "Rain showers"
        95, 96, 99 -> "Thunderstorm"
        else -> "Variable conditions"
    }
}
