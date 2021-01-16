package com.karleinstein.karlrecy.parallax

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("ClickableViewAccessibility")
class KarlRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {
    private var touchList = mutableListOf<Float>()
    private var isEnableScroll = true
    var isEnableOverScroll: Boolean = true

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        this.setOnTouchListener { _, event ->
            if (isEnableOverScroll) {
                val stateOverScroll = try {
                    handleOverScrollState(event.rawY)
                } catch (ex: IndexOutOfBoundsException) {
                    isEnableScroll = true
                    ex.printStackTrace()
                    StateOverScroll.NONE
                }
                if (stateOverScroll == StateOverScroll.OVER_SCROLL_TOP) {
                    isEnableScroll = false
                    animateOverScroll(event)
                }
                if (stateOverScroll == StateOverScroll.OVER_SCROLL_BOT) {
                    isEnableScroll = false
                    animateOverScroll(event)
                }
            }
            return@setOnTouchListener false
        }
    }

    init {
        val linearLayoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                Log.d("linearLayoutManager", "canScrollVertically: $isEnableScroll")
                return isEnableScroll
            }
        }
        layoutManager = linearLayoutManager
    }

    private fun animateOverScroll(event: MotionEvent) {

        if (event.action == ACTION_MOVE) {
            if (isInside(event)) {
                translationY = touchList[1] - touchList[0]
            } else {
                isEnableScroll = true
                handleAnimate(touchList[1] - touchList[0])
                touchList.clear()
            }
        }
        if (event.action == ACTION_UP) {
            handleAnimate(touchList[1] - touchList[0])
            touchList.clear()
            isEnableScroll = true
            this.translationY = 0F
        }
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

    private fun handleOverScrollState(rawY: Float): StateOverScroll? {
        handleTouchOverScroll(rawY)
        val isFirstPositionVisible = isVisibleFirstPosition()
        val isLastPositionVisible = isVisibleLastPosition()
        Log.d("KarlRecycler", "handleOverScrollState: $touchList")
        return if ((touchList[0] < touchList[1]) && isFirstPositionVisible) {

            StateOverScroll.OVER_SCROLL_TOP
        } else if ((touchList[0] > touchList[1]) && isLastPositionVisible) {
            StateOverScroll.OVER_SCROLL_BOT
        } else {
            StateOverScroll.NONE
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
                this@KarlRecyclerView.translationY = it.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    isEnableScroll = true
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
}

private enum class StateOverScroll {
    OVER_SCROLL_TOP,
    OVER_SCROLL_BOT,
    NONE
}

