package com.example.mapapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapapp.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices
import okhttp3.OkHttpClient
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.PolylineOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.module.http.HttpRequestUtil
import org.maplibre.android.style.layers.RasterLayer
import org.maplibre.android.style.sources.RasterSource
import org.maplibre.android.style.sources.TileSet
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

private val WORLD_CENTER = LatLng(20.0, 0.0)
private const val WORLD_ZOOM = 1.6

private data class WorldCityItem(
    val name: String,
    val country: String,
    val lat: Double,
    val lng: Double
)

private val WORLD_CITIES = listOf(
    WorldCityItem("Seoul", "South Korea", 37.5665, 126.9780),
    WorldCityItem("Busan", "South Korea", 35.1796, 129.0756),
    WorldCityItem("Tokyo", "Japan", 35.6762, 139.6503),
    WorldCityItem("Osaka", "Japan", 34.6937, 135.5023),
    WorldCityItem("Beijing", "China", 39.9042, 116.4074),
    WorldCityItem("Shanghai", "China", 31.2304, 121.4737),
    WorldCityItem("Singapore", "Singapore", 1.3521, 103.8198),
    WorldCityItem("Bangkok", "Thailand", 13.7563, 100.5018),
    WorldCityItem("New Delhi", "India", 28.6139, 77.2090),
    WorldCityItem("Mumbai", "India", 19.0760, 72.8777),
    WorldCityItem("Dubai", "UAE", 25.2048, 55.2708),
    WorldCityItem("Istanbul", "Turkey", 41.0082, 28.9784),
    WorldCityItem("London", "United Kingdom", 51.5072, -0.1276),
    WorldCityItem("Paris", "France", 48.8566, 2.3522),
    WorldCityItem("Berlin", "Germany", 52.5200, 13.4050),
    WorldCityItem("Rome", "Italy", 41.9028, 12.4964),
    WorldCityItem("Madrid", "Spain", 40.4168, -3.7038),
    WorldCityItem("New York", "United States", 40.7128, -74.0060),
    WorldCityItem("Los Angeles", "United States", 34.0522, -118.2437),
    WorldCityItem("San Francisco", "United States", 37.7749, -122.4194),
    WorldCityItem("Chicago", "United States", 41.8781, -87.6298),
    WorldCityItem("Toronto", "Canada", 43.6532, -79.3832),
    WorldCityItem("Mexico City", "Mexico", 19.4326, -99.1332),
    WorldCityItem("Sao Paulo", "Brazil", -23.5505, -46.6333),
    WorldCityItem("Buenos Aires", "Argentina", -34.6037, -58.3816),
    WorldCityItem("Cairo", "Egypt", 30.0444, 31.2357),
    WorldCityItem("Nairobi", "Kenya", -1.2921, 36.8219),
    WorldCityItem("Sydney", "Australia", -33.8688, 151.2093),
    WorldCityItem("Melbourne", "Australia", -37.8136, 144.9631),
    WorldCityItem("Auckland", "New Zealand", -36.8485, 174.7633)
)

private enum class MapLayerStyle(
    val label: String,
    val tileUrl: String,
    val attribution: String
) {
    STANDARD(
        label = "Standard",
        tileUrl = "https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}",
        attribution = "Tiles © Esri"
    ),
    SATELLITE(
        label = "Satellite",
        tileUrl = "https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}",
        attribution = "Tiles © Esri"
    ),
    TERRAIN(
        label = "Terrain",
        tileUrl = "https://server.arcgisonline.com/ArcGIS/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}",
        attribution = "Tiles © Esri"
    ),
    OSM(
        label = "OSM",
        tileUrl = "https://tile.openstreetmap.org/{z}/{x}/{y}.png",
        attribution = "© OpenStreetMap contributors"
    )
}

private fun configureMapLibreHttpClient() {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) MapApp/1.0")
                .header("Accept", "image/png,image/jpeg,image/*,*/*;q=0.8")
                .build()
            chain.proceed(request)
        }
        .build()
    HttpRequestUtil.setOkHttpClient(okHttpClient)
}

private fun buildRasterStyleBuilder(layerStyle: MapLayerStyle): Style.Builder {
    val tileSet = TileSet("tileset-${layerStyle.name.lowercase()}", layerStyle.tileUrl).apply {
        minZoom = 0f
        maxZoom = 18f
        attribution = layerStyle.attribution
    }
    return Style.Builder()
        .withSource(RasterSource("source-${layerStyle.name.lowercase()}", tileSet, 256))
        .withLayer(RasterLayer("layer-${layerStyle.name.lowercase()}", "source-${layerStyle.name.lowercase()}"))
}

