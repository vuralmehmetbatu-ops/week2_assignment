package com.example.week2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import java.util.Locale

@Immutable
data class HardwareItem(
    val id: String,
    val name: String,
    val category: String,
    val cost: Double,
    val stockCount: Int
)

private val hardwareItems = listOf(
    HardwareItem("101", "MacBook Pro M3 Max", "Geliştirici Bilgisayarı", 3499.0, 5),
    HardwareItem("102", "NVIDIA RTX 4090 Sunucu", "Yapay Zeka / GPU", 1599.0, 2),
    HardwareItem("103", "Google Pixel 8 Pro", "Mobil Test Cihazı", 999.0, 8),
    HardwareItem("104", "Dell UltraSharp 49\"", "Monitör", 1199.0, 4),
    HardwareItem("105", "Sony WH-1000XM5 Kulaklık", "Odaklanma Ekipmanı", 399.0, 12),
    HardwareItem("106", "Raspberry Pi 5 8GB", "IoT Geliştirme Kiti", 85.0, 25),
    HardwareItem("107", "HHKB Professional Keyboard", "Ergonomik Klavye", 299.0, 0),
    HardwareItem("108", "AMD Ryzen Threadripper", "Yapay Zeka / GPU", 4999.0, 1)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DevClusterApp()
            }
        }
    }
}

@Composable
fun DevClusterApp() {
    // Dynamic screen state is deliberately stored in the Compose Slot Table with remember.
    var totalBudget by remember { mutableDoubleStateOf(0.0) }

    // Search survives Activity recreation caused by configuration changes.
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Filtering is memoized and runs again only when the search key changes.
    val filteredItems = remember(searchQuery) {
        val query = searchQuery.trim()
        if (query.isBlank()) {
            hardwareItems
        } else {
            hardwareItems.filter { item ->
                item.name.contains(query, ignoreCase = true) ||
                    item.category.contains(query, ignoreCase = true) ||
                    item.id.contains(query, ignoreCase = true)
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "DevCluster",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Donanım ve Sunucu Kaynak Yönetim Simülatörü",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            HardwareSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Toplam harcanan ödenek: $${String.format(Locale.US, "%.2f", totalBudget)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Gösterilen kaynak: ${filteredItems.size}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = filteredItems,
                    key = { it.id }
                ) { item ->
                    HardwareCard(
                        item = item,
                        onClick = {
                            if (item.stockCount > 0) {
                                totalBudget += item.cost
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun HardwareSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Stateless: state comes from above and events go up through the callback.
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        singleLine = true,
        label = { Text("Donanım ara") },
        placeholder = { Text("Ad, kategori veya ID") },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {})
    )
}

@Composable
fun HardwareCard(
    item: HardwareItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Stateless. Required modifier order: clickable() first, padding() afterwards.
    Card(
        modifier = modifier
            .clickable(enabled = item.stockCount > 0, onClick = onClick)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "#${item.id}")
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = item.category, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$${String.format(Locale.US, "%.2f", item.cost)}",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (item.stockCount == 0) "Stokta yok" else "Stok: ${item.stockCount}",
                    fontWeight = if (item.stockCount == 0) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
