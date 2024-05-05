import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.grader_s.Attendance
import com.example.grader_s.R

class CalendarAdapter(private val dates: List<DateModel>, private val listener: Attendance) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    interface OnDateClickListener {
        fun onDateClick(date: DateModel)
    }

    data class DateModel(val date: String, val status: DateStatus)

    enum class DateStatus { ABSENT, PRESENT, LEAVE, TODAY, FUTURE}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = dates[position]
        holder.bind(date)
    }

    override fun getItemCount(): Int = dates.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)

        fun bind(date: DateModel) {
            textViewDate.text = date.date
            val context = textViewDate.context
            val backgroundDrawable = getBackgroundDrawable(context, date.status)
            textViewDate.background = backgroundDrawable

            itemView.setOnClickListener {
                listener.onDateClick(date)
            }
        }
    }

    private fun getBackgroundDrawable(context: Context, status: DateStatus): Drawable {
        val circleBackground =
            ContextCompat.getDrawable(context, R.drawable.circle_background)?.mutate()
        return when (status) {
            DateStatus.PRESENT -> {
                circleBackground?.setTint(ContextCompat.getColor(context, R.color.colorPresent))
                circleBackground ?: ColorDrawable(Color.TRANSPARENT)
            }
            DateStatus.ABSENT -> {
                circleBackground?.setTint(ContextCompat.getColor(context, R.color.colorAbsent))
                circleBackground ?: ColorDrawable(Color.TRANSPARENT)
            }
            DateStatus.LEAVE -> {
                circleBackground?.setTint(ContextCompat.getColor(context, R.color.colorHoliday))
                circleBackground ?: ColorDrawable(Color.TRANSPARENT)
            }
            DateStatus.TODAY -> {
                circleBackground?.setTint(ContextCompat.getColor(context, R.color.colorYellow))
                circleBackground ?: ColorDrawable(Color.TRANSPARENT)
            }
            DateStatus.FUTURE -> {
                circleBackground?.setTint(ContextCompat.getColor(context, R.color.colorFuture))
                circleBackground ?: ColorDrawable(Color.TRANSPARENT)
            }

        }
    }
}
