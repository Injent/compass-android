package ru.bgitu.feature.professor_search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.bgitu.core.designsystem.components.AppTextButton
import ru.bgitu.core.designsystem.icon.AppIcons
import ru.bgitu.core.designsystem.theme.AppTheme
import ru.bgitu.core.ui.onClick
import ru.bgitu.feature.professor_search.R
import ru.bgitu.feature.professor_search.presentation.search.TeacherSearchUiState

@Composable
fun RecentProfessorSearch(
    uiState: TeacherSearchUiState,
    onClick: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.recent_search_results),
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foreground3
            )
            AppTextButton(
                text = stringResource(R.string.clear_search),
                onClick = onClear
            )
        }
        Spacer(Modifier.height(AppTheme.spacing.xs))
        uiState.recentSearchResults.forEach { professorName ->
            RecentItem(
                label = professorName,
                onClick = { onClick(professorName) },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun RecentItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Transparent,
        shape = AppTheme.shapes.default,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(AppTheme.spacing.s),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
        ) {
            Icon(
                painter = painterResource(AppIcons.Recent),
                tint = AppTheme.colorScheme.foreground2,
                contentDescription = null,
                modifier = Modifier.size(AppTheme.spacing.xl)
            )
            Text(
                text = label,
                style = AppTheme.typography.callout,
                color = AppTheme.colorScheme.foreground1
            )
        }
    }
}