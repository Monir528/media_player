 package app.testproject.mediaplayer
import java.util.concurrent.TimeUnit


data class Music(val title: String, val url: String, val img: Int)

//class Playlist{
//    lateinit var name: String
//    lateinit var playlist: ArrayList<Music>
//    lateinit var createdBy: String
//    lateinit var createdOn: String
//}
//class MusicPlaylist{
//    var ref: ArrayList<Playlist> = ArrayList()
//}

fun formatDuration(duration: Long):String{
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
            minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}
//fun getImgArt(path: String): ByteArray? {
//    val retriever = MediaMetadataRetriever()
//    retriever.setDataSource(path)
//    return retriever.embeddedPicture
//}
