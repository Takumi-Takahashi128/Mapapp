
package com.example.mapapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.DirectionsBoat
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.NearMe
import androidx.compose.material.icons.rounded.Payment
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapapp.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.annotations.PolylineOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.module.http.HttpRequestUtil
import org.maplibre.android.style.layers.RasterLayer
import org.maplibre.android.style.sources.RasterSource
import org.maplibre.android.style.sources.TileSet
import java.net.URLEncoder
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

private val BrandBlue = Color(0xFF2563EB)
private val BrandBlueDark = Color(0xFF1E3A8A)
private val BrandBlueSoft = Color(0xFFEFF6FF)
private val OriginGreen = Color(0xFF059669)
private val RouteRed = Color(0xFFDC2626)
private val RouteRedSoft = Color(0xFFFEF2F2)
private val AdAmberBg = Color(0xFFFEF9EC)
private val AdAmberBorder = Color(0xFFFDE68A)
private val AdAmberAccent = Color(0xFFD97706)
private val CardSurface = Color(0xFFFCFDFF)
private val SubtleBg = Color(0xFFF1F5F9)
private val BorderSubtle = Color(0xFFE2E8F0)
private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF64748B)

private const val REMOVE_ADS_PRICE = "$0.99"

private val WORLD_CENTER = LatLng(22.0, 15.0)
private const val WORLD_ZOOM = 1.6

private enum class FocusedNavField {
    NONE,
    FROM,
    TO
}

private enum class PaymentMethod(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
) {
    CREDIT_CARD(
        id = "card",
        title = "Credit / Debit Card",
        subtitle = "Visa, Mastercard, Amex",
        icon = Icons.Rounded.CreditCard
    ),
    GOOGLE_PAY(
        id = "google_pay",
        title = "Google Pay",
        subtitle = "Fast one-tap mobile checkout",
        icon = Icons.Rounded.Payment
    ),
    PAYPAL(
        id = "paypal",
        title = "PayPal",
        subtitle = "Pay with your PayPal balance or bank",
        icon = Icons.Rounded.AccountBalanceWallet
    )
}

private data class WorldCityItem(
    val name: String,
    val country: String,
    val lat: Double,
    val lng: Double
)

private data class FerryPort(
    val name: String,
    val region: String,
    val lat: Double,
    val lng: Double
)

private data class FerryCorridor(
    val fromPort: FerryPort,
    val toPort: FerryPort,
    val label: String,
    val speedKmh: Double = 36.0
)

private data class ComputedRouteResult(
    val routeSegments: List<List<Pair<Double, Double>>>,
    val totalDistanceMeters: Double,
    val walkableDistanceMeters: Double,
    val totalDurationSeconds: Double,
    val summaryLabel: String,
    val modeLabel: String,
    val includesFerry: Boolean,
    val isEstimatedFallback: Boolean = false,
    val requestedDistanceKm: Double? = null
)

private enum class TravelMode(
    val id: String,
    val osrmProfile: String,
    val label: String,
    val icon: ImageVector,
    val durationMultiplier: Double
) {
    WALK("walk", "foot", "Walk", Icons.Rounded.DirectionsWalk, 1.0),
    TRANSIT("transit", "foot", "Walk & Ferry", Icons.Rounded.DirectionsBus, 1.15),
    DRIVE("drive", "driving", "Drive", Icons.Rounded.DirectionsCar, 1.0)
}

private val WORLD_CITIES = listOf(
    WorldCityItem("Seoul", "South Korea", 37.5665, 126.9780),
    WorldCityItem("Incheon", "South Korea", 37.4563, 126.7052),
    WorldCityItem("Busan", "South Korea", 35.1796, 129.0756),
    WorldCityItem("Jeju", "South Korea", 33.4996, 126.5312),
    WorldCityItem("Tokyo", "Japan", 35.6762, 139.6503),
    WorldCityItem("Osaka", "Japan", 34.6937, 135.5023),
    WorldCityItem("Fukuoka", "Japan", 33.5902, 130.4017),
    WorldCityItem("Sapporo", "Japan", 43.0618, 141.3545),
    WorldCityItem("Beijing", "China", 39.9042, 116.4074),
    WorldCityItem("Shanghai", "China", 31.2304, 121.4737),
    WorldCityItem("Hong Kong", "China", 22.3193, 114.1694),
    WorldCityItem("Taipei", "Taiwan", 25.0330, 121.5654),
    WorldCityItem("Singapore", "Singapore", 1.3521, 103.8198),
    WorldCityItem("Bangkok", "Thailand", 13.7563, 100.5018),
    WorldCityItem("Hanoi", "Vietnam", 21.0278, 105.8342),
    WorldCityItem("Manila", "Philippines", 14.5995, 120.9842),
    WorldCityItem("New Delhi", "India", 28.6139, 77.2090),
    WorldCityItem("Mumbai", "India", 19.0760, 72.8777),
    WorldCityItem("Dubai", "UAE", 25.2048, 55.2708),
    WorldCityItem("Istanbul", "Turkey", 41.0082, 28.9784),
    WorldCityItem("London", "United Kingdom", 51.5072, -0.1276),
    WorldCityItem("Paris", "France", 48.8566, 2.3522),
    WorldCityItem("Berlin", "Germany", 52.5200, 13.4050),
    WorldCityItem("Rome", "Italy", 41.9028, 12.4964),
    WorldCityItem("Madrid", "Spain", 40.4168, -3.7038),
    WorldCityItem("Amsterdam", "Netherlands", 52.3676, 4.9041),
    WorldCityItem("New York", "United States", 40.7128, -74.0060),
    WorldCityItem("Los Angeles", "United States", 34.0522, -118.2437),
    WorldCityItem("San Francisco", "United States", 37.7749, -122.4194),
    WorldCityItem("Chicago", "United States", 41.8781, -87.6298),
    WorldCityItem("Toronto", "Canada", 43.6532, -79.3832),
    WorldCityItem("Vancouver", "Canada", 49.2827, -123.1207),
    WorldCityItem("Mexico City", "Mexico", 19.4326, -99.1332),
    WorldCityItem("Sao Paulo", "Brazil", -23.5505, -46.6333),
    WorldCityItem("Buenos Aires", "Argentina", -34.6037, -58.3816),
    WorldCityItem("Cairo", "Egypt", 30.0444, 31.2357),
    WorldCityItem("Nairobi", "Kenya", -1.2921, 36.8219),
    WorldCityItem("Sydney", "Australia", -33.8688, 151.2093),
    WorldCityItem("Melbourne", "Australia", -37.8136, 144.9631),
    WorldCityItem("Auckland", "New Zealand", -36.8485, 174.7633)
)

