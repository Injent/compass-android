package ru.bgitu.feature.about.presentation

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.DrawFilter
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.common.DOWNLOAD_APP_URL
import ru.bgitu.core.common.USER_AGREEMENT_URL
import ru.bgitu.core.common.openUrl
import ru.bgitu.core.common.versionCode
import ru.bgitu.core.common.versionName
import ru.bgitu.core.designsystem.components.AppCircularLoading
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.designsystem.theme.end
import ru.bgitu.core.designsystem.theme.start
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.navigation.back
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.feature.about.R
import ru.bgitu.feature.about.components.Changelog
import ru.bgitu.feature.about.components.DevsList
import java.util.concurrent.TimeUnit


@Composable
fun AboutScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val viewModel: AboutViewModel = koinViewModel()

    val changelogPagination = viewModel.paginationState

    AboutScreenContent(
        onBack = { navController.back() },
        versionName = "${context.versionName} (${context.versionCode})",
        paginationState = changelogPagination
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreenContent(
    onBack: () -> Unit,
    versionName: String,
    paginationState: PaginationState<Int, String?>,
) {
    val context = LocalContext.current
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val appVersion = context.versionCode
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ConfettiDialog(
            onDismiss = { showDialog = false }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.about_app),
                        style = AppTheme.typography.title3,
                        color = AppTheme.colorScheme.foreground1
                    )
                },
                navigationIcon = {
                    AppBackButton(
                        onClick = onBack,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.background3
                )
            )
        },
        modifier = Modifier
            .padding(start = LocalExternalPadding.current.start),
    ) { paddingValues ->
        PaginatedLazyColumn(
            paginationState = paginationState,
            contentPadding = PaddingValues(
                start = if (isLandscape) AppTheme.spacing.l else 0.dp,
                end = WindowInsets.navigationBars.asPaddingValues().end,
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + AppTheme.spacing.l
            ),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            newPageProgressIndicator = {
                AppCircularLoading(modifier = Modifier)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = AppTheme.spacing.s)
                        .padding(top = 56.dp)
                ) {
                    Image(
                        painter = painterResource(ru.bgitu.core.common.R.drawable.app_logo) ,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null,
                            ) { showDialog = true }
                    )
                    Spacer(Modifier.height(AppTheme.spacing.xxxl))
                    Text(
                        text = versionName,
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foreground2
                    )
                    Spacer(Modifier.height(AppTheme.spacing.xxxl))
                    Text(
                        text = stringResource(R.string.app_description),
                        style = AppTheme.typography.body,
                        color = AppTheme.colorScheme.foreground1,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(AppTheme.spacing.l))

                    Text(
                        text = buildAnnotatedString {
                            withLink(
                                link = LinkAnnotation.Clickable(
                                    tag = "click_user_agreement",
                                    linkInteractionListener = { context.openUrl(USER_AGREEMENT_URL) }
                                )
                            ) {
                                append(context.getString(R.string.user_agreement))
                            }
                        },
                        color = AppTheme.colorScheme.foreground,
                        fontWeight = FontWeight.SemiBold,
                        style = AppTheme.typography.callout
                    )

                    Spacer(Modifier.height(AppTheme.spacing.l))

                    Text(
                        text = buildAnnotatedString {
                            withLink(
                                link = LinkAnnotation.Clickable(
                                    tag = "click_share",
                                    linkInteractionListener = { context.shareAppLink() }
                                )
                            ) {
                                append(context.getString(R.string.share_app))
                            }
                        },
                        color = AppTheme.colorScheme.foreground,
                        fontWeight = FontWeight.SemiBold,
                        style = AppTheme.typography.callout
                    )
                    Spacer(Modifier.height(AppTheme.spacing.l))
                    DevsList()
                    Spacer(Modifier.height(AppTheme.spacing.xxxl * 2))
                    Text(
                        text = stringResource(R.string.changelogs),
                        style = AppTheme.typography.title2,
                        color = AppTheme.colorScheme.foreground1
                    )
                }
            }
            itemsIndexed(
                paginationState.allItems!!
            ) { index, changelogMd ->
                if (changelogMd != null) {
                    Changelog(
                        markdown = changelogMd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                AppTheme.colorScheme.background1,
                                AppTheme.shapes.default
                            )
                            .padding(AppTheme.spacing.l)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.noChangelogFor, appVersion - index),
                        style = AppTheme.typography.callout,
                        color = AppTheme.colorScheme.foreground3,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ConfettiDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        properties = DialogProperties(
            decorFitsSystemWindows = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = null,
                    indication = null
                ) { onDismiss() }
        ) {
            var konfetti by remember {
                mutableStateOf<Party?>(
                    Party(
                        size = listOf(Size(sizeInDp = 20)),
                        emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30),
                    )
                )
            }
            konfetti?.let {
                KonfettiView(
                    parties = listOf(it),
                    updateListener = object : OnParticleSystemUpdateListener {
                        override fun onParticleSystemEnded(system: PartySystem, activeSystems: Int) {
                            konfetti = null
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = (-32).dp),
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l),
            ) {
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            setImageDrawable(
                                AliasingDrawableWrapper(
                                    AppCompatResources.getDrawable(
                                        context,
                                        R.drawable.compass
                                    )!!
                                )
                            )
                        }
                    },
                    modifier = Modifier.size(200.dp)
                ) {}
                Text(
                    text = stringResource(R.string.easter),
                    style = AppTheme.typography.title1,
                    color = AppTheme.colorScheme.foreground1
                )
            }
        }
    }
}

private fun Context.shareAppLink() {
    val share = Intent.createChooser(Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_message, DOWNLOAD_APP_URL))
    }, "")
    startActivity(share)
}

private class AliasingDrawableWrapper(wrapped: Drawable) : DrawableWrapper(wrapped) {
    override fun draw(canvas: Canvas) {
        val oldDrawFilter = canvas.drawFilter
        canvas.drawFilter = DRAW_FILTER
        super.draw(canvas)
        canvas.drawFilter = oldDrawFilter
    }

    companion object {
        private val DRAW_FILTER: DrawFilter = PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG, 0)
    }
}