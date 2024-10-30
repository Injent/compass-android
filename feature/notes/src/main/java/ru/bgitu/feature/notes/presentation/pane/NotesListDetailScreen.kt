package ru.bgitu.feature.notes.presentation.pane

import androidx.activity.compose.BackHandler
import androidx.annotation.Keep
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.PaneAdaptedValue
import androidx.compose.material3.adaptive.PaneScaffoldDirective
import androidx.compose.material3.adaptive.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.calculateStandardPaneScaffoldDirective
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.theme.LocalExternalPadding
import ru.bgitu.core.navigation.Screen
import ru.bgitu.feature.notes.R
import ru.bgitu.feature.notes.presentation.details.NoteDetailsRoute
import ru.bgitu.feature.notes.presentation.list.NotesRoute
import java.util.UUID

@Serializable internal object NotePlaceholderRoute

@Keep
@Serializable internal object DetailPaneNavHostRoute


@Composable
fun NotesListDetailRoute() {
    val viewModel = koinViewModel<Notes2PaneViewModel>()


    NotesListDetailScreen(
        selectedNoteId = null,
        onNoteClick = {}
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NotesListDetailScreen(
    selectedNoteId: Int?,
    onNoteClick: (Int) -> Unit
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val systemDirective = calculateStandardPaneScaffoldDirective(windowAdaptiveInfo)
    val customDirective = PaneScaffoldDirective(
        contentPadding = PaddingValues(0.dp),
        maxHorizontalPartitions = systemDirective.maxHorizontalPartitions,
        horizontalPartitionSpacerSize = 0.dp,
        maxVerticalPartitions = systemDirective.maxVerticalPartitions,
        verticalPartitionSpacerSize = systemDirective.verticalPartitionSpacerSize,
        excludedBounds = systemDirective.excludedBounds
    )
    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = customDirective,
        initialDestinationHistory = listOfNotNull(
            ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List),
            ThreePaneScaffoldDestinationItem<Nothing>(ListDetailPaneScaffoldRole.Detail).takeIf {
                selectedNoteId != null
            }
        )
    )
    BackHandler(listDetailNavigator.canNavigateBack()) {
        listDetailNavigator.navigateBack()
    }

    var nestedNavHostStartRoute by remember {
        val route = selectedNoteId?.let { Screen.NoteDetails(noteId = it) } ?: NotePlaceholderRoute
        mutableStateOf(route)
    }

    var nestedNavKey by rememberSaveable(
        stateSaver = Saver({ it.toString() }, UUID::fromString)
    ) {
        mutableStateOf(UUID.randomUUID())
    }
    val nestedNavController = key(nestedNavKey) {
        rememberNavController()
    }

    fun onNoteClickShowDetailPane(noteId: Int) {
        onNoteClick(noteId)
        if (listDetailNavigator.isDetailPaneVisible()) {
            nestedNavController.navigate(Screen.NoteDetails(noteId)) {
                popUpTo<DetailPaneNavHostRoute>()
            }
        } else {
            nestedNavHostStartRoute = Screen.NoteDetails(noteId = noteId)
            nestedNavKey = UUID.randomUUID()
        }
        listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
    }

    ListDetailPaneScaffold(
        modifier = Modifier.padding(LocalExternalPadding.current),
        scaffoldState = listDetailNavigator.scaffoldState,
        listPane = {
            NotesRoute(
                onNoteClick = ::onNoteClickShowDetailPane
            )
        },
        detailPane = {
            key(nestedNavKey) {
                NavHost(
                    navController = nestedNavController,
                    startDestination = nestedNavHostStartRoute,
                    route = DetailPaneNavHostRoute::class,
                    modifier = Modifier
                ) {
                    composable<Screen.NoteDetails> {
                        NoteDetailsRoute(
                            onBack = { listDetailNavigator.navigateBack() }
                        )
                    }
                    composable<NotePlaceholderRoute> {
                        NotePlaceholderScreen()
                    }
                }
            }
        },
    )
}

@Composable
fun NotePlaceholderScreen() {
    val density = LocalDensity.current
    val shape = AppTheme.shapes.default
    val stroke = Stroke(
        width = density.run { AppTheme.strokeWidth.thin.toPx() },
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    val borderColor = AppTheme.colorScheme.foreground2

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(AppTheme.spacing.l)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    style = stroke,
                    cornerRadius = CornerRadius(shape.topStart.toPx(size, density))
                )
            }
    ) {
        Text(
            text = stringResource(R.string.click_on_card_to_add_note),
            style = AppTheme.typography.body,
            color = AppTheme.colorScheme.foreground2,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 150.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isListPaneVisible(): Boolean =
    scaffoldState.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isDetailPaneVisible(): Boolean =
    scaffoldState.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
