package app.testproject.mediaplayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.google.android.material.imageview.ShapeableImageView

class ListAdapter(private val list: Array<Music>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private lateinit var listListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        listListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.play_list_item, parent,false)
        return  ListViewHolder(itemView, listListener)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = list[position]
        holder.image.setImageResource(currentItem.img)
        holder.title.text = currentItem.title
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ListViewHolder(itemView: View, listener: onItemClickListener) : ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.playlist_item_title)
        val image: ShapeableImageView = itemView.findViewById(R.id.playlist_item_img)
        init {
             itemView.setOnClickListener {
                 listener.onItemClick(adapterPosition)
             }
        }
    }

}