private val FERRY_CORRIDORS: List<FerryCorridor> = run {
    val busan = FerryPort("Busan Port International Terminal", "KR", 35.1155, 129.0490)
    val incheon = FerryPort("Incheon Port International Terminal", "KR", 37.4240, 126.5920)
    val mokpo = FerryPort("Mokpo Coastal Ferry Terminal", "KR", 34.7830, 126.3840)
    val jeju = FerryPort("Jeju Port Passenger Terminal", "JEJU", 33.5245, 126.5430)

    val hakata = FerryPort("Hakata Port (Fukuoka)", "JP", 33.6050, 130.4050)
    val shimonoseki = FerryPort("Shimonoseki Port", "JP", 33.9470, 130.9270)
    val osakaPort = FerryPort("Osaka Nanko Ferry Terminal", "JP", 34.6330, 135.4150)

    val qingdao = FerryPort("Qingdao Ferry Terminal", "CN", 36.0820, 120.3120)
    val weihai = FerryPort("Weihai Ferry Terminal", "CN", 37.5080, 122.1420)
    val keelung = FerryPort("Keelung Port (Taiwan)", "TW", 25.1340, 121.7410)
    val xiamen = FerryPort("Xiamen Ferry Terminal", "CN", 24.4840, 118.0750)

    val calais = FerryPort("Port of Calais (France)", "EU", 50.9660, 1.8630)
    val dover = FerryPort("Port of Dover (UK)", "UK", 51.1240, 1.3280)
    val singaporePort = FerryPort("Singapore HarbourFront", "SG", 1.2640, 103.8200)
    val hongKongPort = FerryPort("Hong Kong Macau Ferry Terminal", "HK", 22.2890, 114.1520)

    val oneWay = listOf(
        FerryCorridor(busan, hakata, "Busan ↔ Hakata Fast Ferry"),
        FerryCorridor(busan, shimonoseki, "Busan ↔ Shimonoseki Ferry"),
        FerryCorridor(busan, osakaPort, "Busan ↔ Osaka PanStar Ferry"),
        FerryCorridor(mokpo, jeju, "Mokpo ↔ Jeju Passenger Ferry"),
        FerryCorridor(busan, jeju, "Busan ↔ Jeju Passenger Ferry"),
        FerryCorridor(incheon, weihai, "Incheon ↔ Weihai Ferry"),
        FerryCorridor(incheon, qingdao, "Incheon ↔ Qingdao Ferry"),
        FerryCorridor(xiamen, keelung, "Xiamen ↔ Keelung Strait Ferry"),
        FerryCorridor(calais, dover, "Calais ↔ Dover Channel Ferry"),
        FerryCorridor(singaporePort, hongKongPort, "South China Sea Maritime Route")
    )
    oneWay + oneWay.map {
        FerryCorridor(it.toPort, it.fromPort, it.label, it.speedKmh)
    }
}

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

private val sharedHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(14, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) MapAppPro/2.2")
                .header("Accept", "application/json,image/png,image/jpeg,image/*,*/*;q=0.8")
                .build()
            chain.proceed(request)
        }
        .build()
}

private fun configureMapLibreHttpClient() {
    HttpRequestUtil.setOkHttpClient(sharedHttpClient)
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

private fun haversineMeters(a: Pair<Double, Double>, b: Pair<Double, Double>): Double {
    val r = 6371000.0
    val dLat = Math.toRadians(b.first - a.first)
    val dLng = Math.toRadians(b.second - a.second)
    val lat1 = Math.toRadians(a.first)
    val lat2 = Math.toRadians(b.first)
    val h = sin(dLat / 2) * sin(dLat / 2) +
            cos(lat1) * cos(lat2) * sin(dLng / 2) * sin(dLng / 2)
    return 2 * r * atan2(sqrt(h), sqrt(1 - h))
}

private fun initialBearingDegrees(from: Pair<Double, Double>, to: Pair<Double, Double>): Double {
    val lat1 = Math.toRadians(from.first)
    val lat2 = Math.toRadians(to.first)
    val dLng = Math.toRadians(to.second - from.second)
    val y = sin(dLng) * cos(lat2)
    val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLng)
    return (Math.toDegrees(atan2(y, x)) + 360.0) % 360.0
}

private fun destinationPoint(
    start: Pair<Double, Double>,
    distanceMeters: Double,
    bearingDegrees: Double
): Pair<Double, Double> {
    val r = 6371000.0
    val angularDist = distanceMeters / r
    val bearing = Math.toRadians(bearingDegrees)
    val lat1 = Math.toRadians(start.first)
    val lng1 = Math.toRadians(start.second)

    val lat2 = asin(
        sin(lat1) * cos(angularDist) +
                cos(lat1) * sin(angularDist) * cos(bearing)
    )
    val lng2 = lng1 + atan2(
        sin(bearing) * sin(angularDist) * cos(lat1),
        cos(angularDist) - sin(lat1) * sin(lat2)
    )
    return Math.toDegrees(lat2) to Math.toDegrees(lng2)
}

