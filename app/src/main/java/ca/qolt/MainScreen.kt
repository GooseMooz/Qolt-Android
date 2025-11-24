package ca.qolt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class TabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    BLOCKS("Blocks", Icons.Filled.Lock, Icons.Outlined.Lock),
    STATS("Stats", Icons.Filled.BarChart, Icons.Outlined.BarChart),
    PROFILE("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(TabItem.HOME) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    TabItem.HOME -> HomeScreen()
                    TabItem.BLOCKS -> PresetsScreen()
                    TabItem.STATS -> ca.qolt.ui.statistics.StatisticsScreen()
                    TabItem.PROFILE -> ProfileScreen(onLogout = onLogout)
                }
            }

            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedTab: TabItem,
    onTabSelected: (TabItem) -> Unit
) {
    val orange = Color(0xFFFF6A1A)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2E))
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabItem.entries.forEach { tab ->
            TabButton(
                tab = tab,
                isSelected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                selectedColor = orange
            )
        }
    }
}

@Composable
private fun TabButton(
    tab: TabItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color
) {
    val backgroundColor = if (isSelected) selectedColor.copy(alpha = 0.15f) else Color.Transparent
    val contentColor = if (isSelected) selectedColor else Color(0xFF888888)
    val icon = if (isSelected) tab.selectedIcon else tab.unselectedIcon

    Column(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = tab.title,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )

        if (isSelected) {
            Text(
                text = tab.title,
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
