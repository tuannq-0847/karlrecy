package com.karleinstein.karlrecy.listener

import android.view.MotionEvent

interface KarlRecycleListener {

    fun onAnimateTranslationY(translationY: Float)

    fun onMotionEventListener(event: MotionEvent)
}
