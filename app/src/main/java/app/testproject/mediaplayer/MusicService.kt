package app.testproject.mediaplayer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import java.io.IOException

class MusicService: Service() {

    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var mediaSession : MediaSessionCompat
    private lateinit var runnable: Runnable

    override fun onBind(intent: Intent?): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "my music")
        return  myBinder
    }

    inner class MyBinder: Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }


    fun showNotification(playPauseBtn: Int) {

        val intent = Intent(baseContext, MainActivity::class.java)

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent, flag)

        val playIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent, flag)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, flag)

        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, flag)

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayList.musicList[Mp3Player.musicPosition].title)
            .setContentText("Unknown")
            .setSmallIcon(R.drawable.ic_baseline_audiotrack_24)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.vinyl))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_baseline_skip_previous_24, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.ic_baseline_skip_next_24, "Next", nextPendingIntent)
            .addAction(R.drawable.ic_baseline_exit_to_app_24, "Exit", exitPendingIntent)
            .build()

        startForeground(13, notification)
    }

    fun playNextSong() {
        if (Mp3Player.musicPosition<(PlayList.musicList.size-1)) {
            Mp3Player.musicPosition++
        }else{
            Mp3Player.musicPosition = 0
        }
        createMedia()
        showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
    }

    fun playPreviousSong() {
        if (Mp3Player.musicPosition == 0) {
            Mp3Player.musicPosition = (PlayList.musicList.size - 1)
        }else{
            Mp3Player.musicPosition--

        }

        Log.d("musicPosition:"," ${Mp3Player.musicPosition}")
        createMedia()
        showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
    }

    fun createMedia() {
        try {
            if(mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(PlayList.musicList[Mp3Player.musicPosition].url)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            Mp3Player.isPlaying = true
            Mp3Player.musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
            Mp3Player.binding.playPause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
            Mp3Player.binding.progress.text = formatDuration(Mp3Player.musicService!!.mediaPlayer!!.currentPosition.toLong())
            Mp3Player.binding.duration.text = formatDuration(Mp3Player.musicService!!.mediaPlayer!!.duration.toLong())
            Mp3Player.binding.seekBar.progress = 0
            Mp3Player.binding.seekBar.max = Mp3Player.musicService!!.mediaPlayer!!.duration
            Mp3Player.binding.textView.text = PlayList.musicList[Mp3Player.musicPosition].title
        }catch (e: IOException){
            e.printStackTrace()
        }

    }

    fun seekBarSetup(){
        runnable = Runnable {
            Mp3Player.binding.progress.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            Mp3Player.binding.seekBar.progress = mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

}