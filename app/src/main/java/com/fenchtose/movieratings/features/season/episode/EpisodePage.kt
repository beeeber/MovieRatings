package com.fenchtose.movieratings.features.season.episode

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.fenchtose.movieratings.MovieRatingsApplication
import com.fenchtose.movieratings.R
import com.fenchtose.movieratings.analytics.ga.GaCategory
import com.fenchtose.movieratings.analytics.ga.AppEvents
import com.fenchtose.movieratings.base.redux.Dispatch
import com.fenchtose.movieratings.base.redux.Unsubscribe
import com.fenchtose.movieratings.model.entity.Episode
import com.fenchtose.movieratings.model.entity.Movie
import com.fenchtose.movieratings.util.IntentUtils
import com.fenchtose.movieratings.util.show
import com.fenchtose.movieratings.widgets.ThemedSnackbar
import com.fenchtose.movieratings.widgets.pagesection.ExpandableSection
import com.fenchtose.movieratings.widgets.pagesection.InlineTextSection
import com.fenchtose.movieratings.widgets.pagesection.SimpleTextSection
import com.fenchtose.movieratings.widgets.pagesection.TextSection

@SuppressLint("ViewConstructor")
class EpisodePage(context: Context, private val episode: Episode,
                  private val series: Movie): FrameLayout(context) {

    private val progressbar: ProgressBar
    private val content: ViewGroup

    private val titleSection: SimpleTextSection
    private val seriesTitleSection: SimpleTextSection
    private val ratingSection: SimpleTextSection
    private val genreSection: SimpleTextSection
    private val directorSection: InlineTextSection
    private val releaseSection: InlineTextSection
    private val actorSection: TextSection
    private val writerSection: TextSection
    private val plotSection: ExpandableSection

    private val imdbCta: View

    var onLoaded: ((Movie) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.episode_page_layout, this, true)
        progressbar = findViewById(R.id.progressbar)
        content = findViewById(R.id.episode_content)
        titleSection = SimpleTextSection(findViewById(R.id.title_view))
        seriesTitleSection = SimpleTextSection(findViewById(R.id.series_view))
        ratingSection = SimpleTextSection(findViewById(R.id.rating_view), R.string.episode_page_rating)
        genreSection = SimpleTextSection(findViewById(R.id.genre_view))
        directorSection = InlineTextSection(findViewById(R.id.director_view), R.string.episode_page_directed_by)
        releaseSection = InlineTextSection(findViewById(R.id.released_view), R.string.episode_page_released_on)
        actorSection = TextSection(findViewById(R.id.actors_header), findViewById(R.id.actors_view))
        writerSection = TextSection(findViewById(R.id.writers_header), findViewById(R.id.writers_view))
        plotSection = ExpandableSection(findViewById(R.id.plot_header),
                findViewById(R.id.plot_toggle),
                findViewById(R.id.plot_view),
                AppEvents.togglePlot("expand", category()),
                AppEvents.togglePlot("collapse", category()))

        imdbCta = findViewById(R.id.imdb_cta)

        setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
    }

    private var dispatch: Dispatch? = null
    private var unsubscribe: Unsubscribe? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        MovieRatingsApplication.store.dispatchEarly(EpisodePageAction.InitEpisodePage(episode))
        unsubscribe = MovieRatingsApplication.store.subscribe { appState, dispatch ->
            this.dispatch = dispatch
            appState.episodePages[episode.imdbId]?.let {
                render(it, dispatch)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unsubscribe?.invoke()
        dispatch = null
    }

    private fun render(state: EpisodePageState, dispatch: Dispatch) {
        if (state.episode.imdbId.isEmpty() && state.progress === Progress.Default) {
            dispatch(EpisodePageAction.LoadEpisodePage(episode.imdbId))
        }

        when(state.progress) {
            is Progress.Loading -> showLoading()
            is Progress.Loaded -> showEpisode(state.episode)
            is Progress.Error -> showError()
        }
    }

    private fun showLoading() {
        progressbar.visibility = View.VISIBLE
        content.visibility = View.GONE
    }

    private fun showError() {
        progressbar.visibility = View.GONE
        ThemedSnackbar.makeWithAction(this,
                R.string.episode_page_loading_error,
                Snackbar.LENGTH_LONG,
                R.string.episode_page_retry_cta,
                View.OnClickListener {
                    dispatch?.invoke(EpisodePageAction.LoadEpisodePage(episode.imdbId))
                }
        ).show()
    }

    private fun showEpisode(episode: Movie) {

        progressbar.visibility = View.GONE
        content.visibility = View.VISIBLE

        titleSection.setContent(episode.title)
        seriesTitleSection.setContent(context.getString(R.string.episode_page_series_title, series.title, this.episode.season))
        ratingSection.setContent(episode.ratings.firstOrNull()?.rating)
        genreSection.setContent(episode.genre)
        directorSection.setContent(" ${episode.director}")
        releaseSection.setContent(" ${episode.released}")
        actorSection.setContent(episode.actors)
        writerSection.setContent(episode.writers)
        plotSection.setContent(episode.plot)
        imdbCta.setOnClickListener {
            AppEvents.openImdb(GaCategory.EPISODE).track()
            IntentUtils.openImdb(context, episode.imdbId)
        }

        onLoaded?.invoke(episode)

    }

    private fun category() = GaCategory.EPISODE

}