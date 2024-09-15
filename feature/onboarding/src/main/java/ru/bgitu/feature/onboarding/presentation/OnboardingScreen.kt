package ru.bgitu.feature.onboarding.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.icon.AppIllustrations
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.navigation.LocalNavController
import ru.bgitu.core.ui.MatesBannerContent
import ru.bgitu.core.ui.onClick
import ru.bgitu.feature.onboarding.R

@Composable
fun OnboardingRoute() {
    val viewModel = koinViewModel<OnboardingViewModel>()

    OnboardingScreen(
        onSkip = viewModel::onSkip
    )
}

@Composable
private fun OnboardingScreen(
    onSkip: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 3 }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.spacing.xxl)
                    .navigationBarsPadding()
            ) {
                AppTextButton(
                    text = stringResource(R.string.action_skip),
                    color = AppTheme.colorScheme.foreground2,
                    onClick = onSkip,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                PageIndicator(
                    pagerState = pagerState,
                    modifier = Modifier.align(Alignment.Center)
                )

                if (pagerState.currentPage < pagerState.pageCount - 1) {
                    AppTextButton(
                        text = stringResource(R.string.action_next),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    page = (pagerState.currentPage + 1)
                                        .coerceAtMost(pagerState.pageCount - 1)
                                )
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> WidgetAndNotificationPage()
                1 -> WidgetAndNotificationPage()
                2 -> WidgetAndNotificationPage()
            }
        }
    }
}

@Composable
private fun WidgetAndNotificationPage(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
    ) {
        MatesBannerContent()
    }
}

@Composable
private fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
        modifier = modifier
    ) {
        repeat(pagerState.pageCount) { page ->
            Spacer(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = if (pagerState.currentPage == page) {
                            AppTheme.colorScheme.foreground
                        } else AppTheme.colorScheme.foreground4,
                        shape = CircleShape
                    )
                    .onClick {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page)
                        }
                    }
            )
        }
    }
}