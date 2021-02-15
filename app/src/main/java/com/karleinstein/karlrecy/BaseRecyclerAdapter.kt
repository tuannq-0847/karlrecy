package com.karleinstein.karlrecy

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karleinstein.karlrecy.listener.RecyclerAdapterListener
import java.util.concurrent.Executors

abstract class BaseRecyclerAdapter<Item : Any>(
    callBack: DiffUtil.ItemCallback<Item> = BaseDiffUtil(),
    @LayoutRes var swipeForOptionsLayout: Int? = null,
    val onClickItem: (item: Item) -> Unit = {}
) : ListAdapter<Item, BaseViewHolder>(
    AsyncDifferConfig.Builder<Item>(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
), RecyclerAdapterListener<Item> {

    protected var dataRaw: List<Item> = listOf()
    private val states = mutableMapOf<Item, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = swipeForOptionsLayout?.let {
            LayoutInflater.from(parent.context).inflate(it, parent, false)
        }
        val frameLayout = FrameLayout(parent.context)
        frameLayout.layoutParams =
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        frameLayout.addView(view)
        frameLayout.addView(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        )
        return BaseViewHolder(
            itemView = frameLayout,
            parent = parent,
            swipeForOptionsLayout = swipeForOptionsLayout
        ).apply {
            bindFirstTime(this)
            dataRaw.forEach {
                states[it] = false
            }
        }
    }

    override fun bindFirstTime(baseViewHolder: BaseViewHolder) {}

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        onBind(holder, item, position)
    }

    override fun getItemViewType(position: Int): Int {
        return buildLayoutRes(position)
    }

    protected fun stateClickedHandler(isStateChanged: Boolean, position: Int): Boolean {
        if (position < states.keys.size) {
            val key = states.keys.toList()[position]
            states[key] = isStateChanged
        }
        return isStateChanged
    }
}

class BaseDiffUtil<Item : Any> : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}

@SuppressLint("ClickableViewAccessibility")
open class BaseViewHolder(
    itemView: View,
    val parent: ViewGroup,
    swipeForOptionsLayout: Int? = null
) : RecyclerView.ViewHolder(itemView) {

    private var touchSwipeList = mutableListOf<Float>()

    private fun setLayoutSwipe(@LayoutRes swipeLayout: Int?) {

    }

    init {
//        setLayoutSwipe(swipeForOptionsLayout)
        itemView.setOnTouchListener { v, event ->
            handleTouchSwipeToOptions(event.rawX)
            if (touchSwipeList.size == 2) {
                if (touchSwipeList[1] - touchSwipeList[0] < 0) {
                    handleAnimateSwipe(touchSwipeList[1] - touchSwipeList[0])
                } else handleAnimateSwipe(touchSwipeList[0] - touchSwipeList[1])
            }
            return@setOnTouchListener false
        }
    }


    private fun handleAnimateSwipe(rawX: Float) {
        ObjectAnimator.ofFloat(rawX, 0F).apply {
            addUpdateListener {
                val viewGroup = itemView as? ViewGroup
                viewGroup?.forEachIndexed { index, view ->
                    Log.d("handleAnimateSwipe", "view: $view")
                    if (index > 0) {
                        view.translationX = it.animatedValue as Float
                    }
                }
//                viewGroup?.translationX = it.animatedValue as Float
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    touchSwipeList.clear()
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

    private fun handleTouchSwipeToOptions(rawX: Float) {
        if (touchSwipeList.firstOrNull() == null) {
            touchSwipeList.add(0, rawX)
            return
        }
        if (touchSwipeList.size < 2)
            touchSwipeList.add(1, rawX)
        else {
            touchSwipeList.removeAt(1)
            touchSwipeList.add(rawX)
        }
    }
}
