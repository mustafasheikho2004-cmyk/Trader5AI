package com.trader5ai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trader5ai.app.ui.theme.Trader5AITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Trader5AITheme {
                Trader5AIApp()
            }
        }
    }
}

data class MarketAsset(
    val symbol: String,
    val name: String,
    val price: Double,
    val change: Double,
    val volume: String,
    val signal: String,
    val trend: List<Float>,
    val color: Color
)

data class PortfolioItem(
    val symbol: String,
    val amount: String,
    val pnl: String,
    val weight: Float,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Trader5AIApp() {
    val assets = remember { sampleMarketData() }
    val portfolio = remember { samplePortfolio() }
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trader 5 AI") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Bolt, contentDescription = null) },
                    label = { Text("Overview") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
                    label = { Text("Portfolio") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                    label = { Text("Alerts") }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeaderCard()
            SummaryRow()
            TabsRow(selectedTab = selectedTab, onTabSelected = { selectedTab = it })

            when (selectedTab) {
                0 -> OverviewTab(assets)
                1 -> PortfolioTab(portfolio)
                2 -> AlertsTab()
                else -> OverviewTab(assets)
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
                Text(
                    text = "AI Market Pulse",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
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
                    color = Color(0xFF1EE39A),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "AI score 81/100",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun SummaryRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SmallStatCard(title = "Portfolio", value = "$24.8K", delta = "+8.2%")
        SmallStatCard(title = "Win Rate", value = "73%", delta = "+5.4%")
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = delta,
                color = if (delta.startsWith("+")) Color(0xFF1EE39A) else Color(0xFFFF8A65),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TabsRow(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val tabs = listOf("Markets", "Portfolio", "Alerts")
        tabs.forEachIndexed { index, label ->
            OutlinedButton(
                onClick = { onTabSelected(index) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    color = if (selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun OverviewTab(assets: List<MarketAsset>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SectionTitle(title = "AI Signals")
        }
        items(assets) { asset ->
            MarketAssetRow(asset)
        }
    }
}

@Composable
private fun PortfolioTab(portfolio: List<PortfolioItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(title = "Holdings")
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                portfolio.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(item.color.copy(alpha = 0.22f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item.symbol.take(2), color = item.color, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            Column {
                                Text(item.symbol, fontWeight = FontWeight.Bold)
                                Text(item.amount, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text(item.pnl, color = if (item.pnl.startsWith("+")) Color(0xFF1EE39A) else Color(0xFFFF8A65), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertsTab() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(title = "Signal Feed")
        val alerts = listOf(
            "BTC/USD buy opportunity confirmed by RSI + MACD momentum",
            "ETH/USD strong breakout above 3,520 resistance",
            "XAU/USD wait signal — range compression detected",
            "USD/JPY momentum weak; prefer hedged exposure"
        )

        alerts.forEach { message ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1EE39A))
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(message, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun MarketAssetRow(asset: MarketAsset) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(asset.color.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(asset.symbol.take(2), fontWeight = FontWeight.Bold, color = asset.color)
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column {
                        Text(asset.symbol, fontWeight = FontWeight.Bold)
                        Text(asset.name, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("$${String.format("%.2f", asset.price)}")
                    Text(
                        text = "${if (asset.change >= 0) "+" else ""}${String.format("%.2f", asset.change)}%",
                        color = if (asset.change >= 0) Color(0xFF1EE39A) else Color(0xFFFF8A65),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MiniTrendChart(asset.trend, asset.color)
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = asset.signal,
                        color = if (asset.signal.contains("Buy")) Color(0xFF1EE39A) else if (asset.signal.contains("Sell")) Color(0xFFFF8A65) else Color(0xFF8AB4FF),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Vol: ${asset.volume}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniTrendChart(values: List<Float>, color: Color) {
    Box(
        modifier = Modifier
            .size(width = 96.dp, height = 36.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.06f)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(6.dp)) {
            if (values.isEmpty()) return@Canvas
            val path = Path()
            val max = values.maxOrNull() ?: 1f
            val min = values.minOrNull() ?: 0f
            val range = (max - min).coerceAtLeast(0.1f)
            val step = size.width / (values.size - 1).coerceAtLeast(1)

            values.forEachIndexed { index, value ->
                val x = index * step
                val y = size.height - ((value - min) / range) * size.height
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }

            drawPath(path, color = color, style = Stroke(width = 3f, cap = StrokeCap.Round))
        }
    }
}

private fun sampleMarketData(): List<MarketAsset> = listOf(
    MarketAsset("BTC/USD", "Bitcoin", 67240.12, 4.82, "12.4M", "Buy Signal", listOf(58f, 60f, 62f, 61f, 66f, 68f, 70f, 72f), Color(0xFFF7931A)),
    MarketAsset("ETH/USD", "Ethereum", 3520.45, 3.14, "8.1M", "Buy Signal", listOf(41f, 43f, 42f, 45f, 48f, 49f, 52f, 55f), Color(0xFF627EEA)),
    MarketAsset("XAU/USD", "Gold", 2356.78, -0.62, "1.9M", "Wait", listOf(72f, 70f, 69f, 68f, 66f, 65f, 64f, 63f), Color(0xFFF1C40F)),
    MarketAsset("EUR/USD", "Forex", 1.0874, 0.42, "2.5M", "Buy Signal", listOf(50f, 52f, 51f, 54f, 56f, 58f, 60f, 62f), Color(0xFF00C2FF)),
    MarketAsset("SOL/USD", "Solana", 154.88, -1.84, "5.6M", "Sell Signal", listOf(81f, 80f, 78f, 76f, 73f, 70f, 68f, 65f), Color(0xFF00FFA3))
)

private fun samplePortfolio(): List<PortfolioItem> = listOf(
    PortfolioItem("BTC", "0.82 BTC", "+$6,420", 0.46f, Color(0xFFF7931A)),
    PortfolioItem("ETH", "14.32 ETH", "+$3,180", 0.33f, Color(0xFF627EEA)),
    PortfolioItem("SOL", "120 SOL", "+$1,480", 0.21f, Color(0xFF00FFA3))
)

@Preview(showBackground = true)
@Composable
fun Trader5AIPreview() {
    Trader5AITheme {
        Trader5AIApp()
    }
}
