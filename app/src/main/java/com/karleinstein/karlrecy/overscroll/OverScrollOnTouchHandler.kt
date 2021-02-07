package com.karleinstein.karlrecy.overscroll

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.karleinstein.karlrecy.listener.KarlRecycleListener

class OverScrollOnTouchHandler(
    private var isEnableOverScroll: Boolean,
    private val isEnableSwipeToOptions: Boolean,
) : View.OnTouchListener, ItemTouchHelper.Callback() {
    var isVisibleFirstPosition = false
    var isVisibleLastPosition = false
    var isInside = false

    private var touchList = mutableListOf<Float>()
    private var karlRecyclerView: KarlRecycleListener? = null
    private var isNeedSwipeBack = false

    fun setOnKarlRecycleViewListener(karlRecycleListener: KarlRecycleListener) {
        this.karlRecyclerView = karlRecycleListener
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        karlRecyclerView?.onMotionEventListener(event)
        Log.d(
            "OverHandler", "onTouch: " +
                    "isVisibleFirstPosition: $isVisibleFirstPosition isVisibleLastPosition: $isVisibleLastPosition" +
                    "isInside: $isInside" +
                    "isEnableOverScroll: $isEnableOverScroll"
        )
        isEnableOverScroll = !isEnableSwipeToOptions
        if (event.action in arrayOf(MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL)) {
            isNeedSwipeBack = true
        }
        if (isEnableOverScroll) {
            val stateOverScroll = try {
                handleOverScrollState(event.rawY)
            } catch (ex: IndexOutOfBoundsException) {
                ex.printStackTrace()
                StateOverScroll.NONE
            }
            Log.d("OverHandler", "onTouch: $stateOverScroll")
            if (stateOverScroll == StateOverScroll.OVER_SCROLL_TOP) {
                animateOverScroll(event)
            }
            if (stateOverScroll == StateOverScroll.OVER_SCROLL_BOT) {
                animateOverScroll(event)
            }
        }
        return false
    }

    private fun animateOverScroll(event: MotionEvent) {
        Log.d("TAG", "animateOverScroll: event: ${event.action}")
        if (event.action == MotionEvent.ACTION_MOVE) {
            if (isInside) {
                karlRecyclerView?.onAnimateTranslationY(touchList[1] - touchList[0])
            } else {
                handleAnimate(touchList[1] - touchList[0])
                touchList.clear()
            }
        }
        if (event.action == MotionEvent.ACTION_UP) {
            handleAnimate(touchList[1] - touchList[0])
            touchList.clear()
            karlRecyclerView?.onAnimateTranslationY(0F)
        }
    }

    private fun handleOverScrollState(rawY: Float): StateOverScroll? {
        handleTouchOverScroll(rawY)
        Log.d("KarlRecycler", "handleOverScrollState: $touchList")
        return when {
            (touchList[0] < touchList[1]) && isVisibleFirstPosition -> {
                StateOverScroll.OVER_SCROLL_TOP
            }
            (touchList[0] > touchList[1]) && isVisibleLastPosition -> {
                StateOverScroll.OVER_SCROLL_BOT
            }
            else -> {
                StateOverScroll.NONE
            }
        }
    }

    private fun handleTouchOverScroll(rawY: Float) {
        if (touchList.firstOrNull() == null) {
            touchList.add(0, rawY)
            return
        }
        if (touchList.size < 2)
            touchList.add(1, rawY)
        else {
            touchList.removeAt(1)
            touchList.add(rawY)
        }
    }

    private fun handleAnimate(height: Float) {
        ObjectAnimator.ofFloat(height, 0F).apply {
            addUpdateListener {
                karlRecyclerView?.onAnimateTranslationY(it.animatedValue as Float)
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    touchList.clear()
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {}
            })
            duration = 1000
            start()
        }
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        Log.d("A", "onMove: ")
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Log.d("A", "onSwiped: ")
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (isNeedSwipeBack) {
            isNeedSwipeBack = false
            Log.d("A", "convertToAbsoluteDirection: ")
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener(this)
        Log.d("onChildDraw", "onChildDraw: actionState: $actionState")
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
