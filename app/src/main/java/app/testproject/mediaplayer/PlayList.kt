package app.testproject.mediaplayer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.testproject.mediaplayer.databinding.ActivityPlayListBinding
import com.google.android.material.textfield.TextInputEditText

class PlayList : AppCompatActivity() {

    companion object {
        var musicList: Array<Music> = arrayOf(
            Music(
                title = "Audio 1",
                url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                img = R.drawable.vinyl),
            Music(
                title = "Audio 2",
                url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                img = R.drawable.vinyl),
            Music(
                title = "Audio 3",
                url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
                img = R.drawable.vinyl),
        )
    }

    private var _binding: ActivityPlayListBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appbar)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.setHasFixedSize(true)


        var adapter = ListAdapter(musicList)
        binding.recyclerview.adapter = adapter
        adapter.setOnItemClickListener(object : ListAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
//                startActivity(Intent(this@PlayList, Mp3Player::class.java))

                val intent = Intent(baseContext, Mp3Player::class.java)
                intent.putExtra("index", position)
                startActivity(intent, null)
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.playlist_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_play_list_id) {
            
        }
        return super.onOptionsItemSelected(item)
    }


}