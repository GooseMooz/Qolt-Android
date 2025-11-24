package ca.qolt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ca.qolt.ui.theme.QoltTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QoltTheme {

                // TODO: Change back to "onboarding" when done testing
                var currentScreen by remember { mutableStateOf("statistics") }

                Crossfade(targetState = currentScreen) { screen ->

                    when (screen) {

                        "onboarding" ->
                            OnboardingPager(
                                onFinished = { currentScreen = "qoltTag" }
                            )

                        "qoltTag" ->
                            QoltTagScreen(
                                onHaveTag = { currentScreen = "login" },
                                onNoTag = { currentScreen = "login" }
                            )

                        "login" -> LoginScreen(
                            onBack = { currentScreen = "qoltTag" },
                            onCreateAccount = { currentScreen = "createAccount" },
                            onForgotPassword = { currentScreen = "forgotPassword" },
                            onLogin = { currentScreen = "statistics" } // Demo: go to statistics after login
                        )

                        "createAccount" -> CreateAccountScreen(
                            onBack = { currentScreen = "login" },
                            onContinue = { /* TODO */ }
                        )

                        "forgotPassword" -> ForgotPasswordScreen(
                            onBack = { currentScreen = "login" },
                            onSendReset = { /* TODO */ },
                            onLoginClick = { currentScreen = "login" }
                        )
                        
                        "statistics" -> ca.qolt.ui.statistics.StatisticsScreen()
                    }
                }
            }
        }
    }
}