private fun buildInterpolatedPath(
    from: Pair<Double, Double>,
    to: Pair<Double, Double>,
    steps: Int = 28
): List<Pair<Double, Double>> {
    val points = ArrayList<Pair<Double, Double>>(steps + 1)
    val dLat = to.first - from.first
    val dLng = to.second - from.second
    val curveOffsetLat = -dLng * 0.04
    val curveOffsetLng = dLat * 0.04
    for (i in 0..steps) {
        val t = i.toDouble() / steps
        val arch = sin(Math.PI * t)
        val lat = from.first + dLat * t + curveOffsetLat * arch
        val lng = from.second + dLng * t + curveOffsetLng * arch
        points.add(lat to lng)
    }
    return points
}

private fun formatCoordLabel(lat: Double, lng: Double): String {
    return String.format(Locale.US, "%.4f, %.4f", lat, lng)
}

private suspend fun resolveLocationInput(
    input: String,
    userLatLng: Pair<Double, Double>?
): Pair<Pair<Double, Double>, String>? = withContext(Dispatchers.IO) {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return@withContext null

    if (trimmed.equals("My Location", ignoreCase = true) || trimmed.equals("Current Location", ignoreCase = true)) {
        return@withContext userLatLng?.let { it to "My Location" }
    }

    val parts = trimmed.split(",").map { it.trim() }
    if (parts.size == 2) {
        val lat = parts[0].toDoubleOrNull()
        val lng = parts[1].toDoubleOrNull()
        if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
            return@withContext (lat to lng) to formatCoordLabel(lat, lng)
        }
    }

    val matchedCity = WORLD_CITIES.firstOrNull {
        it.name.equals(trimmed, ignoreCase = true) ||
                "${it.name}, ${it.country}".equals(trimmed, ignoreCase = true)
    }
    if (matchedCity != null) {
        return@withContext (matchedCity.lat to matchedCity.lng) to matchedCity.name
    }

    try {
        val encoded = URLEncoder.encode(trimmed, "UTF-8")
        val req = Request.Builder()
            .url("https://photon.komoot.io/api/?q=$encoded&limit=1")
            .build()
        sharedHttpClient.newCall(req).execute().use { resp ->
            if (resp.isSuccessful) {
                val body = resp.body?.string().orEmpty()
                val features = JSONObject(body).optJSONArray("features")
                if (features != null && features.length() > 0) {
                    val feat = features.getJSONObject(0)
                    val coords = feat.getJSONObject("geometry").getJSONArray("coordinates")
                    val lng = coords.getDouble(0)
                    val lat = coords.getDouble(1)
                    val props = feat.optJSONObject("properties")
                    val label = props?.optString("name").takeUnless { it.isNullOrBlank() } ?: trimmed
                    return@withContext (lat to lng) to label
                }
            }
        }
    } catch (_: Exception) {
    }
    null
}

private data class OsrmLeg(
    val points: List<Pair<Double, Double>>,
    val distanceMeters: Double,
    val durationSeconds: Double
)

private suspend fun queryOsrmLeg(
    profile: String,
    from: Pair<Double, Double>,
    to: Pair<Double, Double>,
    maxSnapGapMeters: Double = 80_000.0
): OsrmLeg? = withContext(Dispatchers.IO) {
    try {
        val url = String.format(
            Locale.US,
            "https://router.project-osrm.org/route/v1/%s/%.6f,%.6f;%.6f,%.6f?overview=full&geometries=geojson",
            profile,
            from.second,
            from.first,
            to.second,
            to.first
        )
        val req = Request.Builder().url(url).build()
        sharedHttpClient.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) return@withContext null
            val json = JSONObject(resp.body?.string().orEmpty())
            if (json.optString("code") != "Ok") return@withContext null
            val routes = json.optJSONArray("routes") ?: return@withContext null
            if (routes.length() == 0) return@withContext null
            val best = routes.getJSONObject(0)
            val dist = best.optDouble("distance", -1.0)
            val dur = best.optDouble("duration", -1.0)
            val coords = best.getJSONObject("geometry").getJSONArray("coordinates")
            val pts = ArrayList<Pair<Double, Double>>(coords.length())
            for (i in 0 until coords.length()) {
                val pair = coords.getJSONArray(i)
                pts.add(pair.getDouble(1) to pair.getDouble(0))
            }
            if (pts.size < 2 || dist <= 0.0) return@withContext null

            val endSnapGap = haversineMeters(pts.last(), to)
            val startSnapGap = haversineMeters(pts.first(), from)
            if (endSnapGap > maxSnapGapMeters || startSnapGap > maxSnapGapMeters) {
                return@withContext null
            }

            OsrmLeg(pts, dist, dur)
        }
    } catch (_: Exception) {
        null
    }
}

private fun buildFallbackRoute(
    from: Pair<Double, Double>,
    to: Pair<Double, Double>,
    mode: TravelMode,
    targetOverrideMeters: Double? = null
): ComputedRouteResult {
    val straightMeters = haversineMeters(from, to).coerceAtLeast(100.0)
    val roadFactor = when (mode) {
        TravelMode.DRIVE -> 1.25
        TravelMode.WALK -> 1.22
        TravelMode.TRANSIT -> 1.18
    }
    val distMeters = targetOverrideMeters ?: (straightMeters * roadFactor)
    val speedMps = when (mode) {
        TravelMode.WALK -> 1.38
        TravelMode.TRANSIT -> 6.5
        TravelMode.DRIVE -> 22.2
    }
    val durationSec = (distMeters / speedMps) * mode.durationMultiplier
    val path = buildInterpolatedPath(from, to)

    return ComputedRouteResult(
        routeSegments = listOf(path),
        totalDistanceMeters = distMeters,
        walkableDistanceMeters = if (mode != TravelMode.DRIVE) distMeters else 0.0,
        totalDurationSeconds = durationSec,
        summaryLabel = "${mode.label} Route",
        modeLabel = mode.label,
        includesFerry = mode == TravelMode.TRANSIT,
        isEstimatedFallback = true,
        requestedDistanceKm = targetOverrideMeters?.let { it / 1000.0 }
    )
}

