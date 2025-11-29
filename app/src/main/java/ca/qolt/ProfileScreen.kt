package ca.qolt

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import coil.compose.AsyncImage
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val orange = Color(0xFFFF6A1A)
    val bg = Color(0xFF1C1C1E)

    var blockTimer by remember { mutableStateOf(PreferencesManager.getBlockTimerEnabled(context)) }
    var emergencyUnlock by remember { mutableStateOf(PreferencesManager.getEmergencyUnlockEnabled(context)) }
    var darkMode by remember { mutableStateOf(PreferencesManager.getDarkModeEnabled(context)) }
    var liveActivity by remember { mutableStateOf(PreferencesManager.getLiveActivityEnabled(context)) }
    var appDeletion by remember { mutableStateOf(PreferencesManager.getAppDeletionEnabled(context)) }
    var notifications by remember { mutableStateOf(PreferencesManager.getNotificationsEnabled(context)) }
    var language by remember { mutableStateOf(PreferencesManager.getLanguage(context)) }

    var showEditProfile by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(PreferencesManager.getProfileName(context)) }
    var email by remember { mutableStateOf(PreferencesManager.getProfileEmail(context)) }
    var profileImageUri by remember {
        mutableStateOf(
            PreferencesManager.getProfileImageUri(context)?.let { Uri.parse(it) }
        )
    }

    var showSuccess by remember { mutableStateOf(false) }
    var showHelpCenter by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    if (showHelpCenter) {
        HelpCenterScreen(onBack = { showHelpCenter = false })
        return
    }

    val blurRadius = if (showEditProfile || showLogoutDialog || showLanguageDialog) 20.dp else 0.dp

    val settingsOrder = listOf(
        "Block Timer" to "INTERFACE",
        "Emergency Unlock" to "INTERFACE",
        "Dark Mode" to "INTERFACE",
        "Live Activity" to "INTERFACE",
        "App Deletion" to "SETTINGS",
        "Notifications" to "SETTINGS",
        "Language" to "SETTINGS",
        "Help Center" to "SETTINGS",
        "Log Out" to "OTHER"
    )

    val visibleSettings = if (searchQuery.isBlank()) {
        settingsOrder
    } else {
        settingsOrder.filter { (label, _) ->
            label.contains(searchQuery, ignoreCase = true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .blur(blurRadius)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFDFDFD))
                            .clickable { isSearching = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = "Account",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(orange)
                            .clickable { showEditProfile = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit profile",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                AnimatedVisibility(visible = isSearching) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF2C2C2E))
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            placeholder = {
                                Text("Search settings", color = Color.White.copy(alpha = 0.4f))
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.White
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontSize = 13.sp
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier
                                .size(22.dp)
                                .clickable {
                                    searchQuery = ""
                                    isSearching = false
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(3.dp, orange, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUri != null) {
                            AsyncImage(
                                model = profileImageUri,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .background(orange, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "FA",
                                    color = Color.White,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(orange)
                            .padding(horizontal = 18.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = name,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = email,
                        color = Color(0xFFB0B0B0),
                        fontSize = 13.sp
                    )
                }


                Spacer(modifier = Modifier.height(28.dp))


                var lastSection = ""

                visibleSettings.forEach { (setting, section) ->

                    if (section != lastSection && section != "OTHER") {
                        Text(
                            text = section,
                            color = Color(0xFF888888),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
                        )
                        lastSection = section
                    }

                    when (setting) {
                        "Block Timer" -> SettingToggleRow(
                            icon = { Icon(Icons.Outlined.Schedule, null, tint = Color.White) },
                            label = "Block Timer",
                            checked = blockTimer,
                            onCheckedChange = {
                                blockTimer = it
                                PreferencesManager.setBlockTimerEnabled(context, it)
                            },
                            orange = orange
                        )

                        "Emergency Unlock" -> SettingToggleRow(
                            icon = { Icon(Icons.Outlined.Warning, null, tint = Color.White) },
                            label = "Emergency Unlock",
                            checked = emergencyUnlock,
                            onCheckedChange = {
                                emergencyUnlock = it
                                PreferencesManager.setEmergencyUnlockEnabled(context, it)
                            },
                            orange = orange
                        )

                        "Dark Mode" -> SettingToggleRow(
                            icon = { Icon(Icons.Outlined.DarkMode, null, tint = Color.White) },
                            label = "Dark Mode",
                            checked = darkMode,
                            onCheckedChange = {
                                darkMode = it
                                PreferencesManager.setDarkModeEnabled(context, it)
                            },
                            orange = orange
                        )

                        "Live Activity" -> SettingToggleRow(
                            icon = { Icon(Icons.Outlined.Bolt, null, tint = Color.White) },
                            label = "Live Activity",
                            checked = liveActivity,
                            onCheckedChange = {
                                liveActivity = it
                                PreferencesManager.setLiveActivityEnabled(context, it)
                            },
                            orange = orange,
                            subText = "See your session status on your Lock Screen.\nSilent notifications need to be enabled on the lock screen."
                        )

                        "App Deletion" -> SettingToggleRow(
                            icon = { Icon(Icons.Outlined.Delete, null, tint = Color.White) },
                            label = "App Deletion",
                            checked = appDeletion,
                            onCheckedChange = {
                                appDeletion = it
                                PreferencesManager.setAppDeletionEnabled(context, it)
                            },
                            orange = orange,
                            subText = "Prevent QOLT from being uninstalled."
                        )

                        "Notifications" -> SettingToggleRow(
                            icon = { Icon(Icons.Outlined.Notifications, null, tint = Color.White) },
                            label = "Notifications",
                            checked = notifications,
                            onCheckedChange = {
                                notifications = it
                                PreferencesManager.setNotificationsEnabled(context, it)
                            },
                            orange = orange
                        )

                        "Language" -> SettingRow(
                            icon = {
                                Icon(
                                    Icons.Outlined.Language,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = "Language",
                            trailingContent = {
                                Text(
                                    text = language,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    textDecoration = TextDecoration.Underline
                                )
                            },
                            onClick = { showLanguageDialog = true }
                        )

                        "Help Center" -> SettingRow(
                            icon = {
                                Icon(
                                    Icons.Outlined.SupportAgent,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = "Help Center",
                            onClick = { showHelpCenter = true }
                        )

                        "Log Out" -> Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showLogoutDialog = true }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.PowerSettingsNew,
                                contentDescription = null,
                                tint = Color(0xFFFF3B30),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Log Out",
                                color = Color(0xFFFF3B30),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        if (showEditProfile) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.30f))
            )
        }

        if (showEditProfile) {
            EditProfileOverlay(
                orange = orange,
                currentName = name,
                currentEmail = email,
                onSave = { newName, newEmail, newImageUri ->
                    name = newName
                    email = newEmail

                    PreferencesManager.setProfileName(context, newName)
                    PreferencesManager.setProfileEmail(context, newEmail)

                    if (newImageUri != null) {
                        PreferencesManager.setProfileImageUri(context, newImageUri.toString())
                        profileImageUri = newImageUri
                    }

                    showSuccess = true
                },


                        onDismiss = { showEditProfile = false }
            )
        }

        if (showSuccess) {
            SuccessPopup()
        }

        if (showLogoutDialog) {
            LogoutConfirmationDialog(
                onConfirm = {
                    showLogoutDialog = false
                    onLogout()
                },
                onCancel = { showLogoutDialog = false }
            )
        }

        if (showLanguageDialog) {
            LanguageSelectionDialog(
                currentLanguage = language,
                onSelect = { selected ->
                    language = selected
                    PreferencesManager.setLanguage(context, selected)
                    showLanguageDialog = false
                },
                onDismiss = { showLanguageDialog = false }
            )
        }

    }
}

@Composable
private fun SettingToggleRow(
    icon: @Composable () -> Unit,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    orange: Color,
    subText: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = label,
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = orange,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFF3A3A3C)
                )
            )
        }

        if (subText != null) {
            Text(
                text = subText,
                color = Color.White.copy(alpha = 0.55f),
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(start = 42.dp)
            )
        }
    }
}



@Composable
fun SettingRow(
    icon: @Composable () -> Unit,
    label: String,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(28.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = label,
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        trailingContent?.invoke()
    }
}

@Composable
private fun EditProfileOverlay(
    orange: Color,
    currentName: String,
    currentEmail: String,
    onSave: (String, String, Uri?) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var password by remember { mutableStateOf("") }

    var profileImageUri by remember {
        mutableStateOf(
            PreferencesManager.getProfileImageUri(context)?.let { Uri.parse(it) }
        )
    }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                profileImageUri = uri

                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: Exception) { }
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 65.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 20.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.width(24.dp))

                    Text(
                        text = "Edit Profile",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { onDismiss() }
                    )
                }

                Spacer(modifier = Modifier.height(48.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .clip(CircleShape)
                            .border(3.dp, orange, CircleShape)
                            .clickable { pickImageLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUri != null) {
                            AsyncImage(
                                model = profileImageUri,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1F1F21)),
                                contentAlignment = Alignment.Center
                            ) { }
                        }

                        Icon(
                            imageVector = Icons.Outlined.CameraAlt,
                            contentDescription = "Change Profile Picture",
                            tint = Color.White.copy(alpha = 0.75f),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    EditProfileField(
                        icon = Icons.Outlined.Person,
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Username"
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    EditProfileField(
                        icon = Icons.Outlined.Email,
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email"
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    EditProfileField(
                        icon = Icons.Outlined.Key,
                        value = password,
                        onValueChange = { password = it },
                        isPassword = true,
                        placeholder = "Password"
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(orange)
                        .clickable {
                            PreferencesManager.setProfileName(context, name)
                            PreferencesManager.setProfileEmail(context, email)

                            if (profileImageUri != null) {
                                PreferencesManager.setProfileImageUri(
                                    context,
                                    profileImageUri.toString()
                                )
                            }

                            onSave(name, email, profileImageUri)
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Save",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}




@Composable
fun EditProfileField(
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    placeholder: String = ""
) {
    Row(
        modifier = Modifier
            .width(327.dp)
            .height(54.dp)
            .shadow(
                elevation = 10.dp,
                spotColor = Color(0x1AFFFFFF),
                ambientColor = Color(0x1AFFFFFF),
                shape = RoundedCornerShape(48.dp)
            )
            .clip(RoundedCornerShape(48.dp))
            .border(1.dp, Color(0x4DFDFDFD), RoundedCornerShape(48.dp))
            .background(Color(0x4DFDFDFD), RoundedCornerShape(48.dp))
            .padding(start = 18.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,

            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.White.copy(alpha = 0.45f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400
                )
            },

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White
            ),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun SuccessPopup() {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        visible = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300))
        ) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .background(Color(0xFF28A745), RoundedCornerShape(50))
                    .padding(start = 24.dp, top = 6.dp, end = 24.dp, bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Changes Saved",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 40.dp)
                .align(Alignment.BottomCenter)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure you want to log out?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(22.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color(0xFFFF0000))
                        .clickable { onConfirm() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Log Out",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color(0xFFF1F1F1))
                        .clickable { onCancel() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }


    }
}

@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val orange = Color(0xFFFF6A1A)

    val languages = listOf(
        "English" to R.drawable.flag_uk,
        "Español" to R.drawable.flag_spain,
        "Français" to R.drawable.flag_france,
        "中文" to R.drawable.flag_china,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 22.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Language",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )

                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(26.dp)
                        .clickable { onDismiss() }
                )
            }

            Text(
                text = "Select your preferred language",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textAlign = TextAlign.Center
            )


            Text(
                text = "CURRENT",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            val currentFlag = languages.firstOrNull { it.first == currentLanguage }?.second

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(orange)
                    .clickable { onSelect(currentLanguage) }
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (currentFlag != null) {
                        AsyncImage(
                            model = currentFlag,
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = currentLanguage,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "MOST USED",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            languages.forEach { (lang, flag) ->

                if (lang == currentLanguage) return@forEach

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(36.dp))
                        .background(Color(0xFF3A3A3C))
                        .clickable { onSelect(lang) }
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = flag,
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = lang,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
