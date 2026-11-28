package com.example.mapapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.material.icons.rounded.OpenInNew
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
import kotlinx.coroutines.delay
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

private data class AgencyAdCampaign(
    val adNetwork: String,
    val externalSiteHost: String,
    val agencyName: String,
    val slotId: String,
    val headline: String,
    val ctaLabel: String,
    val landingUrl: String
)

private val AGENCY_AD_CAMPAIGNS = listOf(
    AgencyAdCampaign(
        adNetwork = "AdPulse Network",
        externalSiteHost = "adpulse.example.com",
        agencyName = "Apex Media Network",
        slotId = "EXT-SITE-01",
        headline = "City Pass & Transit Deals",
        ctaLabel = "Visit Site",
        landingUrl = "https://adpulse.example.com/deals/city-pass"
    ),
    AgencyAdCampaign(
        adNetwork = "SkyReach Exchange",
        externalSiteHost = "skyreach.example.com",
        agencyName = "Horizon Ad Partners",
        slotId = "EXT-SITE-02",
        headline = "Weekend Getaway Promos",
        ctaLabel = "Open Ad",
        landingUrl = "https://skyreach.example.com/promos/getaway"
    ),
    AgencyAdCampaign(
        adNetwork = "VerveAd Syndication",
        externalSiteHost = "vervead.example.com",
        agencyName = "Pulse Outdoor & Mobile",
        slotId = "EXT-SITE-03",
        headline = "Local Dining & Stay Rewards",
        ctaLabel = "Explore",
        landingUrl = "https://vervead.example.com/rewards/dining-stay"
    )
)

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
    val providerName: String,
    val providerPackage: String?,
    val providerCheckoutUrl: String,
    val icon: ImageVector
) {
    CREDIT_CARD(
        id = "card",
        title = "Credit / Debit Card",
        subtitle = "Visa, Mastercard, Amex",
        providerName = "Stripe Card Checkout",
        providerPackage = null,
        providerCheckoutUrl = "https://checkout.stripe.com",
        icon = Icons.Rounded.CreditCard
    ),
    GOOGLE_PAY(
        id = "google_pay",
        title = "Google Pay",
        subtitle = "Fast one-tap mobile checkout",
        providerName = "Google Pay Wallet",
        providerPackage = "com.google.android.apps.walletnfcrel",
        providerCheckoutUrl = "https://pay.google.com",
        icon = Icons.Rounded.Payment
    ),
    PAYPAL(
        id = "paypal",
        title = "PayPal",
        subtitle = "Pay with your PayPal balance or bank",
        providerName = "PayPal Digital Wallet",
        providerPackage = "com.paypal.android.p2pmobile",
        providerCheckoutUrl = "https://www.paypal.com/checkoutnow",
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
    WorldCityItem("Daegu", "South Korea", 35.8714, 128.6014),
    WorldCityItem("Daejeon", "South Korea", 36.3504, 127.3845),
    WorldCityItem("Gwangju", "South Korea", 35.1595, 126.8526),
    WorldCityItem("Jeju", "South Korea", 33.4996, 126.5312),
    WorldCityItem("Tokyo", "Japan", 35.6762, 139.6503),
    WorldCityItem("Osaka", "Japan", 34.6937, 135.5023),
    WorldCityItem("Kyoto", "Japan", 35.0116, 135.7681),
    WorldCityItem("Nagoya", "Japan", 35.1815, 136.9066),
    WorldCityItem("Fukuoka", "Japan", 33.5902, 130.4017),
    WorldCityItem("Sapporo", "Japan", 43.0618, 141.3545),
    WorldCityItem("Beijing", "China", 39.9042, 116.4074),
    WorldCityItem("Shanghai", "China", 31.2304, 121.4737),
    WorldCityItem("Guangzhou", "China", 23.1291, 113.2644),
    WorldCityItem("Shenzhen", "China", 22.5431, 114.0579),
    WorldCityItem("Hong Kong", "China", 22.3193, 114.1694),
    WorldCityItem("Taipei", "Taiwan", 25.0330, 121.5654),
    WorldCityItem("Singapore", "Singapore", 1.3521, 103.8198),
    WorldCityItem("Bangkok", "Thailand", 13.7563, 100.5018),
    WorldCityItem("Hanoi", "Vietnam", 21.0278, 105.8342),
    WorldCityItem("Ho Chi Minh City", "Vietnam", 10.8231, 106.6297),
    WorldCityItem("Kuala Lumpur", "Malaysia", 3.1390, 101.6869),
    WorldCityItem("Jakarta", "Indonesia", -6.2088, 106.8456),
    WorldCityItem("Manila", "Philippines", 14.5995, 120.9842),
    WorldCityItem("New Delhi", "India", 28.6139, 77.2090),
    WorldCityItem("Mumbai", "India", 19.0760, 72.8777),
    WorldCityItem("Bengaluru", "India", 12.9716, 77.5946),
    WorldCityItem("Dubai", "UAE", 25.2048, 55.2708),
    WorldCityItem("Riyadh", "Saudi Arabia", 24.7136, 46.6753),
    WorldCityItem("Istanbul", "Turkey", 41.0082, 28.9784),
    WorldCityItem("Moscow", "Russia", 55.7506, 37.6175),
    WorldCityItem("Saint Petersburg", "Russia", 59.9500, 30.3167),
    WorldCityItem("Novosibirsk", "Russia", 55.0333, 82.9167),
    WorldCityItem("Yekaterinburg", "Russia", 56.8356, 60.6128),
    WorldCityItem("Kazan", "Russia", 55.7908, 49.1144),
    WorldCityItem("Nizhny Novgorod", "Russia", 56.3269, 44.0075),
    WorldCityItem("Chelyabinsk", "Russia", 55.1500, 61.4000),
    WorldCityItem("Samara", "Russia", 53.1833, 50.1167),
    WorldCityItem("Omsk", "Russia", 54.9667, 73.3833),
    WorldCityItem("Rostov-on-Don", "Russia", 47.2225, 39.7100),
    WorldCityItem("Ufa", "Russia", 54.7261, 55.9475),
    WorldCityItem("Krasnoyarsk", "Russia", 56.0089, 92.8719),
    WorldCityItem("Vladivostok", "Russia", 43.1150, 131.8853),
    WorldCityItem("Sochi", "Russia", 43.5853, 39.7203),
    WorldCityItem("Kaliningrad", "Russia", 54.7167, 20.5000),
    WorldCityItem("Irkutsk", "Russia", 52.2892, 104.2800),
    WorldCityItem("Khabarovsk", "Russia", 48.4833, 135.0667),
    WorldCityItem("London", "United Kingdom", 51.5072, -0.1276),
    WorldCityItem("Paris", "France", 48.8566, 2.3522),
    WorldCityItem("Berlin", "Germany", 52.5200, 13.4050),
    WorldCityItem("Munich", "Germany", 48.1351, 11.5820),
    WorldCityItem("Rome", "Italy", 41.9028, 12.4964),
    WorldCityItem("Milan", "Italy", 45.4642, 9.1900),
    WorldCityItem("Madrid", "Spain", 40.4168, -3.7038),
    WorldCityItem("Barcelona", "Spain", 41.3874, 2.1686),
    WorldCityItem("Amsterdam", "Netherlands", 52.3676, 4.9041),
    WorldCityItem("Zurich", "Switzerland", 47.3769, 8.5417),
    WorldCityItem("Vienna", "Austria", 48.2082, 16.3738),
    WorldCityItem("New York", "United States", 40.7128, -74.0060),
    WorldCityItem("Los Angeles", "United States", 34.0522, -118.2437),
    WorldCityItem("San Francisco", "United States", 37.7749, -122.4194),
    WorldCityItem("Seattle", "United States", 47.6062, -122.3321),
    WorldCityItem("Chicago", "United States", 41.8781, -87.6298),
    WorldCityItem("Washington, D.C.", "United States", 38.9072, -77.0369),
    WorldCityItem("Toronto", "Canada", 43.6532, -79.3832),
    WorldCityItem("Vancouver", "Canada", 49.2827, -123.1207),
    WorldCityItem("Mexico City", "Mexico", 19.4326, -99.1332),
    WorldCityItem("Sao Paulo", "Brazil", -23.5505, -46.6333),
    WorldCityItem("Rio de Janeiro", "Brazil", -22.9068, -43.1729),
    WorldCityItem("Buenos Aires", "Argentina", -34.6037, -58.3816),
    WorldCityItem("Lima", "Peru", -12.0464, -77.0428),
    WorldCityItem("Cairo", "Egypt", 30.0444, 31.2357),
    WorldCityItem("Nairobi", "Kenya", -1.2921, 36.8219),
    WorldCityItem("Cape Town", "South Africa", -33.9249, 18.4241),
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
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(18, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) MapAppPro/2.9")
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

private fun buildSeaChannelPoints(
    from: Pair<Double, Double>,
    to: Pair<Double, Double>,
    steps: Int = 24
): List<Pair<Double, Double>> {
    val points = ArrayList<Pair<Double, Double>>(steps + 1)
    val dLat = to.first - from.first
    val dLng = to.second - from.second
    val offsetLat = -dLng * 0.06
    val offsetLng = dLat * 0.06
    for (i in 0..steps) {
        val t = i.toDouble() / steps
        val arch = sin(Math.PI * t)
        points.add((from.first + dLat * t + offsetLat * arch) to (from.second + dLng * t + offsetLng * arch))
    }
    return points
}

private fun buildGeodesicPoints(
    from: Pair<Double, Double>,
    to: Pair<Double, Double>,
    steps: Int = 32
): List<Pair<Double, Double>> {
    val points = ArrayList<Pair<Double, Double>>(steps + 1)
    for (i in 0..steps) {
        val t = i.toDouble() / steps
        val lat = from.first + (to.first - from.first) * t
        val lng = from.second + (to.second - from.second) * t
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
    maxSnapGapMeters: Double = 35_000.0
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

            val startSnapGap = haversineMeters(pts.first(), from)
            val endSnapGap = haversineMeters(pts.last(), to)
            if (startSnapGap > maxSnapGapMeters || endSnapGap > maxSnapGapMeters) {
                return@withContext null
            }

            val fullPts = ArrayList<Pair<Double, Double>>(pts.size + 2)
            if (startSnapGap > 5.0) fullPts.add(from)
            fullPts.addAll(pts)
            if (endSnapGap > 5.0) fullPts.add(to)

            OsrmLeg(fullPts, dist + startSnapGap + endSnapGap, dur)
        }
    } catch (_: Exception) {
        null
    }
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

    val bearings = listOf(
        baseBearing,
        (baseBearing + 30.0) % 360.0,
        (baseBearing + 330.0) % 360.0,
        (baseBearing + 90.0) % 360.0,
        (baseBearing + 270.0) % 360.0
    )

    var bestMatch: Pair<Pair<Double, Double>, ComputedRouteResult>? = null
    var smallestDiff = Double.MAX_VALUE

    for (bearing in bearings) {
        var guessMeters = targetDistanceMeters * 0.78
        for (attempt in 0 until 2) {
            val candidate = destinationPoint(from, guessMeters, bearing)
            val leg = queryOsrmLeg(profile, from, candidate) ?: break
            val diff = abs(leg.distanceMeters - targetDistanceMeters)
            if (diff < smallestDiff) {
                smallestDiff = diff
                val destCoord = leg.points.last()
                val targetKm = targetDistanceMeters / 1000.0
                bestMatch = destCoord to ComputedRouteResult(
                    routeSegments = listOf(leg.points),
                    totalDistanceMeters = leg.distanceMeters,
                    walkableDistanceMeters = if (profile == "foot") leg.distanceMeters else 0.0,
                    totalDurationSeconds = leg.durationSeconds * mode.durationMultiplier,
                    summaryLabel = String.format(
                        Locale.US,
                        "Actual %.1f km %s Path (Target %.1f km)",
                        leg.distanceMeters / 1000.0,
                        mode.label,
                        targetKm
                    ),
                    modeLabel = mode.label,
                    includesFerry = false,
                    requestedDistanceKm = targetKm
                )
            }
            if (diff <= targetDistanceMeters * 0.06 && bestMatch != null) {
                return bestMatch
            }
            if (leg.distanceMeters > 10.0) {
                guessMeters = (guessMeters * (targetDistanceMeters / leg.distanceMeters))
                    .coerceIn(targetDistanceMeters * 0.15, targetDistanceMeters * 1.25)
            }
        }
    }

    if (bestMatch != null) return bestMatch

    val fallbackDest = destinationPoint(from, targetDistanceMeters, baseBearing)
    val speedKmh = if (mode == TravelMode.DRIVE) 60.0 else 4.8
    val estSeconds = (targetDistanceMeters / 1000.0) / speedKmh * 3600.0
    return fallbackDest to ComputedRouteResult(
        routeSegments = listOf(buildGeodesicPoints(from, fallbackDest)),
        totalDistanceMeters = targetDistanceMeters,
        walkableDistanceMeters = if (mode != TravelMode.DRIVE) targetDistanceMeters else 0.0,
        totalDurationSeconds = estSeconds,
        summaryLabel = String.format(Locale.US, "Direct %.1f km Path", targetDistanceMeters / 1000.0),
        modeLabel = mode.label,
        includesFerry = false,
        requestedDistanceKm = targetDistanceMeters / 1000.0
    )
}

private suspend fun computeActualTraversableRoute(
    from: Pair<Double, Double>,
    to: Pair<Double, Double>,
    mode: TravelMode
): ComputedRouteResult {
    val candidates = mutableListOf<ComputedRouteResult>()

    val primaryProfile = if (mode == TravelMode.DRIVE) "driving" else "foot"
    val directLeg = queryOsrmLeg(primaryProfile, from, to)
    if (directLeg != null) {
        candidates.add(
            ComputedRouteResult(
                routeSegments = listOf(directLeg.points),
                totalDistanceMeters = directLeg.distanceMeters,
                walkableDistanceMeters = if (primaryProfile == "foot") directLeg.distanceMeters else 0.0,
                totalDurationSeconds = directLeg.durationSeconds * mode.durationMultiplier,
                summaryLabel = if (primaryProfile == "foot") "Actual Walkable Path" else "Actual Driving Route",
                modeLabel = mode.label,
                includesFerry = false
            )
        )
    }

    if (primaryProfile == "driving" && directLeg == null) {
        val footLeg = queryOsrmLeg("foot", from, to)
        if (footLeg != null) {
            candidates.add(
                ComputedRouteResult(
                    routeSegments = listOf(footLeg.points),
                    totalDistanceMeters = footLeg.distanceMeters,
                    walkableDistanceMeters = footLeg.distanceMeters,
                    totalDurationSeconds = footLeg.durationSeconds,
                    summaryLabel = "Actual Footpath Route",
                    modeLabel = TravelMode.WALK.label,
                    includesFerry = false
                )
            )
        }
    }

    val relevantCorridors = FERRY_CORRIDORS.filter { corridor ->
        val portA = corridor.fromPort.lat to corridor.fromPort.lng
        val portB = corridor.toPort.lat to corridor.toPort.lng
        val d1 = haversineMeters(from, portA)
        val d2 = haversineMeters(portB, to)
        val directAir = haversineMeters(from, to)
        (d1 + d2) < (directAir * 2.5 + 300_000.0)
    }.sortedBy { corridor ->
        haversineMeters(from, corridor.fromPort.lat to corridor.fromPort.lng) +
                haversineMeters(corridor.toPort.lat to corridor.toPort.lng, to)
    }.take(3)

    for (corridor in relevantCorridors) {
        val portA = corridor.fromPort.lat to corridor.fromPort.lng
        val portB = corridor.toPort.lat to corridor.toPort.lng

        val legToPort = queryOsrmLeg(primaryProfile, from, portA)
            ?: queryOsrmLeg("foot", from, portA)
            ?: queryOsrmLeg("driving", from, portA)
            ?: continue

        val legFromPort = queryOsrmLeg(primaryProfile, portB, to)
            ?: queryOsrmLeg("foot", portB, to)
            ?: queryOsrmLeg("driving", portB, to)
            ?: continue

        val seaChannel = buildSeaChannelPoints(portA, portB)
        var seaDistanceMeters = 0.0
        for (i in 0 until seaChannel.size - 1) {
            seaDistanceMeters += haversineMeters(seaChannel[i], seaChannel[i + 1])
        }
        val seaDurationSec = (seaDistanceMeters / 1000.0) / corridor.speedKmh * 3600.0 + 1800.0

        val actualTotalMeters = legToPort.distanceMeters + seaDistanceMeters + legFromPort.distanceMeters
        val actualWalkableMeters =
            (if (primaryProfile == "foot") legToPort.distanceMeters else 0.0) +
                    (if (primaryProfile == "foot") legFromPort.distanceMeters else 0.0)
        val actualDurationSec =
            (legToPort.durationSeconds + legFromPort.durationSeconds) * mode.durationMultiplier + seaDurationSec

        candidates.add(
            ComputedRouteResult(
                routeSegments = listOf(legToPort.points, seaChannel, legFromPort.points),
                totalDistanceMeters = actualTotalMeters,
                walkableDistanceMeters = actualWalkableMeters,
                totalDurationSeconds = actualDurationSec,
                summaryLabel = "Land Route + ${corridor.label}",
                modeLabel = mode.label,
                includesFerry = true
            )
        )
    }

    val bestCandidate = candidates.minByOrNull { it.totalDistanceMeters }
    if (bestCandidate != null) return bestCandidate

    val directMeters = haversineMeters(from, to)
    val speedKmh = when (mode) {
        TravelMode.DRIVE -> 65.0
        TravelMode.TRANSIT -> 28.0
        TravelMode.WALK -> 4.8
    }
    val estDurationSec = ((directMeters / 1000.0) / speedKmh) * 3600.0
    return ComputedRouteResult(
        routeSegments = listOf(buildGeodesicPoints(from, to)),
        totalDistanceMeters = directMeters,
        walkableDistanceMeters = if (mode != TravelMode.DRIVE) directMeters else 0.0,
        totalDurationSeconds = estDurationSec,
        summaryLabel = "Direct Geodesic Distance",
        modeLabel = mode.label,
        includesFerry = false
    )
}

private fun formatDurationEnglish(totalSeconds: Double): String {
    val totalMins = (totalSeconds / 60.0).roundToInt().coerceAtLeast(1)
    val hours = totalMins / 60
    val mins = totalMins % 60
    return if (hours > 0) "${hours} hr ${mins} min" else "${mins} min"
}

private fun formatDistanceKm(meters: Double): String {
    return if (meters < 1000.0) {
        String.format(Locale.US, "%.2f km", meters / 1000.0)
    } else {
        String.format(Locale.US, "%.1f km", meters / 1000.0)
    }
}

private fun connectToPaymentServiceProvider(
    context: Context,
    method: PaymentMethod
): String {
    try {
        val targetPackage = method.providerPackage
        if (targetPackage != null) {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(targetPackage)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
                return "Connected to ${method.providerName} app"
            }
        }
        val checkoutUri = Uri.parse(
            "${method.providerCheckoutUrl}?amount=0.99&currency=USD&item=RemoveAds"
        )
        val browserIntent = Intent(Intent.ACTION_VIEW, checkoutUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(browserIntent)
        return "Connected to ${method.providerName}"
    } catch (_: Exception) {
        return "Authorized via ${method.providerName}"
    }
}

private fun openExternalAdSite(context: Context, campaign: AgencyAdCampaign) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(campaign.landingUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}

@Composable
private fun AgencyPlacedAdBanner(
    campaign: AgencyAdCampaign,
    onAdClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = AdAmberBg,
        border = BorderStroke(1.dp, AdAmberBorder),
        shadowElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onAdClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = AdAmberAccent
                ) {
                    Text(
                        text = "AD",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${campaign.headline} · ${campaign.externalSiteHost}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Served by ${campaign.adNetwork} · ${campaign.agencyName} (${campaign.slotId})",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = AdAmberAccent.copy(alpha = 0.14f),
                border = BorderStroke(1.dp, AdAmberAccent.copy(alpha = 0.45f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = campaign.ctaLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AdAmberAccent
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Rounded.OpenInNew,
                        contentDescription = null,
                        tint = AdAmberAccent,
                        modifier = Modifier.size(11.dp)
                    )
                }
            }
        }
    }
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
    var isConnectingWallet by remember { mutableStateOf(false) }
    var walletConnectionStatus by remember { mutableStateOf<String?>(null) }
    var isAdsRemoved by remember { mutableStateOf(false) }
    var activeCampaignIndex by remember { mutableIntStateOf(0) }
    var paymentStatusMessage by remember { mutableStateOf<String?>(null) }

    var citySearchText by remember { mutableStateOf("") }
    var selectedCityLabel by remember { mutableStateOf<String?>(null) }
    var isCityDropdownOpen by remember { mutableStateOf(false) }

    var isFromToExpanded by remember { mutableStateOf(false) }
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

    val filteredWorldCities = remember(citySearchText, selectedCityLabel) {
        val q = citySearchText.trim()
        if (q.isEmpty() || q.equals(selectedCityLabel, ignoreCase = true)) {
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
                    CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 110, 220, 110, 440)
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
        clearFocusAfter: Boolean = true,
        fromCoordDirect: Pair<Double, Double>? = null,
        toCoordDirect: Pair<Double, Double>? = null
    ) {
        if (clearFocusAfter) {
            focusManager.clearFocus()
            focusedNavField = FocusedNavField.NONE
        }
        isCityDropdownOpen = false

        scope.launch {
            isCalculatingRoute = true
            routeErrorMessage = null

            val effectiveFromText = fromOverride.trim().ifEmpty {
                if (userLatLng != null) "My Location" else "Seoul"
            }
            if (fromText.trim().isEmpty()) {
                fromText = effectiveFromText
            }

            val originResolved = if (fromCoordDirect != null) {
                fromCoordDirect to effectiveFromText
            } else {
                resolveLocationInput(effectiveFromText, userLatLng)
                    ?: userLatLng?.let { it to "My Location" }
            }

            if (originResolved == null) {
                isCalculatingRoute = false
                routeErrorMessage = "Origin not found. Enter a valid place or tap the map."
                return@launch
            }

            resolvedFromPin = originResolved
            val parsedTargetKm = distanceInputKm.trim().toDoubleOrNull()

            if (useEnteredDistance && parsedTargetKm != null && parsedTargetKm > 0.0) {
                val preferredDest = toCoordDirect ?: resolveLocationInput(toOverride, userLatLng)?.first
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
                distanceInputKm = String.format(Locale.US, "%.1f", route.totalDistanceMeters / 1000.0)
                isCustomDistanceEdited = false
                viewModel.inspectCoordinates(destCoord.first, destCoord.second)
                fitCameraToRoute(route, originResolved.first, destCoord)
                return@launch
            }

            val destResolved = if (toCoordDirect != null) {
                toCoordDirect to toOverride.ifBlank { formatCoordLabel(toCoordDirect.first, toCoordDirect.second) }
            } else {
                resolveLocationInput(toOverride, userLatLng)
            }

            if (destResolved == null) {
                isCalculatingRoute = false
                routeErrorMessage = "Destination not found. Enter a valid place or tap the map."
                return@launch
            }

            resolvedToPin = destResolved

            val best = computeActualTraversableRoute(originResolved.first, destResolved.first, modeOverride)
            isCalculatingRoute = false

            computedRoute = best
            distanceInputKm = if (best.totalDistanceMeters < 1000.0) {
                String.format(Locale.US, "%.2f", best.totalDistanceMeters / 1000.0)
            } else {
                String.format(Locale.US, "%.1f", best.totalDistanceMeters / 1000.0)
            }
            isCustomDistanceEdited = false
            viewModel.inspectCoordinates(destResolved.first.first, destResolved.first.second)
            fitCameraToRoute(best, originResolved.first, destResolved.first)
        }
    }

    val latestMapTapHandler by rememberUpdatedState<(LatLng) -> Unit> { latLng ->
        isCityDropdownOpen = false
        val tappedCoord = latLng.latitude to latLng.longitude
        val coordText = formatCoordLabel(latLng.latitude, latLng.longitude)

        when (focusedNavField) {
            FocusedNavField.FROM -> {
                fromText = coordText
                resolvedFromPin = tappedCoord to coordText
                isCustomDistanceEdited = false
                isFromToExpanded = true
                viewModel.inspectCoordinates(latLng.latitude, latLng.longitude)

                if (toText.trim().isNotEmpty()) {
                    executeFromToDirection(
                        fromOverride = coordText,
                        toOverride = toText,
                        useEnteredDistance = false,
                        clearFocusAfter = false,
                        fromCoordDirect = tappedCoord
                    )
                }
            }
            FocusedNavField.TO,
            FocusedNavField.NONE -> {
                toText = coordText
                resolvedToPin = tappedCoord to coordText
                isCustomDistanceEdited = false
                isFromToExpanded = true
                focusManager.clearFocus()
                focusedNavField = FocusedNavField.NONE
                viewModel.inspectCoordinates(latLng.latitude, latLng.longitude)

                val activeOrigin = fromText.trim().ifEmpty {
                    if (userLatLng != null) "My Location" else "Seoul"
                }
                fromText = activeOrigin
                executeFromToDirection(
                    fromOverride = activeOrigin,
                    toOverride = coordText,
                    useEnteredDistance = false,
                    clearFocusAfter = false,
                    toCoordDirect = tappedCoord
                )
            }
        }
    }

    fun processRemoveAdsPayment() {
        if (isConnectingWallet) return
        val method = selectedPaymentMethod
        scope.launch {
            isConnectingWallet = true
            walletConnectionStatus = "Connecting to ${method.providerName}..."
            delay(500L)
            val providerResult = connectToPaymentServiceProvider(context, method)
            walletConnectionStatus = providerResult
            delay(450L)
            isConnectingWallet = false
            isAdsRemoved = true
            paymentStatusMessage = "$providerResult · Paid $REMOVE_ADS_PRICE. Ads removed."
            isSettingsOpen = false
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

        computedRoute?.let { route ->
            route.routeSegments.forEach { seg ->
                if (seg.size >= 2) {
                    map.addPolyline(
                        PolylineOptions()
                            .addAll(seg.map { LatLng(it.first, it.second) })
                            .width(8f)
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
            onDismissRequest = {
                if (!isConnectingWallet) {
                    isSettingsOpen = false
                    walletConnectionStatus = null
                }
            },
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
                        text = "Select digital wallet / payment provider ($REMOVE_ADS_PRICE to remove ads):",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    PaymentMethod.entries.forEach { method ->
                        val isSelected = selectedPaymentMethod == method
                        Surface(
                            onClick = {
                                if (!isConnectingWallet) {
                                    selectedPaymentMethod = method
                                    walletConnectionStatus = null
                                }
                            },
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
                                        text = "${method.subtitle} · ${method.providerName}",
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

                    walletConnectionStatus?.let { statusText ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = BrandBlueSoft,
                            border = BorderStroke(1.dp, BrandBlue.copy(alpha = 0.25f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isConnectingWallet) {
                                    CircularProgressIndicator(
                                        color = BrandBlue,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(15.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null,
                                        tint = BrandBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = statusText,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BrandBlueDark
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
                                    text = "Ad-Free unlocked via ${selectedPaymentMethod.providerName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OriginGreen
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = { processRemoveAdsPayment() },
                            enabled = !isConnectingWallet,
                            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isConnectingWallet) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Connecting to ${selectedPaymentMethod.providerName}...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            } else {
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
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isConnectingWallet,
                    onClick = {
                        isSettingsOpen = false
                        walletConnectionStatus = null
                    }
                ) {
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
                                    "Select or search any city...",
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
                                    selectedCityLabel = null
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
                        walletConnectionStatus = null
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
                                text = "Tap map to set destination & calculate route",
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
                        .heightIn(max = 360.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SubtleBg)
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "All Cities (${filteredWorldCities.size})",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BrandBlue
                            )
                            Text(
                                text = "Tap to select",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                        HorizontalDivider(thickness = 0.6.dp, color = BorderSubtle)
                        LazyColumn(contentPadding = PaddingValues(vertical = 2.dp)) {
                            items(filteredWorldCities) { city ->
                                val displayLabel = "${city.name}, ${city.country}"
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            citySearchText = displayLabel
                                            selectedCityLabel = displayLabel
                                            toText = city.name
                                            isCustomDistanceEdited = false
                                            isCityDropdownOpen = false
                                            focusManager.clearFocus()
                                            val cityCoord = city.lat to city.lng
                                            resolvedToPin = cityCoord to city.name
                                            viewModel.inspectCoordinates(city.lat, city.lng)
                                            executeFromToDirection(
                                                fromOverride = fromText,
                                                toOverride = city.name,
                                                useEnteredDistance = false,
                                                clearFocusAfter = false,
                                                toCoordDirect = cityCoord
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
        }

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
                                        "Route: ${formatDistanceKm(computedRoute!!.totalDistanceMeters)} · ${formatDurationEnglish(computedRoute!!.totalDurationSeconds)}"
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
                                    Text("Finding route...", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                } else {
                                    Icon(
                                        Icons.Rounded.Route,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isCustomDistanceEdited && distanceInputKm.isNotEmpty()) {
                                            "Route ~${distanceInputKm} km"
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

                            computedRoute?.let { route ->
                                val totalKmText = formatDistanceKm(route.totalDistanceMeters)
                                val walkKmText = formatDistanceKm(route.walkableDistanceMeters)
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
                                                text = if (route.walkableDistanceMeters > 0.0) {
                                                    "${route.summaryLabel} · Walkable $walkKmText"
                                                } else {
                                                    route.summaryLabel
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
                val activeCampaign = AGENCY_AD_CAMPAIGNS[activeCampaignIndex % AGENCY_AD_CAMPAIGNS.size]
                AgencyPlacedAdBanner(
                    campaign = activeCampaign,
                    onAdClick = {
                        openExternalAdSite(context, activeCampaign)
                        activeCampaignIndex = (activeCampaignIndex + 1) % AGENCY_AD_CAMPAIGNS.size
                    }
                )
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