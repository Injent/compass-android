package ru.bgitu.app.crashscreen

import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.bgitu.app.R
import ru.bgitu.core.designsystem.components.AppSmallButton
import ru.bgitu.core.designsystem.illustration.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.CompassTheme

class CrashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        if (SDK_INT in 29..34) {
            @Suppress("DEPRECATION")
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            CompassTheme {
                CrashScreen()
            }
        }
    }

    @Composable
    private fun CrashScreen() {
        Surface(
            color = AppTheme.colorScheme.background2,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
                modifier = Modifier
                    .padding(AppTheme.spacing.l)
                    .systemBarsPadding()
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(AppIllustrations.Broken),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 600.dp)
                            .heightIn(max = 300.dp)
                            .fillMaxHeight()

                    )

                    Text(
                        text = stringResource(R.string.error_has_occurred),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )

                    Text(
                        text = stringResource(R.string.we_apologize),
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foreground4,
                        textAlign = TextAlign.Center
                    )
                }

                AppSmallButton(
                    text = stringResource(R.string.relaunch_app),
                    onClick = { restartApp() },
                    modifier = Modifier
                        .width(200.dp)
                )
            }
        }
    }

    private fun Activity.restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val mainIntent = Intent.makeRestartActivityTask(intent?.component)
        startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}