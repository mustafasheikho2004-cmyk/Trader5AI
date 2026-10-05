package com.trader5ai.app

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trader5ai.app.ui.theme.Trader5AITheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Trader5AITheme {
                Trader5AIScreen()
            }
        }
    }
}

private data class MarketAsset(
    val symbol: String,
    val name: String,
    val price: Double,
    val change: Double,
    val volume: String,
    val signal: String,
    val trend: FloatArray,
    val color: Color
)

@Composable
fun Trader5AIScreen() {
    val assets = remember { sampleMarketData() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Bolt, contentDescription = "AI signal")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                HeaderCard()
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SmallStatCard(title = "Portfolio", value = "$24.8K", delta = "+8.2%")
                    SmallStatCard(title = "AI Score", value = "81/100", delta = "Strong")
                }
            }

            item {
                SectionTitle(title = "AI Signals")
            }

            items(assets) { asset ->
                MarketAssetRow(asset = asset)
            }
        }
    }
}

@Composable
private fun HeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Trader 5 AI",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Market Overview",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Alerts",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = "$128,420.56",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "+$6,480.21 today",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1EE39A)
                )
                Text(
                    text = "24h change +8.7%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun SmallStatCard(title: String, value: String, delta: String) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = delta,
                style = MaterialTheme.typography.labelMedium,
                color = if (delta.contains("+")) Color(0xFF1EE39A) else Color(0xFFFF8A65)
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun MarketAssetRow(asset: MarketAsset) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(asset.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = asset.symbol.take(2),
                        style = MaterialTheme.typography.labelLarge,
                        color = asset.color,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column {
                    Text(
                        text = asset.symbol,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = asset.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format("%.2f", asset.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${if (asset.change >= 0) "+" else ""}${String.format("%.2f", asset.change)}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (asset.change >= 0) Color(0xFF1EE39A) else Color(0xFFFF8A65)
                )
            }

            MiniTrendChart(asset = asset)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vol: ${asset.volume}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Text(
                text = asset.signal,
                style = MaterialTheme.typography.labelMedium,
                color = if (asset.signal.contains("Buy")) Color(0xFF1EE39A) else if (asset.signal.contains("Sell")) Color(0xFFFF8A65) else Color(0xFF8AB4FF),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MiniTrendChart(asset: MarketAsset) {
    Box(
        modifier = Modifier
            .size(width = 88.dp, height = 36.dp)
            .padding(start = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path()
            val values = asset.trend
            if (values.isNotEmpty()) {
                val step = size.width / (values.size - 1).coerceAtLeast(1)
                val maxValue = values.maxOrNull() ?: 1f
                val minValue = values.minOrNull() ?: 0f
                val range = (maxValue - minValue).coerceAtLeast(0.1f)

                values.forEachIndexed { index, value ->
                    val x = index * step
                    val y = size.height - ((value - minValue) / range) * size.height
                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }

                drawPath(
                    path = path,
                    color = asset.color,
                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                )
            }
        }
    }
}

private fun sampleMarketData(): List<MarketAsset> {
    return listOf(
        MarketAsset(
            symbol = "BTC/USD",
            name = "Bitcoin",
            price = 67240.12,
            change = 4.82,
            volume = "12.4M",
            signal = "Buy Signal",
            trend = floatArrayOf(58f, 60f, 62f, 61f, 66f, 68f, 70f, 72f),
            color = Color(0xFFF7931A)
        ),
        MarketAsset(
            symbol = "ETH/USD",
            name = "Ethereum",
            price = 3520.45,
            change = 3.14,
            volume = "8.1M",
            signal = "Buy Signal",
            trend = floatArrayOf(41f, 43f, 42f, 45f, 48f, 49f, 52f, 55f),
            color = Color(0xFF627EEA)
        ),
        MarketAsset(
            symbol = "XAU/USD",
            name = "Gold",
            price = 2356.78,
            change = -0.62,
            volume = "1.9M",
            signal = "Wait",
            trend = floatArrayOf(72f, 70f, 69f, 68f, 66f, 65f, 64f, 63f),
            color = Color(0xFFF1C40F)
        ),
        MarketAsset(
            symbol = "EUR/USD",
            name = "Forex",
            price = 1.0874,
            change = 0.42,
            volume = "2.5M",
            signal = "Buy Signal",
            trend = floatArrayOf(50f, 52f, 51f, 54f, 56f, 58f, 60f, 62f),
            color = Color(0xFF00C2FF)
        ),
        MarketAsset(
            symbol = "SOL/USD",
            name = "Solana",
            price = 154.88,
            change = -1.84,
            volume = "5.6M",
            signal = "Sell Signal",
            trend = floatArrayOf(81f, 80f, 78f, 76f, 73f, 70f, 68f, 65f),
            color = Color(0xFF00FFA3)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun Trader5AIPreview() {
    Trader5AITheme {
        Trader5AIScreen()
    }
}