private suspend fun findDestinationForTargetDistance(
    from: Pair<Double, Double>,
    preferredTo: Pair<Double, Double>?,
    targetDistanceMeters: Double,
    mode: TravelMode
): Pair<Pair<Double, Double>, ComputedRouteResult> {
    val profile = if (mode == TravelMode.DRIVE) "driving" else "foot"
    val baseBearing = if (preferredTo != null && haversineMeters(from, preferredTo) > 20.0) {
        initialBearingDegrees(from, preferredTo)
    } else {
        45.0
    }

    val candidateCoord = destinationPoint(from, targetDistanceMeters * 0.8, baseBearing)
    val leg = queryOsrmLeg(profile, from, candidateCoord)
    if (leg != null) {
        val snappedDest = leg.points.last()
        val targetKm = targetDistanceMeters / 1000.0
        return snappedDest to ComputedRouteResult(
            routeSegments = listOf(leg.points),
            totalDistanceMeters = leg.distanceMeters,
            walkableDistanceMeters = if (profile == "foot") leg.distanceMeters else 0.0,
            totalDurationSeconds = leg.durationSeconds * mode.durationMultiplier,
            summaryLabel = String.format(Locale.US, "Target %.1f km · %s Route", targetKm, mode.label),
            modeLabel = mode.label,
            includesFerry = false,
            requestedDistanceKm = targetKm
        )
    }

    val fallbackDest = destinationPoint(from, targetDistanceMeters / 1.22, baseBearing)
    return fallbackDest to buildFallbackRoute(
        from = from,
        to = fallbackDest,
        mode = mode,
        targetOverrideMeters = targetDistanceMeters
    )
}

private suspend fun computeAccessibleRoute(
    from: Pair<Double, Double>,
    to: Pair<Double, Double>,
    mode: TravelMode
): ComputedRouteResult {
    val primaryProfile = if (mode == TravelMode.DRIVE) "driving" else "foot"
    val directPath = queryOsrmLeg(primaryProfile, from, to)
    if (directPath != null) {
        return ComputedRouteResult(
            routeSegments = listOf(directPath.points),
            totalDistanceMeters = directPath.distanceMeters,
            walkableDistanceMeters = if (primaryProfile == "foot") directPath.distanceMeters else 0.0,
            totalDurationSeconds = directPath.durationSeconds * mode.durationMultiplier,
            summaryLabel = "${mode.label} Route",
            modeLabel = mode.label,
            includesFerry = false
        )
    }

    if (mode == TravelMode.TRANSIT) {
        val corridor = FERRY_CORRIDORS.minByOrNull { c ->
            haversineMeters(from, c.fromPort.lat to c.fromPort.lng) +
                    haversineMeters(c.toPort.lat to c.toPort.lng, to)
        }
        if (corridor != null) {
            val portA = corridor.fromPort.lat to corridor.fromPort.lng
            val portB = corridor.toPort.lat to corridor.toPort.lng
            val d1 = haversineMeters(from, portA)
            val seaDist = haversineMeters(portA, portB) * 1.12
            val d2 = haversineMeters(portB, to)
            val totalDist = d1 + seaDist + d2
            val seaSec = (seaDist / 1000.0) / corridor.speedKmh * 3600.0 + 1800.0
            val landSec = ((d1 + d2) / 1000.0 / 4.8) * 3600.0
            val fullLine = buildInterpolatedPath(from, portA, 10) +
                    buildInterpolatedPath(portA, portB, 18) +
                    buildInterpolatedPath(portB, to, 10)

            return ComputedRouteResult(
                routeSegments = listOf(fullLine),
                totalDistanceMeters = totalDist,
                walkableDistanceMeters = d1 + d2,
                totalDurationSeconds = seaSec + landSec,
                summaryLabel = corridor.label,
                modeLabel = mode.label,
                includesFerry = true
            )
        }
    }

    return buildFallbackRoute(from = from, to = to, mode = mode)
}

private fun formatDurationEnglish(totalSeconds: Double): String {
    val totalMins = (totalSeconds / 60.0).roundToInt().coerceAtLeast(1)
    val hours = totalMins / 60
    val mins = totalMins % 60
    return if (hours > 0) "${hours} hr ${mins} min" else "${mins} min"
}

