package com.fenchtose.movieratings.features.baselistpage

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.fenchtose.movieratings.R
import com.fenchtose.movieratings.analytics.ga.AppEvents
import com.fenchtose.movieratings.base.AppState
import com.fenchtose.movieratings.base.BaseFragment
import com.fenchtose.movieratings.base.BaseMovieAdapter
import com.fenchtose.movieratings.base.redux.Action
import com.fenchtose.movieratings.base.redux.Dispatch
import com.fenchtose.movieratings.base.router.Navigation
import com.fenchtose.movieratings.features.moviepage.MoviePath
import com.fenchtose.movieratings.features.searchpage.MovieLazyList
import com.fenchtose.movieratings.features.searchpage.SearchItemViewHolder
import com.fenchtose.movieratings.model.db.like.LikeMovie
import com.fenchtose.movieratings.model.entity.Movie
import com.fenchtose.movieratings.model.image.GlideLoader
import com.google.accompanist.appcompattheme.AppCompatTheme

abstract class BaseMovieListPageFragment : BaseFragment() {

    private var recyclerView: RecyclerView? = null
    private var adapter: BaseMovieAdapter? = null
    private lateinit var composeRecyclerView: ComposeView

    private var stateContent: TextView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreated()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeRecyclerView = view.findViewById(R.id.compose_recyclerview)
        recyclerView = view.findViewById(R.id.recyclerview)
        stateContent = view.findViewById(R.id.screen_state_content)
        progressBar = view.findViewById(R.id.progressbar)

        val adapter = BaseMovieAdapter(requireContext(), createAdapterConfig())
        adapter.setHasStableIds(true)

        recyclerView?.let {
            it.adapter = adapter
            it.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            it.visibility = View.GONE
        }

        this.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        render { appState, dispatch ->
            render(reduceState(appState), dispatch)
            render(appState, dispatch)
        }
        dispatch?.invoke(loadingAction())
    }

    private fun render(state: BaseMovieListPageState, dispatch: Dispatch) {
        progressBar?.visibility = View.GONE
        when (state.progress) {
            is Progress.Loading -> {
                progressBar?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
                composeRecyclerView.isVisible = false
            }

            is Progress.Error -> showContentState(getErrorContent())
            is Progress.Success -> if (state.movies.isEmpty()) showContentState(getEmptyContent()) else setData(
                state.movies
            )
        }
    }

    private fun showContentState(resId: Int) {
        recyclerView?.visibility = View.GONE
        stateContent?.visibility = View.VISIBLE
        stateContent?.setText(resId)
        composeRecyclerView.isVisible = false
    }

    private fun setData(movies: List<Movie>) {
        adapter?.data?.clear()
        adapter?.data?.addAll(movies)
        adapter?.notifyDataSetChanged()
        stateContent?.visibility = View.GONE
        recyclerView?.visibility = View.VISIBLE

        recyclerView?.isVisible = false
        composeRecyclerView.apply {
            isVisible = true
            setContent {
                AppCompatTheme() {
                    MovieLazyList(movies = movies, onMovieLiked = ::toggleLike) { movie -> openMovie(movie, null)}
                }
            }
        }
    }

    open fun getLayout(): Int {
        return R.layout.base_movies_list_page_layout
    }

    abstract fun getErrorContent(): Int

    abstract fun getEmptyContent(): Int

    abstract fun reduceState(appState: AppState): BaseMovieListPageState

    abstract fun loadingAction(): Action

    open fun createAdapterConfig(): BaseMovieAdapter.AdapterConfig {
        val glide = GlideLoader(Glide.with(this))
        return BaseMovieListAdapterConfig(
            ::toggleLike,
            ::openMovie,
            glide,
            createExtraLayoutHelper()
        )
    }

    open fun createExtraLayoutHelper(): (() -> SearchItemViewHolder.ExtraLayoutHelper)? = null

    protected open fun onCreated() {

    }

    protected open fun render(appState: AppState, dispatch: Dispatch) {

    }

    protected open fun toggleLike(movie: Movie) {
        AppEvents.like(path?.category(), !movie.liked).track()
        dispatch?.invoke(LikeMovie(movie, !movie.liked))
    }

    protected open fun openMovie(movie: Movie, sharedElement: Pair<View, String>?) {
        AppEvents.openMovie(path?.category() ?: "unknown").track()
        path?.getRouter()?.let {
            dispatch?.invoke(Navigation(it, MoviePath(movie, sharedElement)))
        }
    }
}