package ru.bgitu.feature.notes.presentation.details

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel
import ru.bgitu.core.designsystem.components.AppIconButton
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.icon.Edit
import ru.bgitu.core.designsystem.icon.More
import ru.bgitu.core.designsystem.icon.OpenBook
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.thenIf
import ru.bgitu.core.ui.AppBackButton
import ru.bgitu.core.ui.listenEvents
import ru.bgitu.feature.notes.R
import ru.bgitu.feature.notes.presentation.component.EditorActionsBottomSheet

@Composable
fun NoteDetailsRoute(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val viewModel = koinViewModel<NoteDetailsViewModel>()

    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val completeBeforeDate by viewModel.completeBeforeDate.collectAsStateWithLifecycle()
    val priority by viewModel.priority.collectAsStateWithLifecycle()

    viewModel.events.listenEvents { event ->
        when (event) {
            NoteDetailsEvent.Back -> onBack()
            is NoteDetailsEvent.Copy -> {
                clipboardManager.setClip(
                    ClipEntry(
                        ClipData.newPlainText(
                            runCatching { event.content.substring(0, 30) }.getOrDefault(event.content),
                            event.content
                        )
                    )
                )
            }
            NoteDetailsEvent.Delete -> onBack()
            is NoteDetailsEvent.Share -> context.launchShareIntent(event.content)
        }
    }

    AnimatedContent(
        targetState = loading,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { isLoading ->
        if (isLoading) {
            Spacer(Modifier.fillMaxSize())
        } else {
            NoteDetailsScreen(
                contentFieldState = viewModel.contentField,
                subjectNameFieldState = viewModel.subjectNameField,
                completeBeforeDate = completeBeforeDate,
                priority = priority,
                readModeInitially = viewModel.readModeInitially,
                onIntent = viewModel::onIntent
            )
        }
    }
}

@Composable
private fun NoteDetailsScreen(
    contentFieldState: TextFieldState,
    subjectNameFieldState: TextFieldState,
    completeBeforeDate: LocalDateTime?,
    priority: Int,
    readModeInitially: Boolean,
    onIntent: (NoteDetailsIntent) -> Unit,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    var readMode by rememberSaveable { mutableStateOf(readModeInitially) }
    var showMoreActionsBottomSheet by remember { mutableStateOf(false) }

    if (showMoreActionsBottomSheet) {
        EditorActionsBottomSheet(
            onDismissRequest = { showMoreActionsBottomSheet = false },
            onIntent = onIntent
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Column {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        AppBackButton(onClick = { onIntent(NoteDetailsIntent.Back) })
                    },
                    actions = {
                        AppIconButton(
                            onClick = {
                                readMode = !readMode
                            },
                            icon = if (readMode) AppIcons.OpenBook else AppIcons.Edit,
                            tint = AppTheme.colorScheme.foreground
                        )
                    },
                    modifier = Modifier
                        .thenIf(isLandscape) {
                            systemBarsPadding()
                        }
                )
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(AppTheme.strokeWidth.thin)
                        .background(AppTheme.colorScheme.stroke2)
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(AppTheme.colorScheme.background3)
                    .navigationBarsPadding()
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    AppIconButton(
                        onClick = {
                            showMoreActionsBottomSheet = true
                        },
                        iconPadding = PaddingValues(),
                        icon = AppIcons.More
                    )
                }
            }
        },
        modifier = Modifier
            .imePadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = AppTheme.spacing.l)
        ) {
            Spacer(Modifier.height(AppTheme.spacing.l))

            BasicTextField(
                state = subjectNameFieldState,
                readOnly = true,
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = AppTheme.typography.title3.copy(
                    color = AppTheme.colorScheme.foreground1
                )
            )

            Spacer(Modifier.height(AppTheme.spacing.s))

            NoteTextArea(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = contentFieldState,
                placeholder = stringResource(R.string.placeholder_text),
                readMode = readMode,
                onSaveRequest = { onIntent(NoteDetailsIntent.Save) }
            )
            Spacer(Modifier.height(AppTheme.spacing.xxxl))
        }
    }
}

@Composable
private fun NoteTextArea(
    modifier: Modifier,
    state: TextFieldState,
    placeholder: String,
    readMode: Boolean,
    onSaveRequest: () -> Unit,
) {
    val linkStyle = AppTheme.typography.body.copy(
        color = AppTheme.colorScheme.foreground,
        textDecoration = TextDecoration.Underline
    ).toSpanStyle()

    if (readMode) {
        val readModeText = buildAnnotatedString {
            append(state.text)
            val regex = "(https?://\\S+)".toRegex()
            for (match in regex.findAll(state.text)) {
                val startIndex = match.range.first
                val endIndex = match.range.first + match.value.length
                addLink(
                    url = LinkAnnotation.Url(match.value),
                    start = startIndex,
                    end = endIndex
                )
                addStyle(linkStyle, startIndex, endIndex)
            }
        }

        SelectionContainer {
            Text(
                text = readModeText,
                style = AppTheme.typography.body,
                color = AppTheme.colorScheme.foreground1,
                modifier = modifier
            )
        }
    } else {
        BasicTextField(
            state = state,
            textStyle = AppTheme.typography.body.copy(color = AppTheme.colorScheme.foreground1),
            lineLimits = TextFieldLineLimits.MultiLine(),
            decorator = { text ->
                if (state.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = AppTheme.colorScheme.foreground2,
                        style = AppTheme.typography.body,
                    )
                } else {
                    text()
                }
            },
            modifier = modifier
                .onFocusChanged { focus ->
                    if (!focus.isFocused) onSaveRequest()
                },
        )
    }
}

private fun Context.launchShareIntent(content: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }

    startActivity(Intent.createChooser(sendIntent, null))
}