package ca.qolt

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import ca.qolt.data.local.entity.PresetEntity
import ca.qolt.model.InstalledApp
import java.util.UUID

private fun loadInstalledApps(context: Context): List<InstalledApp> {
    val pm = context.packageManager
    val ownPackageName = context.packageName // Get Qolt's package name

    return pm.getInstalledApplications(PackageManager.GET_META_DATA)
        .filter { appInfo ->
            // Exclude Qolt app itself and only include apps with launch intent
            pm.getLaunchIntentForPackage(appInfo.packageName) != null &&
            appInfo.packageName != ownPackageName
        }
        .map { appInfo ->
            InstalledApp(
                packageName = appInfo.packageName,
                appName = pm.getApplicationLabel(appInfo).toString(),
                icon = pm.getApplicationIcon(appInfo),
                category = appInfo.category
            )
        }
        .sortedBy { it.appName.lowercase() }
}

@Composable
private fun rememberAppIcon(drawable: Drawable): ImageBitmap {
    return remember(drawable) {
        val w = drawable.intrinsicWidth.coerceAtLeast(1)
        val h = drawable.intrinsicHeight.coerceAtLeast(1)
        val bmp = createBitmap(w, h)
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bmp.asImageBitmap()
    }
}

@Composable
fun PresetsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var allApps by remember { mutableStateOf<List<InstalledApp>>(emptyList()) }
    LaunchedEffect(Unit) {
        allApps = loadInstalledApps(context)
    }

    var presets by remember {
        mutableStateOf(
            listOf(
                PresetEntity(
                    id = "study",
                    name = "Study Focus",
                    description = "Black List",
                    blockedApps = emptyList(),
                ),
                PresetEntity(
                    id = "sleep",
                    name = "Sleep Mode",
                    description = "Black List",
                    blockedApps = emptyList(),
                    emoji = "\uD83D\uDCA4"
                )
            )
        )
    }

    var editingPreset by remember { mutableStateOf<PresetEntity?>(null) }
    var isCreatingNew by remember { mutableStateOf(false) }
    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1C1C1E))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFDFDFD),
                                shape = RoundedCornerShape(100)
                            )
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(
                            0.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "search",
                            contentScale = ContentScale.None,
                        )
                    }
                    Text(
                        text = "Blocks",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    )
                    Column(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFF4400),
                                shape = RoundedCornerShape(100)
                            )
                            .padding(16.dp)
                            .clickable {
                                isCreatingNew = true
                                editingPreset = PresetEntity(
                                    id = UUID.randomUUID().toString(),
                                    name = "",
                                    description = "",
                                    blockedApps = emptyList()
                                )
                            },
                        verticalArrangement = Arrangement.spacedBy(
                            0.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "add",
                            contentScale = ContentScale.None,
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
                ) {
                    items(presets) { preset ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color(0xFFFF4400),
                                        RoundedCornerShape(48.dp)
                                    )
                                    .clickable {
                                        isCreatingNew = false
                                        editingPreset = preset
                                    }
                                    .padding(vertical = 16.dp, horizontal = 24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = preset.emoji,
                                        style = TextStyle(
                                            fontSize = 22.sp
                                        )
                                    )
                                }

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = preset.name.ifBlank { "Untitled preset" },
                                        style = TextStyle(
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                    if (preset.description.isNotBlank()) {
                                        Text(
                                            text = preset.description,
                                            style = TextStyle(
                                                color = Color(0xFFFFEDE3),
                                                fontSize = 13.sp
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = "${preset.blockedApps.size} apps",
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                        }
                    }
                }
                val presetToEdit = editingPreset
                if (presetToEdit != null) {
                    PresetEditorDialog(
                        preset = presetToEdit,
                        allApps = allApps,
                        onDismiss = {
                            editingPreset = null
                            isCreatingNew = false
                        },
                        onSave = { updated ->
                            presets = if (isCreatingNew) {
                                presets + updated
                            } else {
                                presets.map { if (it.id == updated.id) updated else it }
                            }
                            editingPreset = null
                            isCreatingNew = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PresetEditorDialog(
    preset: PresetEntity,
    allApps: List<InstalledApp>,
    onDismiss: () -> Unit,
    onSave: (PresetEntity) -> Unit
) {
    var name by remember { mutableStateOf(preset.name) }
    val description = "Black List"
    var selectedPackages by remember { mutableStateOf(preset.blockedApps.toSet()) }

    val emojiOptions = listOf("\uD83D\uDCD6", "\uD83D\uDCDA", "\uD83D\uDCA4", "\uD83D\uDCBC", "\uD83C\uDFAE", "\uD83C\uDFA7", "\uD83D\uDCF5")
    var emoji by remember { mutableStateOf(preset.emoji.ifBlank { "\uD83D\uDCD6" }) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (preset.name.isBlank()) "New preset" else "Edit preset",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Preset name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Emoji",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        emojiOptions.forEach { option ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (emoji == option) Color(0x33FFFFFF) else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { emoji = option }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option,
                                    style = TextStyle(fontSize = 24.sp)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = "Blocked apps",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    if (allApps.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Loading apps...",
                                style = TextStyle(fontSize = 13.sp, color = Color.Gray)
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(allApps) { app ->
                                val isChecked = app.packageName in selectedPackages
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val iconBitmap = rememberAppIcon(app.icon)
                                    Image(
                                        bitmap = iconBitmap,
                                        contentDescription = app.appName,
                                        modifier = Modifier
                                            .height(32.dp)
                                            .padding(end = 8.dp)
                                    )

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = app.appName,
                                            style = TextStyle(fontSize = 14.sp)
                                        )
                                        Text(
                                            text = app.packageName,
                                            style = TextStyle(
                                                fontSize = 11.sp,
                                                color = Color.Gray
                                            )
                                        )
                                    }

                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { checked ->
                                            selectedPackages = if (checked) {
                                                selectedPackages + app.packageName
                                            } else {
                                                selectedPackages - app.packageName
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        preset.copy(
                            name = name,
                            description = description,
                            blockedApps = selectedPackages.toList(),
                            emoji = emoji
                        )
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}