package ru.bgitu.feature.about.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch
import ru.bgitu.core.data.repository.CompassRepository

const val FIRST_VERSION_WITH_CHANGELOGS = 5

class AboutViewModel(
    private val compassRepository: CompassRepository,
    private val currentVersionCode: Long
) : ViewModel() {

    private val loadedPages = mutableSetOf<Int>()
    val paginationState = PaginationState<Int, String?>(
        initialPageKey = 1,
        onRequestPage = { loadPage(it) }
    )

    private fun loadPage(page: Int) {
        if (page in loadedPages) {
            return
        }
        loadedPages.add(page)

        viewModelScope.launch {
            val appVersion = currentVersionCode - (page - 1)
            val nextPageKey = page + 1
            val isLastPage = FIRST_VERSION_WITH_CHANGELOGS == appVersion.toInt()

            compassRepository.getChangelog(appVersion)
                .onSuccess { changelogMd ->
                    paginationState.appendPage(
                        items = listOf(changelogMd),
                        nextPageKey = nextPageKey,
                        isLastPage = isLastPage
                    )
                }
                .onFailure {
                    paginationState.appendPage(
                        items = listOf(null),
                        nextPageKey = nextPageKey,
                        isLastPage = isLastPage
                    )
                }
        }
    }
}