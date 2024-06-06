package com.Grader.grader_s

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grader_s.R
import coil.load


class adapter_gallery(private val items: List<dataclass_gallery>) :
    RecyclerView.Adapter<adapter_gallery.ViewHolder>() {

    private var onItemClick: ((position: Int) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.load(item.image) {
            placeholder(R.drawable.transparent)
            error(R.drawable.transparent)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        onItemClick = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.gallery_image)
    }
}