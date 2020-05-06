package com.igorwojda.showcase.feature.album.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.igorwojda.showcase.feature.album.domain.model.AlbumDomainModel
import com.igorwojda.showcase.feature.album.domain.usecase.GetAlbumListUseCase
import com.igorwojda.showcase.feature.album.presentation.albumlist.AlbumListFragmentDirections
import com.igorwojda.showcase.feature.album.presentation.albumlist.AlbumListViewModel
import com.igorwojda.showcase.feature.album.presentation.albumlist.AlbumListViewModel.ViewState
import com.igorwojda.showcase.library.base.presentation.navigation.NavigationManager
import com.igorwojda.showcase.library.testutils.CoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AlbumListViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @MockK
    internal lateinit var mockGetAlbumSearchUseCase: GetAlbumListUseCase

    @MockK(relaxed = true)
    internal lateinit var mockNavigationManager: NavigationManager

    private lateinit var cut: AlbumListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        cut = AlbumListViewModel(
            mockNavigationManager,
            mockGetAlbumSearchUseCase
        )
    }

    @Test
    fun `execute getAlbumUseCase`() {
        // when
        cut.loadData()

        // then
        coVerify { mockGetAlbumSearchUseCase.execute() }
    }

    @Test
    fun `navigate to album details`() {
        // given
        val artistName = "artistName"
        val albumName = "albumName"
        val mbId = "mbId"

        val navDirections = AlbumListFragmentDirections.actionAlbumListToAlbumDetail(
            artistName,
            albumName,
            mbId
        )

        // when
        cut.navigateToAlbumDetails(artistName, albumName, mbId)

        // then
        coVerify { mockNavigationManager.navigate(navDirections) }
    }

    @Test
    fun `verify state when GetAlbumSearchUseCase returns empty list`() {
        // given
        coEvery { mockGetAlbumSearchUseCase.execute() } returns listOf()

        // when
        cut.loadData()

        // then
        cut.stateLiveData.value shouldBeEqualTo ViewState(
            isLoading = false,
            isError = true,
            albums = listOf()
        )
    }

    @Test
    fun `verify state when GetAlbumSearchUseCase returns non-empty list`() {
        // given
        val album = AlbumDomainModel("albumName", "artistName", listOf())
        val albums = listOf(album)
        coEvery { mockGetAlbumSearchUseCase.execute() } returns albums

        // when
        cut.loadData()

        // then
        cut.stateLiveData.value shouldBeEqualTo ViewState(
            isLoading = false,
            isError = false,
            albums = albums
        )
    }
}
