package com.example.mapapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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

private enum class MapLayerStyle(
    val label: String,
    val tileUrl: String,
    val attribution: String
) {
    STANDARD(
        label = "Standard",
        tileUrl = "https://tile.openstreetmap.org/{z}/{x}/{y}.png",
        attribution = "© OpenStreetMap contributors"
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
    )
}

private fun configureMapLibreHttpClient() {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header(
                    "User-Agent",
                    "MapAppAndroid/1.0 (https://github.com/example/mapapp; contact@mapapp.local)"
                )
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
        maxZoom = 19f
        attribution = layerStyle.attribution
    }
    return Style.Builder()
        .withSource(RasterSource("source-${layerStyle.name.lowercase()}", tileSet, 256))
        .withLayer(RasterLayer("layer-${layerStyle.name.lowercase()}", "source-${layerStyle.name.lowercase()}"))
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

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
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
                mapLoadError = message.ifBlank { "Unable to load map tiles. Check internet connection." }
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

    LaunchedEffect(mapLibreMap, uiState.selectedPlace, uiState.route) {
        val map = mapLibreMap ?: return@LaunchedEffect
        map.clear()

        userLatLng?.let { (uLat, uLng) ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(uLat, uLng))
                    .title("My Location")
            )
        }

        uiState.selectedPlace?.let { place ->
            val target = LatLng(place.latitude, place.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(target)
                    .title(place.name)
            )
            if (uiState.route == null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 12.0))
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

    Scaffold(
        floatingActionButton = {
            if (uiState.selectedPlace == null) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Layer Switcher FAB (Standard / Satellite / Terrain)
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

                    // Zoom In
                    SmallFloatingActionButton(
                        onClick = { mapLibreMap?.animateCamera(CameraUpdateFactory.zoomIn()) },
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Zoom In")
                    }

                    // Zoom Out
                    SmallFloatingActionButton(
                        onClick = { mapLibreMap?.animateCamera(CameraUpdateFactory.zoomOut()) },
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Zoom Out")
                    }

                    // My Location FAB
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

            // Loading / Error Overlay
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
                                if (selectedLayer != MapLayerStyle.TERRAIN) {
                                    Button(onClick = {
                                        selectedLayer = MapLayerStyle.TERRAIN
                                        mapLibreMap?.let { applyMapStyle(it, MapLayerStyle.TERRAIN) }
                                    }) {
                                        Text("Switch Source")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Top Google-Maps-Style Search Bar & Quick Chips
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
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("Search here") },
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
                                    modifier = Modifier.padding(end = 6.dp)
                                ) {
                                    Text(
                                        text = selectedLayer.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                if (uiState.searchQuery.isNotEmpty() || uiState.selectedPlace != null) {
                                    IconButton(onClick = {
                                        viewModel.onSearchQueryChanged("")
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Autocomplete Search Dropdown
                if (uiState.searchResults.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        shadowElevation = 6.dp,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    ) {
                        Column {
                            uiState.searchResults.forEach { result ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.selectPlace(result, userLatLng) }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Place,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(result.name, fontWeight = FontWeight.SemiBold)
                                        if (result.subtitle.isNotBlank()) {
                                            Text(
                                                result.subtitle,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Horizontal Chips: World + Cities
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = {
                            viewModel.clearSelection()
                            mapLibreMap?.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(WORLD_CENTER, WORLD_ZOOM)
                            )
                        },
                        label = { Text("World") }
                    )

                    viewModel.presetCities.forEach { city ->
                        AssistChip(
                            onClick = { viewModel.selectPlace(city, userLatLng) },
                            label = { Text(city.name) }
                        )
                    }
                }
            }

            // Bottom Place / Weather / Directions Sheet
            uiState.selectedPlace?.let { place ->
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
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = place.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = place.subtitle.ifBlank {
                                        String.format(Locale.US, "%.4f, %.4f", place.latitude, place.longitude)
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { viewModel.clearSelection() }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (uiState.isLoadingWeather) {
                            Text("Loading local weather...", style = MaterialTheme.typography.bodyMedium)
                        } else {
                            uiState.weather?.let { w ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${w.temperatureC}°C · ${w.description} · Wind ${w.windSpeedKmh} km/h",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

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
                            onClick = {
                                val origin = userLatLng ?: (37.7749 to -122.4194)
                                viewModel.fetchRoute(origin, place.latitude to place.longitude)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (uiState.isLoadingRoute) "Calculating Route..." else "Directions")
                        }

                        uiState.route?.let { r ->
                            val km = String.format(Locale.US, "%.1f km", r.distanceMeters / 1000.0)
                            val mins = "${(r.durationSeconds / 60.0).roundToInt()} min"
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "$mins ($km)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            r.steps.take(3).forEach { step ->
                                Text(
                                    text = "• $step",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }

                        uiState.statusMessage?.let { msg ->
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(top = 6.dp)
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
