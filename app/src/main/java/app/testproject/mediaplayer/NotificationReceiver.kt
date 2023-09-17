package app.testproject.mediaplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.TelephonyManager
import kotlin.system.exitProcess

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            when (intent.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    if(Mp3Player.isPlaying) pauseMusic()
                    print("incoming call ----------------")
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    print("end call ----------------")

                    playMusic()
                }
            }
        }

        when(intent?.action){

            ApplicationClass.PREVIOUS -> Mp3Player.musicService!!.playPreviousSong()
            ApplicationClass.PLAY -> if(Mp3Player.isPlaying) pauseMusic() else playMusic()
            ApplicationClass.NEXT -> Mp3Player.musicService!!.playNextSong()
            ApplicationClass.EXIT ->{
                exitApplication()
            }
        }
    }

    fun exitApplication(){
        if(Mp3Player.musicService != null){
//            Mp3Player.musicService!!.audioManager.abandonAudioFocus(PlayerActivity.musicService)
            Mp3Player.musicService!!.stopForeground(true)
            Mp3Player.musicService!!.mediaPlayer!!.release()
            Mp3Player.musicService = null}
        exitProcess(1)
    }

    private fun pauseMusic() {
        Mp3Player.isPlaying = false
        Mp3Player.musicService!!.mediaPlayer!!.pause()
        Mp3Player.musicService!!.showNotification(R.drawable.ic_baseline_play_arrow_24)
        Mp3Player.binding.playPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    private fun playMusic() {
        Mp3Player.isPlaying = true
        Mp3Player.musicService!!.mediaPlayer!!.start()
        Mp3Player.musicService!!.showNotification(R.drawable.ic_baseline_pause_circle_filled_24)
        Mp3Player.binding.playPause.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
    }

}