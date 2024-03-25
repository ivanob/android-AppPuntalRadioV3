package com.example.android_apppuntalradiov3

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.android_apppuntalradiov3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isReset: Boolean = false

    private fun initializeMediaPlayer() {
        // URL of your stream
        val streamUrl = "https://a5.asurahosting.com/listen/puntalradio/radio.mp3"
        // val streamUrl = "https://stream.wqxr.org/wqxr"

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(streamUrl)
            setOnPreparedListener {
                it.start() // Start playback
            }
            setOnErrorListener { _, what, extra ->
                Log.e("MediaPlayerError", "What: $what, Extra: $extra")
                true
            }
            prepareAsync()
        }
    }

    private fun setupListenersButtons() {
        // Find the button by its ID
        val buttonPlay: ImageButton = findViewById(R.id.btn_play)
        val buttonPause: ImageButton = findViewById(R.id.btn_pause)
        val buttonStop: ImageButton = findViewById(R.id.btn_stop)
        val statusLabel: TextView = findViewById(R.id.statusLabel)

        // Set up a click listener
        buttonPlay.setOnClickListener {
            if(isReset){
                initializeMediaPlayer()
                isReset = false
            }else{
                mediaPlayer?.start()
            }
            statusLabel.text = "Reproduciendo emisión"
        }
        buttonPause.setOnClickListener {
            mediaPlayer?.pause()
            statusLabel.text = "Emisión pausada"
        }
        buttonStop.setOnClickListener {
            mediaPlayer?.reset()
            isReset = true
            statusLabel.text = "Emisión parada"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        initializeMediaPlayer()
        setupListenersButtons()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}