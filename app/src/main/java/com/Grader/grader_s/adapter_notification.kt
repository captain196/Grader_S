package com.Grader.grader_s



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.grader_s.R


class adapter_notification(private val items: List<data_class_notification>) :
    RecyclerView.Adapter<adapter_notification.ViewHolder>() {

    private var selectedItemIndex: Int = RecyclerView.NO_POSITION
    private var onItemClick: ((position: Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_notification, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (position == selectedItemIndex) {
            val layoutParams = holder.card.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            holder.card.layoutParams = layoutParams
        } else {
            val sdpHeight = holder.itemView.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._70sdp) // Replace _200sdp with your desired sdp value
            val layoutParams = holder.card.layoutParams
            layoutParams.height = sdpHeight
            holder.card.layoutParams = layoutParams
        }

        holder.itemView.setOnClickListener {
            val previousSelectedItem: Int = selectedItemIndex // Store the previous selected item
            selectedItemIndex = holder.adapterPosition // Update the selected item

            if(previousSelectedItem ==selectedItemIndex){
                selectedItemIndex = -1
            }
            // Notify adapter about item changes
            if (previousSelectedItem != -1) {
                notifyItemChanged(previousSelectedItem) // Notify previous item
            }
            notifyItemChanged(selectedItemIndex) // Notify current item
        }
        holder.bind(item)


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
        var card: CardView = itemView.findViewById(R.id.notification_card)

        init {
            card.setOnClickListener {

                val layoutParams = card.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                card.layoutParams = layoutParams

            }
        }

        fun bind(item: data_class_notification) {
            imageView.setImageResource(item.imageResId)
            textView1.text = item.header
            textView2.text = item.content
            date1.text = item.datee
        }
    }
}