private fun parseCoordinatesOrCity(input: String): Pair<Double, Double>? {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return null

    // Match "lat, lng"
    val parts = trimmed.split(",").map { it.trim() }
    if (parts.size == 2) {
        val lat = parts[0].toDoubleOrNull()
        val lng = parts[1].toDoubleOrNull()
        if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
            return lat to lng
        }
    }

    // Match world city name
    val matchedCity = WORLD_CITIES.firstOrNull {
        it.name.equals(trimmed, ignoreCase = true) ||
            "${it.name}, ${it.country}".equals(trimmed, ignoreCase = true)
    }
    return matchedCity?.let { it.lat to it.lng }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()

    remember {
        MapLibre.getInstance(context)
        configureMapLibreHttpClient()
        true
    }

    var mapLibreMap by remember { mutableStateOf<MapLibreMap?>(null) }
    var userLatLng by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var isMapLoading by remember { mutableStateOf(true) }
    var mapLoadError by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }
    var selectedLayer by remember { mutableStateOf(MapLayerStyle.STANDARD) }
    var hasInitializedCamera by remember { mutableStateOf(false) }

    // Search city dropdown state
    var citySearchText by remember { mutableStateOf("") }
    var isCityDropdownOpen by remember { mutableStateOf(false) }

    // From -> To routing panel state
    var isFromToExpanded by remember { mutableStateOf(true) }
    var fromText by remember { mutableStateOf("Seoul") }
    var toText by remember { mutableStateOf("Tokyo") }
    var customRoutePoints by remember { mutableStateOf<List<Pair<Double, Double>>>(emptyList()) }
    var customFromLatLng by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var customToLatLng by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var directionMessage by remember { mutableStateOf<String?>(null) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val filteredWorldCities = remember(citySearchText) {
        val q = citySearchText.trim()
        if (q.isEmpty()) {
            WORLD_CITIES
        } else {
            WORLD_CITIES.filter {
                it.name.contains(q, ignoreCase = true) ||
                    it.country.contains(q, ignoreCase = true)
            }
        }
    }

    fun applyMapStyle(map: MapLibreMap, layer: MapLayerStyle, resetToWorld: Boolean = false) {
        isMapLoading = true
        mapLoadError = null

        val styleBuilder = buildRasterStyleBuilder(layer)
        map.setStyle(styleBuilder) {
            mapLibreMap = map
            isMapLoading = false
            mapLoadError = null
            map.uiSettings.apply {
                setAllGesturesEnabled(true)
                isCompassEnabled = true
                isLogoEnabled = false
                isAttributionEnabled = true
            }

            if (resetToWorld || !hasInitializedCamera) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(WORLD_CENTER, WORLD_ZOOM))
                hasInitializedCamera = true
            }

            map.addOnMapClickListener { latLng ->
                viewModel.inspectCoordinates(latLng.latitude, latLng.longitude)
                true
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = granted
        if (granted) {
            fetchLastLocation(context) { lat, lng ->
                userLatLng = lat to lng
            }
        }
    }

    LaunchedEffect(Unit) {
        if (hasLocationPermission) {
            fetchLastLocation(context) { lat, lng ->
                userLatLng = lat to lng
            }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val mapView = remember {
        MapView(context).apply {
            addOnWillStartLoadingMapListener {
                isMapLoading = true
                mapLoadError = null
            }
            addOnDidFinishLoadingMapListener {
                isMapLoading = false
                mapLoadError = null
            }
            addOnDidFailLoadingMapListener { message ->
                isMapLoading = false
                mapLoadError = message.ifBlank { "Unable to load map tiles." }
            }
            getMapAsync { map ->
                mapLibreMap = map
                applyMapStyle(map, selectedLayer, resetToWorld = true)
            }
        }
    }

    LaunchedEffect(reloadKey) {
        if (reloadKey > 0) {
            mapLibreMap?.let { map ->
                applyMapStyle(map, selectedLayer, resetToWorld = false)
            }
        }
    }

    LaunchedEffect(mapLibreMap, uiState.selectedPlace, uiState.route, customRoutePoints, customFromLatLng, customToLatLng) {
        val map = mapLibreMap ?: return@LaunchedEffect
        map.clear()

        userLatLng?.let { (uLat, uLng) ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(uLat, uLng))
                    .title("My Location")
            )
        }

        customFromLatLng?.let { (fLat, fLng) ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(fLat, fLng))
                    .title("From: ${fromText.ifBlank { "Start" }}")
            )
        }

        customToLatLng?.let { (tLat, tLng) ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(tLat, tLng))
                    .title("To: ${toText.ifBlank { "Destination" }}")
            )
        }

        if (customRoutePoints.size >= 2) {
            val linePoints = customRoutePoints.map { (lat, lng) -> LatLng(lat, lng) }
            map.addPolyline(
                PolylineOptions()
                    .addAll(linePoints)
                    .width(7f)
                    .color(android.graphics.Color.parseColor("#1A73E8"))
            )
        }

        uiState.selectedPlace?.let { place ->
            val target = LatLng(place.latitude, place.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(target)
                    .title(place.name)
            )
            if (uiState.route == null && customRoutePoints.isEmpty()) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 10.0))
            }
        }

        uiState.route?.let { route ->
            if (route.coordinates.isNotEmpty()) {
                val points = route.coordinates.map { (lat, lng) -> LatLng(lat, lng) }
                map.addPolyline(
                    PolylineOptions()
                        .addAll(points)
                        .width(6f)
                        .color(android.graphics.Color.parseColor("#1A73E8"))
                )
            }
        }
    }

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    fun executeFromToDirection() {
        val resolvedFrom = parseCoordinatesOrCity(fromText)
            ?: if (fromText.equals("My Location", ignoreCase = true)) userLatLng else null
        val resolvedTo = parseCoordinatesOrCity(toText)
            ?: if (toText.equals("My Location", ignoreCase = true)) userLatLng else null

        if (resolvedFrom == null || resolvedTo == null) {
            directionMessage = "Enter valid world cities (e.g. Seoul, Tokyo, London, New York) or 'lat, lng'."
            return
        }

        directionMessage = null
        customFromLatLng = resolvedFrom
        customToLatLng = resolvedTo
        customRoutePoints = listOf(resolvedFrom, resolvedTo)
        viewModel.fetchRoute(resolvedFrom, resolvedTo)

        val midLat = (resolvedFrom.first + resolvedTo.first) / 2.0
        val midLng = (resolvedFrom.second + resolvedTo.second) / 2.0
        mapLibreMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(midLat, midLng), 2.5))
    }

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        val entries = MapLayerStyle.entries
                        val next = entries[(selectedLayer.ordinal + 1) % entries.size]
                        selectedLayer = next
                        mapLibreMap?.let { applyMapStyle(it, next, resetToWorld = false) }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Icon(Icons.Default.Layers, contentDescription = "Switch Map Layer")
                }

                SmallFloatingActionButton(
                    onClick = { mapLibreMap?.animateCamera(CameraUpdateFactory.zoomIn()) },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Zoom In")
                }

                SmallFloatingActionButton(
                    onClick = { mapLibreMap?.animateCamera(CameraUpdateFactory.zoomOut()) },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
                }

                FloatingActionButton(
                    onClick = {
                        if (hasLocationPermission) {
                            fetchLastLocation(context) { lat, lng ->
                                userLatLng = lat to lng
                                mapLibreMap?.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 14.0)
                                )
                            }
                        } else {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "My Location")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
            )

            // Map Loading / Error Overlay
            if (isMapLoading || mapLoadError != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        if (isMapLoading) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Loading ${selectedLayer.label} map...",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        } else if (mapLoadError != null) {
                            Text(
                                text = "Map could not load",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = mapLoadError ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { reloadKey++ }) {
                                    Text("Retry")
                                }
                                Button(onClick = {
                                    val entries = MapLayerStyle.entries
                                    val next = entries[(selectedLayer.ordinal + 1) % entries.size]
                                    selectedLayer = next
                                    mapLibreMap?.let { applyMapStyle(it, next) }
                                }) {
                                    Text("Next Map Source")
                                }
                            }
                        }
                    }
                }
            }

            // Top Area: Search City Input -> Weather Strip (replacing city buttons) -> World City Dropdown
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .align(Alignment.TopCenter)
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    shadowElevation = 6.dp,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = citySearchText,
                        onValueChange = { typed ->
                            citySearchText = typed
                            viewModel.onSearchQueryChanged(typed)
                            isCityDropdownOpen = true
                        },
                        placeholder = { Text("Search city around the world...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier
                                        .padding(end = 4.dp)
                                        .clickable {
                                            viewModel.clearSelection()
                                            customRoutePoints = emptyList()
                                            customFromLatLng = null
                                            customToLatLng = null
                                            mapLibreMap?.animateCamera(
                                                CameraUpdateFactory.newLatLngZoom(WORLD_CENTER, WORLD_ZOOM)
                                            )
                                        }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Public,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "World",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                                if (citySearchText.isNotEmpty() || uiState.selectedPlace != null || isCityDropdownOpen) {
                                    IconButton(onClick = {
                                        citySearchText = ""
                                        viewModel.onSearchQueryChanged("")
                                        isCityDropdownOpen = false
                                        viewModel.clearSelection()
                                    }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear")
                                    }
                                }
                            }
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusable()
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    isCityDropdownOpen = true
                                }
                            }
                    )
                }

                // Weather Strip positioned right where the city buttons were
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.WbSunny,
                                contentDescription = "Weather",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            when {
                                uiState.isLoadingWeather -> {
                                    Text(
                                        text = "Loading weather...",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                uiState.weather != null -> {
                                    val w = uiState.weather!!
                                    val placeTitle = uiState.selectedPlace?.name ?: "Selected Spot"
                                    Text(
                                        text = "$placeTitle: ${w.temperatureC}°C · ${w.description} · Wind ${w.windSpeedKmh} km/h",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                else -> {
                                    Text(
                                        text = "Weather: Select a world city or tap anywhere on the map",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // World Cities Dropdown List when Search City Input is clicked/typed
                if (isCityDropdownOpen) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 8.dp,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        LazyColumn {
                            items(filteredWorldCities) { city ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            citySearchText = "${city.name}, ${city.country}"
                                            toText = city.name
                                            isCityDropdownOpen = false
                                            viewModel.inspectCoordinates(city.lat, city.lng)
                                            mapLibreMap?.animateCamera(
                                                CameraUpdateFactory.newLatLngZoom(
                                                    LatLng(city.lat, city.lng),
                                                    10.0
                                                )
                                            )
                                        }
                                        .padding(horizontal = 16.dp, vertical = 11.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Place,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = city.name,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = city.country,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = String.format(Locale.US, "%.1f, %.1f", city.lat, city.lng),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                            }
                        }
                    }
                }
            }

            // Bottom Panel: "From To" Inputs + "Direction" Button + Route Summary
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                shadowElevation = 12.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isFromToExpanded = !isFromToExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Directions,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "From To",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    val temp = fromText
                                    fromText = toText
                                    toText = temp
                                }
                            ) {
                                Icon(Icons.Default.SwapVert, contentDescription = "Swap From and To")
                            }
                            Icon(
                                imageVector = if (isFromToExpanded) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                                contentDescription = if (isFromToExpanded) "Collapse" else "Expand"
                            )
                        }
                    }

                    if (isFromToExpanded) {
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = fromText,
                            onValueChange = { fromText = it },
                            label = { Text("From") },
                            placeholder = { Text("e.g. Seoul, London, or 37.5, 127.0") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = toText,
                            onValueChange = { toText = it },
                            label = { Text("To") },
                            placeholder = { Text("e.g. Tokyo, New York, or 35.6, 139.6") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("driving" to "Drive", "walking" to "Walk", "cycling" to "Bike").forEach { (mode, label) ->
                                FilterChip(
                                    selected = uiState.routingProfile == mode,
                                    onClick = { viewModel.setRoutingProfile(mode, userLatLng) },
                                    label = { Text(label) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { executeFromToDirection() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (uiState.isLoadingRoute) "Calculating Direction..." else "Direction")
                        }

                        directionMessage?.let { msg ->
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        uiState.statusMessage?.let { msg ->
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        uiState.route?.let { r ->
                            val km = String.format(Locale.US, "%.1f km", r.distanceMeters / 1000.0)
                            val mins = "${(r.durationSeconds / 60.0).roundToInt()} min"
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$fromText → $toText  ·  $mins ($km)",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun fetchLastLocation(
    context: Context,
    onLocation: (Double, Double) -> Unit
) {
    val client = LocationServices.getFusedLocationProviderClient(context)
    client.lastLocation.addOnSuccessListener { loc ->
        if (loc != null) {
            onLocation(loc.latitude, loc.longitude)
        }
    }
}
