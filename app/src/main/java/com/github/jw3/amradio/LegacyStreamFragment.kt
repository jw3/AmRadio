package com.github.jw3.amradio

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.burgstaller.okhttp.digest.Credentials
import com.burgstaller.okhttp.digest.DigestAuthenticator
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import kotlinx.android.synthetic.main.fragment_legacy_stream.*
import okhttp3.OkHttpClient
import java.util.logging.Logger


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LegacyStreamFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LegacyStreamFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LegacyStreamFragment : Fragment() {
    var p1: SimpleExoPlayer? = null
    var p2: SimpleExoPlayer? = null
    var p3: SimpleExoPlayer? = null

    private var listener: OnFragmentInteractionListener? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_legacy_stream, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LegacyStreamFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LegacyStreamFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    fun playerFor(u: String, p: String, host: String): SimpleExoPlayer {
        println("creating player ==== $u $p $host")

        val credentials = Credentials(u, p)
        val digestAuthenticator = DigestAuthenticator(credentials)

        val builder = OkHttpClient.Builder()
        builder.authenticator(digestAuthenticator)
        val f = OkHttpDataSourceFactory(builder.build(), null)
        val uri = "http://$host/cgi-bin/audio.cgi?action=getAudio&httptype=singlepart&channel=1"
        val s = ExtractorMediaSource.Factory(f).createMediaSource(Uri.parse(uri))
        println("connecting to === $uri")

        val ep = ExoPlayerFactory.newSimpleInstance(activity)
        ep.prepare(s)

        return ep
    }

    fun P1(): SimpleExoPlayer {
        if (p1 == null) {
            p1 = playerFor(
                    prefOrUnknown("username_preference"),
                    prefOrUnknown("password_preference"),
                    prefOrUnknown("hostname_preference_1")
            )
        }
        return p1!!
    }

    fun P2(): SimpleExoPlayer {
        if (p2 == null) {
            p2 = playerFor(
                    prefOrUnknown("username_preference"),
                    prefOrUnknown("password_preference"),
                    prefOrUnknown("hostname_preference_2")
            )
        }
        return p2!!
    }

    fun P3(): SimpleExoPlayer {
        if (p3 == null) {
            p3 = playerFor(
                    prefOrUnknown("username_preference"),
                    prefOrUnknown("password_preference"),
                    prefOrUnknown("hostname_preference_3")
            )
        }
        return p3!!
    }

    private fun prefs(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private fun prefOrUnknown(k: String): String {
        return prefs().getString(k, "unknown")
    }
}
