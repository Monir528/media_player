package app.testproject.mediaplayer
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import app.testproject.mediaplayer.databinding.ActivityMp3PlayerBinding
import java.io.IOException
import kotlin.time.Duration.Companion.minutes

class Mp3Player : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {


    companion object {
        var musicService: MusicService? = null
        var musicPosition: Int = 0
        var isPlaying:Boolean = false
        @SuppressLint("StaticFieldLeak")
        var _binding: ActivityMp3PlayerBinding? = null
        val binding get() = _binding!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMp3PlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val serviceIntent = Intent(this, MusicService::class.java)
        bindService(serviceIntent, this, BIND_AUTO_CREATE)
        startService(serviceIntent)

        musicPosition = intent.getIntExtra("index", 0)

        binding.playPause.setOnClickListener {
                playPauseAudio()
        }

        binding.playNext.setOnClickListener{
            musicService!!.playNextSong()
        }

        binding.playPrevious.setOnClickListener{
            musicService!!.playPreviousSong()

        }

        binding.seekBar.setOnSeekBarChangeListener(object :OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) musicService!!.mediaPlayer!!.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })
    }

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null

    }

    private fun playPauseAudio() {
        if(musicService!!.mediaPlayer!!.isPlaying){
            musicService!!.mediaPlayer!!.pause()
            binding.playPause.setImageResource(R.drawable.ic_baseline_play_circle_filled_24)
            isPlaying = false
            musicService!!.showNotification(R.drawable.ic_baseline_play_circle_filled_24)
        }else{
            isPlaying = true
            musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
            musicService!!.mediaPlayer!!.start()
            binding.playPause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
        }
    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        if(musicService == null){
            var binding = service as MusicService.MyBinder
            musicService = binding.currentService()
        }
        musicService!!.createMedia()
        musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
        musicService!!.seekBarSetup()
        musicService!!.mediaPlayer!!.setOnCompletionListener(this)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    override fun onCompletion(p0: MediaPlayer?) {
        musicService!!.playNextSong()
    }
}