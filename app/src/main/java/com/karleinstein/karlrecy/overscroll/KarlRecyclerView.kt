package com.karleinstein.karlrecy.overscroll

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karleinstein.karlrecy.listener.KarlRecycleListener

@SuppressLint("ClickableViewAccessibility")
class KarlRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs),
    KarlRecycleListener {

    private var isEnableScroll = true
    var isEnableOverScroll: Boolean = true
    var isEnableSwipeForOptions = true
    private val overScrollOnTouchHandler =
        OverScrollOnTouchHandler(isEnableOverScroll, isEnableSwipeForOptions)

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        this.setOnTouchListener(overScrollOnTouchHandler)
        overScrollOnTouchHandler.isVisibleFirstPosition = isVisibleFirstPosition()
        overScrollOnTouchHandler.isVisibleLastPosition = isVisibleLastPosition()
    }

    init {
        overScrollOnTouchHandler.setOnKarlRecycleViewListener(this)
        val linearLayoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                Log.d("linearLayoutManager", "canScrollVertically: $isEnableScroll")
                return isEnableScroll
            }
        }
        layoutManager = linearLayoutManager
        val itemTouchHelper = ItemTouchHelper(overScrollOnTouchHandler)
        if (isEnableSwipeForOptions) itemTouchHelper.attachToRecyclerView(this)
    }

    private fun isVisibleFirstPosition(): Boolean {
        if (layoutManager is LinearLayoutManager) {
            val firstVisiblePosition =
                (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
            isEnableScroll = true
            return firstVisiblePosition == 0
        } else throw Exception("Only support Linear Layout Manager....")
    }

    private fun isVisibleLastPosition(): Boolean {
        if (layoutManager is LinearLayoutManager) {
            val lastCompletelyVisibleItemPosition =
                (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            Log.d("lastCompletely", "isVisibleLastPosition: $lastCompletelyVisibleItemPosition")
            isEnableScroll = true
            return lastCompletelyVisibleItemPosition == adapter?.itemCount?.minus(1)
        } else throw Exception("Only support Linear Layout Manager....")
    }

    private fun isInside(e: MotionEvent): Boolean {
        return !(e.x < 0 || e.y < 0
                || e.x > this.measuredWidth
                || e.y > this.measuredHeight)
    }

    override fun onAnimateTranslationY(translationY: Float) {
        Log.d("Karl", "onAnimateTranslationY: $translationY")
        this@KarlRecyclerView.translationY = translationY
    }

    override fun onMotionEventListener(event: MotionEvent) {
        overScrollOnTouchHandler.isInside = isInside(event)
    }
}