private fun formatDistanceKm(meters: Double): String {
    return String.format(Locale.US, "%.1f km", meters / 1000.0)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel = viewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
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

    var isSettingsOpen by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.CREDIT_CARD) }
    var isAdsRemoved by remember { mutableStateOf(false) }
    var paymentStatusMessage by remember { mutableStateOf<String?>(null) }

    var citySearchText by remember { mutableStateOf("") }
    var isCityDropdownOpen by remember { mutableStateOf(false) }

    var isFromToExpanded by remember { mutableStateOf(true) }
    var focusedNavField by remember { mutableStateOf(FocusedNavField.NONE) }
    var fromText by remember { mutableStateOf("Seoul") }
    var toText by remember { mutableStateOf("Busan") }
    var distanceInputKm by remember { mutableStateOf("") }
    var isCustomDistanceEdited by remember { mutableStateOf(false) }
    var selectedTravelMode by remember { mutableStateOf(TravelMode.WALK) }
    var isCalculatingRoute by remember { mutableStateOf(false) }
    var computedRoute by remember { mutableStateOf<ComputedRouteResult?>(null) }
    var resolvedFromPin by remember { mutableStateOf<Pair<Pair<Double, Double>, String>?>(null) }
    var resolvedToPin by remember { mutableStateOf<Pair<Pair<Double, Double>, String>?>(null) }
    var routeErrorMessage by remember { mutableStateOf<String?>(null) }

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

    fun fitCameraToRoute(
        route: ComputedRouteResult,
        fromCoord: Pair<Double, Double>,
        toCoord: Pair<Double, Double>
    ) {
        val allCoords = route.routeSegments.flatten() + listOf(fromCoord, toCoord)
        if (allCoords.size >= 2) {
            val boundsBuilder = LatLngBounds.Builder()
            allCoords.forEach { boundsBuilder.include(LatLng(it.first, it.second)) }
            try {
                mapLibreMap?.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 110, 220, 110, 420)
                )
            } catch (_: Exception) {
                val midLat = (fromCoord.first + toCoord.first) / 2.0
                val midLng = (fromCoord.second + toCoord.second) / 2.0
                mapLibreMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(midLat, midLng), 8.0))
            }
        }
    }

    fun executeFromToDirection(
        modeOverride: TravelMode = selectedTravelMode,
        fromOverride: String = fromText,
        toOverride: String = toText,
        useEnteredDistance: Boolean = isCustomDistanceEdited && distanceInputKm.trim().isNotEmpty(),
        clearFocusAfter: Boolean = true
    ) {
        if (clearFocusAfter) {
            focusManager.clearFocus()
            focusedNavField = FocusedNavField.NONE
        }
        isCityDropdownOpen = false

        scope.launch {
            isCalculatingRoute = true
            routeErrorMessage = null

            val originResolved = resolveLocationInput(fromOverride, userLatLng)
                ?: userLatLng?.let { it to "My Location" }

            if (originResolved == null) {
                isCalculatingRoute = false
                routeErrorMessage = "Please enter a valid origin or tap the map."
                return@launch
            }

            resolvedFromPin = originResolved
            val parsedTargetKm = distanceInputKm.trim().toDoubleOrNull()

            if (useEnteredDistance && parsedTargetKm != null && parsedTargetKm > 0.0) {
                val preferredDest = resolveLocationInput(toOverride, userLatLng)?.first
                val (destCoord, route) = findDestinationForTargetDistance(
                    from = originResolved.first,
                    preferredTo = preferredDest,
                    targetDistanceMeters = parsedTargetKm * 1000.0,
                    mode = modeOverride
                )
                isCalculatingRoute = false

                val destLabel = formatCoordLabel(destCoord.first, destCoord.second)
                toText = destLabel
                resolvedToPin = destCoord to destLabel
                computedRoute = route
                distanceInputKm = formatDistanceKm(route.totalDistanceMeters).removeSuffix(" km")
                isCustomDistanceEdited = false
                viewModel.inspectCoordinates(destCoord.first, destCoord.second)
                fitCameraToRoute(route, originResolved.first, destCoord)
                return@launch
            }

            val destResolved = resolveLocationInput(toOverride, userLatLng)
            if (destResolved == null) {
                isCalculatingRoute = false
                routeErrorMessage = "Please enter a valid destination or tap the map."
                return@launch
            }

            resolvedToPin = destResolved

            val best = computeAccessibleRoute(originResolved.first, destResolved.first, modeOverride)
            isCalculatingRoute = false

            computedRoute = best
            distanceInputKm = formatDistanceKm(best.totalDistanceMeters).removeSuffix(" km")
            isCustomDistanceEdited = false
            viewModel.inspectCoordinates(destResolved.first.first, destResolved.first.second)
            fitCameraToRoute(best, originResolved.first, destResolved.first)
        }
    }

    val latestMapTapHandler by rememberUpdatedState<(LatLng) -> Unit> { latLng ->
        isCityDropdownOpen = false
        val coordText = formatCoordLabel(latLng.latitude, latLng.longitude)

        when (focusedNavField) {
            FocusedNavField.FROM -> {
                fromText = coordText
                resolvedFromPin = (latLng.latitude to latLng.longitude) to coordText
                isCustomDistanceEdited = false
                viewModel.inspectCoordinates(latLng.latitude, latLng.longitude)

                if (toText.trim().isNotEmpty()) {
                    executeFromToDirection(
                        fromOverride = coordText,
                        toOverride = toText,
                        useEnteredDistance = false,
                        clearFocusAfter = false
                    )
                }
            }
            FocusedNavField.TO -> {
                toText = coordText
                resolvedToPin = (latLng.latitude to latLng.longitude) to coordText
                isCustomDistanceEdited = false
                viewModel.inspectCoordinates(latLng.latitude, latLng.longitude)

                if (fromText.trim().isNotEmpty()) {
                    executeFromToDirection(
                        fromOverride = fromText,
                        toOverride = coordText,
                        useEnteredDistance = false,
                        clearFocusAfter = false
                    )
                }
            }
            FocusedNavField.NONE -> {
                focusManager.clearFocus()
                viewModel.inspectCoordinates(latLng.latitude, latLng.longitude)
            }
        }
    }

    fun processRemoveAdsPayment() {
        isAdsRemoved = true
        paymentStatusMessage = "Paid $REMOVE_ADS_PRICE via ${selectedPaymentMethod.title}. Ads removed."
        isSettingsOpen = false
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
                latestMapTapHandler(latLng)
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

    LaunchedEffect(mapLibreMap, uiState.selectedPlace, computedRoute, resolvedFromPin, resolvedToPin, userLatLng) {
        val map = mapLibreMap ?: return@LaunchedEffect
        map.clear()

        userLatLng?.let { (uLat, uLng) ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(uLat, uLng))
                    .title("My Location")
            )
        }

        resolvedFromPin?.let { (coords, label) ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(coords.first, coords.second))
                    .title("From: $label")
            )
        }

        resolvedToPin?.let { (coords, label) ->
            map.addMarker(
                MarkerOptions()
                    .position(LatLng(coords.first, coords.second))
                    .title("To: $label")
            )
        }

        // Draw available route in red
        computedRoute?.let { route ->
            route.routeSegments.forEach { seg ->
                if (seg.size >= 2) {
                    map.addPolyline(
                        PolylineOptions()
                            .addAll(seg.map { LatLng(it.first, it.second) })
                            .width(7f)
                            .color(android.graphics.Color.parseColor("#DC2626"))
                    )
                }
            }
        }

        uiState.selectedPlace?.let { place ->
            val target = LatLng(place.latitude, place.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(target)
                    .title(place.name)
            )
            if (computedRoute == null && focusedNavField == FocusedNavField.NONE) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 10.0))
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

    if (isSettingsOpen) {
        AlertDialog(
            onDismissRequest = { isSettingsOpen = false },
            containerColor = CardSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Tune, contentDescription = null, tint = BrandBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Preferences & Billing",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Payment method ($REMOVE_ADS_PRICE to remove ads):",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    PaymentMethod.entries.forEach { method ->
                        val isSelected = selectedPaymentMethod == method
                        Surface(
                            onClick = { selectedPaymentMethod = method },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) BrandBlueSoft else SubtleBg,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) BrandBlue else BorderSubtle
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = method.icon,
                                    contentDescription = method.title,
                                    tint = if (isSelected) BrandBlue else TextSecondary,
                                    modifier = Modifier.size(19.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = method.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = method.subtitle,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                                Icon(
                                    imageVector = if (isSelected) {
                                        Icons.Rounded.CheckCircle
                                    } else {
                                        Icons.Rounded.RadioButtonUnchecked
                                    },
                                    contentDescription = null,
                                    tint = if (isSelected) BrandBlue else TextSecondary,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    if (isAdsRemoved) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = OriginGreen.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.CheckCircle,
                                    contentDescription = null,
                                    tint = OriginGreen,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Ad-Free unlocked via ${selectedPaymentMethod.title}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OriginGreen
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = { processRemoveAdsPayment() },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = selectedPaymentMethod.icon,
                                contentDescription = null,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Remove Ads · $REMOVE_ADS_PRICE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { isSettingsOpen = false }) {
                    Text("Done", color = BrandBlue, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SubtleBg)
    ) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F172A).copy(alpha = 0.24f), Color.Transparent)
                    )
                )
        )

        if (isMapLoading || mapLoadError != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CardSurface.copy(alpha = 0.94f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    if (isMapLoading) {
                        CircularProgressIndicator(color = BrandBlue, strokeWidth = 3.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Loading ${selectedLayer.label} map...",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    } else if (mapLoadError != null) {
                        Text(
                            text = "Unable to display map",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RouteRed
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = mapLoadError ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { reloadKey++ },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Retry")
                            }
                            Button(
                                onClick = {
                                    val entries = MapLayerStyle.entries
                                    val next = entries[(selectedLayer.ordinal + 1) % entries.size]
                                    selectedLayer = next
                                    mapLibreMap?.let { applyMapStyle(it, next) }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandBlueDark),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Switch Layer")
                            }
                        }
                    }
                }
            }
        }

        // Top Search & Weather Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .align(Alignment.TopCenter)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    shadowElevation = 6.dp,
                    color = CardSurface,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 12.dp, end = 6.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Search,
                            contentDescription = null,
                            tint = BrandBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = Modifier
                                .weight(1f)
                                .focusable()
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        focusedNavField = FocusedNavField.NONE
                                        isCityDropdownOpen = true
                                    }
                                }
                        ) {
                            if (citySearchText.isEmpty()) {
                                Text(
                                    "Search cities or places...",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            BasicTextField(
                                value = citySearchText,
                                onValueChange = { typed ->
                                    citySearchText = typed
                                    viewModel.onSearchQueryChanged(typed)
                                    isCityDropdownOpen = true
                                },
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                cursorBrush = SolidColor(BrandBlue),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = BrandBlueSoft,
                            modifier = Modifier.clickable {
                                isCityDropdownOpen = false
                                focusManager.clearFocus()
                                focusedNavField = FocusedNavField.NONE
                                viewModel.clearSelection()
                                computedRoute = null
                                resolvedFromPin = null
                                resolvedToPin = null
                                distanceInputKm = ""
                                isCustomDistanceEdited = false
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
                                    Icons.Rounded.Public,
                                    contentDescription = "World View",
                                    tint = BrandBlue,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "World",
                                    color = BrandBlue,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        if (citySearchText.isNotEmpty() || isCityDropdownOpen) {
                            Spacer(modifier = Modifier.width(2.dp))
                            IconButton(
                                onClick = {
                                    citySearchText = ""
                                    viewModel.onSearchQueryChanged("")
                                    isCityDropdownOpen = false
                                    focusManager.clearFocus()
                                },
                                modifier = Modifier.size(26.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.Close,
                                    contentDescription = "Clear",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }

                Surface(
                    onClick = {
                        isCityDropdownOpen = false
                        focusManager.clearFocus()
                        focusedNavField = FocusedNavField.NONE
                        isSettingsOpen = true
                    },
                    shape = CircleShape,
                    shadowElevation = 6.dp,
                    color = CardSurface,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Tune,
                            contentDescription = "Settings",
                            tint = BrandBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(visible = focusedNavField != FocusedNavField.NONE) {
                val isFrom = focusedNavField == FocusedNavField.FROM
                val badgeColor = if (isFrom) OriginGreen else RouteRed
                val promptText = if (isFrom) {
                    "Tap map to set FROM point"
                } else {
                    "Tap map to set TO point"
                }
                Spacer(modifier = Modifier.height(5.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp,
                    color = badgeColor,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.TouchApp,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = promptText,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        IconButton(
                            onClick = {
                                focusedNavField = FocusedNavField.NONE
                                focusManager.clearFocus()
                            },
                            modifier = Modifier.size(22.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = "Done picking",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            paymentStatusMessage?.let { status ->
                Spacer(modifier = Modifier.height(5.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp,
                    color = OriginGreen,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = status,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { paymentStatusMessage = null },
                            modifier = Modifier.size(22.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = "Dismiss",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 4.dp,
                color = CardSurface.copy(alpha = 0.95f),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.Thermostat,
                        contentDescription = "Weather",
                        tint = BrandBlue,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    when {
                        uiState.isLoadingWeather -> {
                            Text(
                                text = "Loading weather...",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary
                            )
                        }
                        uiState.weather != null -> {
                            val w = uiState.weather!!
                            val placeTitle = uiState.selectedPlace?.name ?: "Selected Spot"
                            Text(
                                text = "$placeTitle · ${w.temperatureC}°C (${w.description})",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Rounded.Air,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${w.windSpeedKmh} km/h",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                        else -> {
                            Text(
                                text = "Tap map or search a city for live weather",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isCityDropdownOpen,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 8.dp,
                    color = CardSurface,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                        .heightIn(max = 210.dp)
                ) {
                    LazyColumn(contentPadding = PaddingValues(vertical = 2.dp)) {
                        items(filteredWorldCities) { city ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        citySearchText = "${city.name}, ${city.country}"
                                        toText = city.name
                                        isCustomDistanceEdited = false
                                        isCityDropdownOpen = false
                                        focusManager.clearFocus()
                                        viewModel.inspectCoordinates(city.lat, city.lng)
                                        mapLibreMap?.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(city.lat, city.lng),
                                                10.5
                                            )
                                        )
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.Place,
                                    contentDescription = null,
                                    tint = BrandBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = city.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = city.country,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    text = String.format(Locale.US, "%.1f°, %.1f°", city.lat, city.lng),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                            HorizontalDivider(thickness = 0.5.dp, color = BorderSubtle)
                        }
                    }
                }
            }
        }

        // Right Floating Map Controls
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp, bottom = 70.dp)
        ) {
            Surface(
                onClick = {
                    val entries = MapLayerStyle.entries
                    val next = entries[(selectedLayer.ordinal + 1) % entries.size]
                    selectedLayer = next
                    mapLibreMap?.let { applyMapStyle(it, next, resetToWorld = false) }
                },
                shape = CircleShape,
                shadowElevation = 5.dp,
                color = CardSurface,
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Rounded.Layers,
                        contentDescription = "Map Layer",
                        tint = BrandBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = selectedLayer.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 5.dp,
                color = CardSurface,
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = { mapLibreMap?.animateCamera(CameraUpdateFactory.zoomIn()) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = "Zoom In", tint = TextPrimary, modifier = Modifier.size(18.dp))
                    }
                    HorizontalDivider(modifier = Modifier.width(22.dp), thickness = 0.8.dp, color = BorderSubtle)
                    IconButton(
                        onClick = { mapLibreMap?.animateCamera(CameraUpdateFactory.zoomOut()) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Rounded.Remove, contentDescription = "Zoom Out", tint = TextPrimary, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Surface(
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
                shape = CircleShape,
                shadowElevation = 6.dp,
                color = BrandBlue,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Rounded.MyLocation,
                        contentDescription = "My Location",
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }

        // Bottom Direction Panel + Ad Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                shadowElevation = 12.dp,
                color = CardSurface,
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isFromToExpanded = !isFromToExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(RouteRedSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.Route,
                                    contentDescription = null,
                                    tint = RouteRed,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (computedRoute != null) {
                                        "Distance: ${formatDistanceKm(computedRoute!!.totalDistanceMeters)} · ${formatDurationEnglish(computedRoute!!.totalDurationSeconds)}"
                                    } else {
                                        "Route & Distance"
                                    },
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "$fromText → $toText",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    val tempText = fromText
                                    fromText = toText
                                    toText = tempText
                                    val tempPin = resolvedFromPin
                                    resolvedFromPin = resolvedToPin
                                    resolvedToPin = tempPin
                                    isCustomDistanceEdited = false
                                    if (fromText.isNotBlank() && toText.isNotBlank()) {
                                        executeFromToDirection(clearFocusAfter = false)
                                    }
                                },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.SwapVert,
                                    contentDescription = "Swap From and To",
                                    tint = BrandBlue,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                            IconButton(
                                onClick = { isFromToExpanded = !isFromToExpanded },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(
                                    imageVector = if (isFromToExpanded) Icons.Rounded.ExpandMore else Icons.Rounded.ExpandLess,
                                    contentDescription = if (isFromToExpanded) "Collapse bar" else "Expand bar",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = isFromToExpanded) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.height(6.dp))

                            // FROM input
                            val isFromFocused = focusedNavField == FocusedNavField.FROM
                            Surface(
                                shape = RoundedCornerShape(11.dp),
                                color = if (isFromFocused) OriginGreen.copy(alpha = 0.05f) else SubtleBg,
                                border = BorderStroke(
                                    width = if (isFromFocused) 1.5.dp else 1.dp,
                                    color = if (isFromFocused) OriginGreen else BorderSubtle
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 10.dp)
                                ) {
                                    Icon(
                                        Icons.Rounded.NearMe,
                                        contentDescription = null,
                                        tint = OriginGreen,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .weight(1f)
                                            .onFocusChanged { state ->
                                                if (state.isFocused) {
                                                    focusedNavField = FocusedNavField.FROM
                                                    isCityDropdownOpen = false
                                                }
                                            }
                                    ) {
                                        if (fromText.isEmpty()) {
                                            Text(
                                                if (isFromFocused) "Tap map for From..." else "From origin...",
                                                color = TextSecondary,
                                                fontSize = 12.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        BasicTextField(
                                            value = fromText,
                                            onValueChange = {
                                                fromText = it
                                                isCustomDistanceEdited = false
                                            },
                                            singleLine = true,
                                            textStyle = TextStyle(
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            cursorBrush = SolidColor(OriginGreen),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    if (isFromFocused) {
                                        Icon(
                                            Icons.Rounded.TouchApp,
                                            contentDescription = "Tap map active",
                                            tint = OriginGreen,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // TO input
                            val isToFocused = focusedNavField == FocusedNavField.TO
                            Surface(
                                shape = RoundedCornerShape(11.dp),
                                color = if (isToFocused) RouteRed.copy(alpha = 0.05f) else SubtleBg,
                                border = BorderStroke(
                                    width = if (isToFocused) 1.5.dp else 1.dp,
                                    color = if (isToFocused) RouteRed else BorderSubtle
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 10.dp)
                                ) {
                                    Icon(
                                        Icons.Rounded.Flag,
                                        contentDescription = null,
                                        tint = RouteRed,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .weight(1f)
                                            .onFocusChanged { state ->
                                                if (state.isFocused) {
                                                    focusedNavField = FocusedNavField.TO
                                                    isCityDropdownOpen = false
                                                }
                                            }
                                    ) {
                                        if (toText.isEmpty()) {
                                            Text(
                                                if (isToFocused) "Tap map for To..." else "To destination...",
                                                color = TextSecondary,
                                                fontSize = 12.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        BasicTextField(
                                            value = toText,
                                            onValueChange = {
                                                toText = it
                                                isCustomDistanceEdited = false
                                            },
                                            singleLine = true,
                                            textStyle = TextStyle(
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            cursorBrush = SolidColor(RouteRed),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    if (isToFocused) {
                                        Icon(
                                            Icons.Rounded.TouchApp,
                                            contentDescription = "Tap map active",
                                            tint = RouteRed,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Distance (km) input
                            var isDistFocused by remember { mutableStateOf(false) }
                            Surface(
                                shape = RoundedCornerShape(11.dp),
                                color = SubtleBg,
                                border = BorderStroke(
                                    width = if (isDistFocused) 1.5.dp else 1.dp,
                                    color = if (isDistFocused) BrandBlue else BorderSubtle
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 10.dp)
                                ) {
                                    Icon(
                                        Icons.Rounded.Straighten,
                                        contentDescription = null,
                                        tint = BrandBlue,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .weight(1f)
                                            .onFocusChanged { state ->
                                                isDistFocused = state.isFocused
                                                if (state.isFocused) {
                                                    focusedNavField = FocusedNavField.NONE
                                                }
                                            }
                                    ) {
                                        if (distanceInputKm.isEmpty()) {
                                            Text(
                                                "Distance (km)",
                                                color = TextSecondary,
                                                fontSize = 12.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        BasicTextField(
                                            value = distanceInputKm,
                                            onValueChange = { typed ->
                                                val sanitized = typed.filter { it.isDigit() || it == '.' }
                                                distanceInputKm = sanitized
                                                isCustomDistanceEdited = sanitized.isNotEmpty()
                                            },
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                            textStyle = TextStyle(
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            cursorBrush = SolidColor(BrandBlue),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    if (distanceInputKm.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                distanceInputKm = ""
                                                isCustomDistanceEdited = false
                                            },
                                            modifier = Modifier.size(22.dp)
                                        ) {
                                            Icon(
                                                Icons.Rounded.Close,
                                                contentDescription = "Clear distance",
                                                tint = TextSecondary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Travel Mode Pills
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TravelMode.entries.forEach { mode ->
                                    val selected = selectedTravelMode == mode
                                    Surface(
                                        onClick = {
                                            selectedTravelMode = mode
                                            if (computedRoute != null) {
                                                executeFromToDirection(modeOverride = mode)
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (selected) BrandBlueSoft else SubtleBg,
                                        border = BorderStroke(
                                            1.dp,
                                            if (selected) BrandBlue else BorderSubtle
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)
                                        ) {
                                            Icon(
                                                imageVector = mode.icon,
                                                contentDescription = mode.label,
                                                tint = if (selected) BrandBlue else TextSecondary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = mode.label,
                                                fontSize = 11.sp,
                                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (selected) BrandBlue else TextPrimary,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Direction CTA Button
                            Button(
                                onClick = { executeFromToDirection() },
                                enabled = !isCalculatingRoute,
                                shape = RoundedCornerShape(11.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = RouteRed,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                            ) {
                                if (isCalculatingRoute) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Calculating route...", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                } else {
                                    Icon(
                                        Icons.Rounded.Route,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isCustomDistanceEdited && distanceInputKm.isNotEmpty()) {
                                            "Route ${distanceInputKm} km"
                                        } else {
                                            "Direction"
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            routeErrorMessage?.let { msg ->
                                Spacer(modifier = Modifier.height(5.dp))
                                Surface(
                                    shape = RoundedCornerShape(9.dp),
                                    color = RouteRedSoft,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = msg,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = RouteRed,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            // Computed Distance & Route Summary Card
                            computedRoute?.let { route ->
                                val totalKmText = formatDistanceKm(route.totalDistanceMeters)
                                val durText = formatDurationEnglish(route.totalDurationSeconds)
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(11.dp),
                                    color = RouteRedSoft,
                                    border = BorderStroke(1.dp, RouteRed.copy(alpha = 0.28f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = if (route.includesFerry) Icons.Rounded.DirectionsBoat else Icons.Rounded.Schedule,
                                                    contentDescription = null,
                                                    tint = RouteRed,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "$totalKmText · $durText",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextPrimary
                                                )
                                            }
                                            Text(
                                                text = if (route.isEstimatedFallback) {
                                                    "${route.summaryLabel} (Estimated path · Red line)"
                                                } else {
                                                    "${route.summaryLabel} · Red line"
                                                },
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextSecondary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = RouteRed
                                        ) {
                                            Text(
                                                text = totalKmText,
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!isAdsRemoved) {
                Surface(
                    color = AdAmberBg,
                    border = BorderStroke(1.dp, AdAmberBorder),
                    shadowElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Campaign,
                                contentDescription = null,
                                tint = AdAmberAccent,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ad · Travel Offers ($REMOVE_ADS_PRICE to remove)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        OutlinedButton(
                            onClick = { isSettingsOpen = true },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, AdAmberAccent),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(26.dp)
                        ) {
                            Text(
                                text = REMOVE_ADS_PRICE,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AdAmberAccent
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

