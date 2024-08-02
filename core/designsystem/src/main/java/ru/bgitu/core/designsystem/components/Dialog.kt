package ru.bgitu.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.designsystem.util.thenIf

@Composable
fun AppDialogBase(
    title: String,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss ?: {},
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = AppTheme.colorScheme.background2,
                shape = AppTheme.shapes.default
            ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
            modifier = Modifier.padding(
                horizontal = AppTheme.spacing.xl,
                vertical = AppTheme.spacing.l
            )
        ) {
            Text(
                text = title,
                style = AppTheme.typography.title3,
                color = AppTheme.colorScheme.foreground1
            )
            content()
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                onDismiss?.let {
                    AppTextButton(
                        text = stringResource(android.R.string.cancel),
                        onClick = it,
                        color = AppTheme.colorScheme.foregroundError
                    )
                    Spacer(Modifier.width(AppTheme.spacing.s))
                }
                AppConfirmButton(
                    text = stringResource(android.R.string.ok),
                    onClick = onConfirm,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppConfirmDialog(
    title: String,
    text: String,
    headerImage: @Composable BoxScope.() -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(android.R.string.ok),
    cancelText: String = stringResource(android.R.string.cancel),
    confirmColor: Color = AppTheme.colorScheme.foreground,
    dismissColor: Color = AppTheme.colorScheme.foregroundError,
    onDismiss: (() -> Unit)? = null,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss ?: {},
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = AppTheme.colorScheme.background2,
                shape = AppTheme.shapes.default
            ),
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 128.dp)
                    .background(
                        color = AppTheme.colorScheme.backgroundTouchable.copy(alpha = 0.65f),
                        shape = AppTheme.shapes.defaultTopCarved
                    )
            ) {
                Box(Modifier.align(Alignment.Center), content = headerImage)
            }
            Spacer(Modifier.height(AppTheme.spacing.s))
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = AppTheme.spacing.l,
                        vertical = AppTheme.spacing.s
                    ),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.title3,
                    color = AppTheme.colorScheme.foreground1
                )
                Text(
                    text = text,
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foreground1
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    onDismiss?.let {
                        AppTextButton(
                            text = cancelText,
                            onClick = it,
                            color = dismissColor
                        )
                        Spacer(Modifier.width(AppTheme.spacing.s))
                    }
                    AppTextButton(
                        text = confirmText,
                        onClick = onConfirm,
                        color = confirmColor
                    )
                }
            }
        }
    }
}

@Composable
fun AppConfirmDialog(
    title: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(android.R.string.ok),
    cancelText: String = stringResource(android.R.string.cancel),
    confirmColor: Color = AppTheme.colorScheme.foreground,
    dismissColor: Color = AppTheme.colorScheme.foregroundError,
    text: String? = null,
    onDismiss: (() -> Unit)? = null,
    bottomAligned: Boolean = false,
    textAlign: TextAlign = TextAlign.Center
) {
    BasicAlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = onDismiss != null,
            dismissOnClickOutside = onDismiss != null,
            usePlatformDefaultWidth = false
        ),
        onDismissRequest = onDismiss ?: {},
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(AppTheme.spacing.xxl)
            .background(
                color = AppTheme.colorScheme.background2,
                shape = AppTheme.shapes.default
            )
            .thenIf(bottomAligned) {
                bottomAlignedDialog()
            },
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.spacing.l,
                    vertical = AppTheme.spacing.m
                ),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
        ) {
            Text(
                text = title,
                style = AppTheme.typography.title3,
                color = AppTheme.colorScheme.foreground1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
            text?.let {
                Text(
                    text = text,
                    style = AppTheme.typography.body,
                    color = AppTheme.colorScheme.foreground1,
                    textAlign = textAlign,
                    lineHeight = 25.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = AppTheme.spacing.l),
                )
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                val buttonModifier = Modifier
                    .weight(1f)
                    .height(54.dp)

                onDismiss?.let { onDismiss ->
                    AppDismissButton(
                        text = cancelText,
                        onClick = onDismiss,
                        contentColor = dismissColor,
                        modifier = buttonModifier
                    )
                    Spacer(Modifier.width(AppTheme.spacing.s))
                }
                AppConfirmButton(
                    text = confirmText,
                    onClick = onConfirm,
                    modifier = buttonModifier
                )
            }
        }
    }
}

@Composable
fun AppConfirmDialog(
    title: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(android.R.string.ok),
    cancelText: String = stringResource(android.R.string.cancel),
    confirmColor: Color = AppTheme.colorScheme.foreground,
    dismissColor: Color = AppTheme.colorScheme.foregroundError,
    onDismiss: (() -> Unit)? = null,
    bottomAligned: Boolean = false,
    content: @Composable () -> Unit
) {
    BasicAlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = onDismiss != null,
            dismissOnClickOutside = onDismiss != null,
            usePlatformDefaultWidth = false
        ),
        onDismissRequest = onDismiss ?: {},
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(AppTheme.spacing.xxl)
            .background(
                color = AppTheme.colorScheme.background2,
                shape = AppTheme.shapes.default
            )
            .thenIf(bottomAligned) {
                bottomAlignedDialog()
            },
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.spacing.m,
                    vertical = AppTheme.spacing.m
                ),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
        ) {
            Text(
                text = title,
                style = AppTheme.typography.title3,
                color = AppTheme.colorScheme.foreground1,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
            content()
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                val buttonModifier = Modifier
                    .weight(1f)
                    .height(54.dp)

                onDismiss?.let { onDismiss ->
                    AppDismissButton(
                        text = cancelText,
                        onClick = onDismiss,
                        contentColor = dismissColor,
                        modifier = buttonModifier
                    )
                    Spacer(Modifier.width(AppTheme.spacing.s))
                }
                AppConfirmButton(
                    text = confirmText,
                    onClick = onConfirm,
                    modifier = buttonModifier
                )
            }
        }
    }
}

fun Modifier.bottomAlignedDialog() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints);
    layout(constraints.maxWidth, constraints.maxHeight){
        placeable.place(0, constraints.maxHeight - placeable.height, 10f)
    }
}