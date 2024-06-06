package com.Grader.grader_s

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.grader_s.R

class adapter_assignment(private val items: List<data_class_assignments>) :
    RecyclerView.Adapter<adapter_assignment.ViewHolder>() {

    private var selectedItemIndex: Int = RecyclerView.NO_POSITION
    private var onItemClick: ((position: Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_assignment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (position == selectedItemIndex) {

            val layoutParams = holder.card.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            holder.card.layoutParams = layoutParams
//            val sdpHeight = holder.itemView.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._120sdp) // Replace _200sdp with your desired sdp value
//            val layoutParams = holder.card.layoutParams
//            layoutParams.height = sdpHeight
//            holder.card.layoutParams = layoutParams
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val image: ImageView = itemView.findViewById(R.id.card_assign_image1)
        private val textview3: TextView = itemView.findViewById(R.id.date)
        private val textView1: TextView = itemView.findViewById(R.id.card_assign_header_title1)
        private val textView2: TextView = itemView.findViewById(R.id.card_assign_content1)
        val card: CardView = itemView.findViewById(R.id.assignment_cardview)


        init {
            card.setOnClickListener {

                val layoutParams = card.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                card.layoutParams = layoutParams

            }
        }
        fun bind(item: data_class_assignments) {
            image.setImageResource(item.imageResId)
            textview3.text = item.data
            textView1.text = item.header
            textView2.text = item.content
        }
    }
}
