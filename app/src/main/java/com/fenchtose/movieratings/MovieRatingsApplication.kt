package com.fenchtose.movieratings

import android.app.Application
import com.fenchtose.movieratings.analytics.AnalyticsDispatcher
import com.fenchtose.movieratings.base.redux.AppStore
import com.fenchtose.movieratings.model.api.provider.MovieProviderModule
import com.fenchtose.movieratings.model.api.provider.MovieRatingProviderModule
import com.fenchtose.movieratings.model.db.MovieDb
import com.fenchtose.movieratings.model.gsonadapters.IntAdapter
import com.fenchtose.movieratings.util.registerNotificationChannel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient

open class MovieRatingsApplication : Application() {

    companion object {
        lateinit var instance: MovieRatingsApplication

        val flavorHelper: AppFlavorHelper = AppFlavorHelper()

        val database by lazy {
            MovieDb.instance
        }

        val analyticsDispatcher: AnalyticsDispatcher by lazy {
            AnalyticsDispatcher()
                    .attachDispatcher("ga", flavorHelper.getGaDispatcher())
        }

        val gson: Gson by lazy {
            GsonBuilder()
                    .setDateFormat("dd MM yyyy")
                    .registerTypeAdapter(Int::class.java, IntAdapter())
                    .setPrettyPrinting()
                    .create()
        }

        val movieProviderModule by lazy {
            MovieProviderModule(instance, gson)
        }

        val ratingProviderModule by lazy {
            MovieRatingProviderModule(instance, gson)
        }

        val store by lazy { AppStore(instance) }
    }

    open fun getOkHttpClient(cache: Cache? = null, interceptors: List<Interceptor> = listOf(),
                             networkInterceptors: List<Interceptor> = listOf()): OkHttpClient {
        val builder = OkHttpClient.Builder()
        interceptors.forEach { builder.addInterceptor(it) }
        networkInterceptors.forEach { builder.addNetworkInterceptor(it) }
        cache?.apply { builder.cache(this) }
        return builder.build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        flavorHelper.onAppCreated(this)
        registerNotificationChannel(this)
    }
}