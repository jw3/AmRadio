package com.github.jw3.amradio

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.burgstaller.okhttp.digest.Credentials
import com.burgstaller.okhttp.digest.DigestAuthenticator
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import kotlinx.android.synthetic.main.activity_feed_view.*
import okhttp3.OkHttpClient

class FeedViewActivity : Activity() {
    private var player: SimpleExoPlayer? = null
    private val defaultExtractorsFactory = DefaultExtractorsFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_view)


        val credentials = Credentials("xxx", "xxx")
        val digestAuthenticator = DigestAuthenticator(credentials)

        val builder = OkHttpClient.Builder()
        builder.authenticator(digestAuthenticator)

        val f = OkHttpDataSourceFactory(builder.build(), null)
        val s = ExtractorMediaSource.Factory(f).createMediaSource(
                Uri.parse("http://192.168.1.123/cgi-bin/audio.cgi?action=getAudio&httptype=singlepart&channel=1")
        )

        val p = ExoPlayerFactory.newSimpleInstance(this)
        p.prepare(s)

        switch1.setOnCheckedChangeListener { _, play ->
            p.playWhenReady = play
            if (play) p.retry()
            else p.stop()

        }
    }
}
