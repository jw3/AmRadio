package com.github.jw3.amradio

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import com.burgstaller.okhttp.digest.Credentials
import com.burgstaller.okhttp.digest.DigestAuthenticator
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import kotlinx.android.synthetic.main.activity_feed_view.*
import okhttp3.OkHttpClient

class FeedViewActivity : Activity() {
    var p1: SimpleExoPlayer? = null
    var p2: SimpleExoPlayer? = null
    var p3: SimpleExoPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_view)

        switch1.setOnCheckedChangeListener { _, play ->
            val p = P1()
            p.playWhenReady = play
            if (play) p.retry()
            else p.stop()

        }

        switch2.setOnCheckedChangeListener { _, play ->
            val p = P2()
            p.playWhenReady = play
            if (play) p.retry()
            else p.stop()

        }

        switch3.setOnCheckedChangeListener { _, play ->
            val p = P3()
            p.playWhenReady = play
            if (play) p.retry()
            else p.stop()

        }
    }

    fun playerFor(u: String, p: String, host: String): SimpleExoPlayer {
        val credentials = Credentials(u, p)
        val digestAuthenticator = DigestAuthenticator(credentials)

        val builder = OkHttpClient.Builder()
        builder.authenticator(digestAuthenticator)
        val f = OkHttpDataSourceFactory(builder.build(), null)
        val s = ExtractorMediaSource.Factory(f).createMediaSource(Uri.parse(host))

        val p = ExoPlayerFactory.newSimpleInstance(this)
        p.prepare(s)

        return p
    }

    // http://192.168.1.123/cgi-bin/audio.cgi?action=getAudio&httptype=singlepart&channel=1
    fun P1(): SimpleExoPlayer {
        if (p1 == null) {
            p1 = playerFor(
                    prefOrUnknown("preference_user"),
                    prefOrUnknown("preference_pass"),
                    prefOrUnknown("preference_url_cam1")
            )
        }
        return p1!!
    }

    fun P2(): SimpleExoPlayer {
        if (p2 == null) {
            p2 = playerFor(
                    prefOrUnknown("preference_user"),
                    prefOrUnknown("preference_pass"),
                    prefOrUnknown("preference_url_cam2")
            )
        }
        return p2!!
    }

    fun P3(): SimpleExoPlayer {
        if (p3 == null) {
            p3 = playerFor(
                    prefOrUnknown("preference_user"),
                    prefOrUnknown("preference_pass"),
                    prefOrUnknown("preference_url_cam3")
            )
        }
        return p3!!
    }

    private fun prefs(): SharedPreferences {
        return getPreferences(Context.MODE_PRIVATE)
    }

    private fun prefOrUnknown(k: String): String {
        return prefs().getString(k, "unknown")
    }
}
