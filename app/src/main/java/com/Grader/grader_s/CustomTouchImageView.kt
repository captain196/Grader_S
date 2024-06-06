package com.Grader.grader_s

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager2.widget.ViewPager2
import com.github.chrisbanes.photoview.PhotoView

class CustomTouchImageView(context: Context, attrs: AttributeSet?) : PhotoView(context, attrs) {

    var viewPager2: ViewPager2? = null

    init {
        setOnMatrixChangeListener {
            updateViewPagerSwipe()
        }
    }

    private fun updateViewPagerSwipe() {
        viewPager2?.isUserInputEnabled = scale == minimumScale
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                viewPager2?.isUserInputEnabled = scale == minimumScale
            }
        }
        return super.onTouchEvent(event)
    }
}
