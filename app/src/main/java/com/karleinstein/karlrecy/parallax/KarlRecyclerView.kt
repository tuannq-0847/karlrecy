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
    private var firstTouchY = 0F
    private var nextTouchY = 0F
    private var isEnableScroll = true
    var isEnableParallax: Boolean = true

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        Log.d("onScrolled", "dx: $dx dy: $dy")
        this.setOnTouchListener { v, event ->
            if (isEnableParallax && isEnableOverScrollTop(event.rawY) == true) {
                isEnableScroll = false
                animateParallax(v, event)
            }
            return@setOnTouchListener false
        }
    }

    init {
        val linearLayoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return isEnableScroll
            }
        }
        layoutManager = linearLayoutManager
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        Log.d("onScrollStateChanged", "onScrollStateChanged: state: $state")
    }

    private fun animateParallax(view: View, event: MotionEvent) {

        if (event.action == ACTION_MOVE) {
            if (isInside(event)) {
                scrollToPosition(0)
                translationY = event.rawY / 2
            } else {
                isEnableScroll = true
                handleAnimate(event.rawY / 2)
            }
        }
        if (event.action == ACTION_UP) {
            handleAnimate(event.rawY / 2)
            firstTouchY = 0F
            nextTouchY = 0F
            isEnableScroll = true
            this.translationY = 0F
        }
    }

    private fun isVisibleFirstPosition(): Boolean {
        if (layoutManager is LinearLayoutManager) {
            val firstVisiblePosition =
                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            return firstVisiblePosition == 0
        } else throw Exception("Only support Linear Layout Manager....")
    }

    private fun isInside(e: MotionEvent): Boolean {
        return !(e.x < 0 || e.y < 0
                || e.x > this.measuredWidth
                || e.y > this.measuredHeight)
    }

    private fun isEnableOverScrollTop(rawY: Float): Boolean? {
        if (firstTouchY == 0F) {
            firstTouchY = rawY
            isEnableScroll = true
            return null
        }
        if (firstTouchY != nextTouchY && nextTouchY == 0F) nextTouchY = rawY
        val isVisible = isVisibleFirstPosition()
        Log.d("isVisible", "isEnableOverScrollTop: $isVisible")
        Log.d("firstTouchY", "firstTouchY: $firstTouchY nextTouchY: $nextTouchY")
        return if ((firstTouchY < nextTouchY) && isVisible) {
            true
        } else {
            firstTouchY = 0F
            nextTouchY = 0F
            false
        }
    }

    private fun handleAnimate(height: Float) {
        ObjectAnimator.ofFloat(height, 0F).apply {
            addUpdateListener {
                Log.d("KarlRecyclerViewAz", "animatedValue: ${it.animatedValue as Float}")
                this@KarlRecyclerView.translationY = it.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    isEnableScroll = true
                    firstTouchY = 0F
                    nextTouchY = 0F
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }

            })
            duration = 1000
            start()
        }
    }
}
