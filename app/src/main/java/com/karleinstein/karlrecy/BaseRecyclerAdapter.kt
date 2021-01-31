package com.karleinstein.karlrecy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karleinstein.karlrecy.listener.RecyclerAdapterListener
import java.util.concurrent.Executors

abstract class BaseRecyclerAdapter<Item : Any>(
    callBack: DiffUtil.ItemCallback<Item> = BaseDiffUtil(),
    val onClickItem: (item: Item) -> Unit = {}
) : ListAdapter<Item, BaseViewHolder>(
    AsyncDifferConfig.Builder<Item>(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
), RecyclerAdapterListener<Item> {

    protected var dataRaw: List<Item> = listOf()
    private val states = mutableMapOf<Item, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        )
            .apply {
                bindFirstTime(this)
                dataRaw.forEach {
                    states[it] = false
                }
            }
    }

    fun setSwipeForOptionsLayout(@LayoutRes swipeForOptionsLayout: Int) {

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

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
