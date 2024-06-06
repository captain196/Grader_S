package com.example.grader_s

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.Grader.grader_s.dataclass_syllabus

class adapter_syllabus(
    private val subjectList: List<dataclass_syllabus>,
    private val activity: Activity,
    private val downloadClickListener: OnDownloadClickListener
) : RecyclerView.Adapter<adapter_syllabus.SubjectViewHolder>() {

    interface OnDownloadClickListener {
        fun onDownloadClick(url: String, subject: String)
    }

    class SubjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.card_assign_image12)
        val titleView: TextView = view.findViewById(R.id.card_assign_header_title12)
        val contentView: TextView = view.findViewById(R.id.card_assign_content12)
        val downloadButton: ImageButton = view.findViewById(R.id.download_button12)

        fun bind(item: dataclass_syllabus) {
            imageView.setImageResource(item.imageResId)
            titleView.text = item.title
            contentView.text = item.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_syllabus, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val item = subjectList[position]
        holder.bind(item)

        holder.downloadButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO),
                    REQUEST_WRITE_STORAGE
                )
            } else {
                downloadClickListener.onDownloadClick(item.url, item.title)
            }
        }
    }

    override fun getItemCount(): Int {
        return subjectList.size
    }

    companion object {
        const val REQUEST_WRITE_STORAGE = 112
    }
}
