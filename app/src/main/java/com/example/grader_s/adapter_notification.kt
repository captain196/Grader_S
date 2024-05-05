package com.example.grader_s



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class adapter_notification(private val items: List<data_class_notification>) :
    RecyclerView.Adapter<adapter_notification.ViewHolder>() {

    private var onItemClick: ((position: Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_notification, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

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
        private val imageView: ImageView = itemView.findViewById(R.id.card_assign_image)
        private val textView1: TextView = itemView.findViewById(R.id.card_assign_header_title)
        private val textView2: TextView = itemView.findViewById(R.id.card_assign_content)
        private val date1: TextView = itemView.findViewById(R.id.date2)

        fun bind(item: data_class_notification) {
            imageView.setImageResource(item.imageResId)
            textView1.text = item.header
            textView2.text = item.content
            date1.text = item.datee
        }
    }
}
