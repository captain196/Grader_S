package com.Grader.grader_s

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.grader_s.R
import coil.load
import com.ortiz.touchview.TouchImageView


class adapter_gallery_2(private val items: List<dataclass_gallery>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<adapter_gallery_2.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_gallery_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.load(item.image) {
            //placeholder(R.drawable.transparent)
            error(R.drawable.geography_1)
            //crossfade(true)
        }

        holder.imageView.viewPager2 = viewPager2
    }

    override fun getItemCount(): Int {
        return items.size
    }


     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: CustomTouchImageView = itemView.findViewById(R.id.image_gallery_2)
    }
}