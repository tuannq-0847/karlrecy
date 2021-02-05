package com.karleinstein.karlrecy

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karleinstein.karlrecy.expandable.ExpandableData
import com.karleinstein.karlrecy.expandable.getData
import com.karleinstein.karlrecy.expandable.initList
import com.karleinstein.karlrecy.listener.RecyclerAdapterListener
import com.karleinstein.karlrecy.listener.SelectionRecyclerListener
import java.util.concurrent.Executors

abstract class BaseRecyclerAdapter<Item : Any>(
    callBack: DiffUtil.ItemCallback<Item> = BaseDiffUtil(),
    val onClickItem: (item: Item) -> Unit = {}
) : ListAdapter<Item, BaseViewHolder>(
    AsyncDifferConfig.Builder<Item>(callBack)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
), RecyclerAdapterListener<Item> {

    protected var dataRaw = mutableListOf<Item>()
    private val states = mutableMapOf<Item, Boolean>()

    private var selectionRecyclerListener: SelectionRecyclerListener<Item>? = null

    protected fun setOnSelectionListener(selectionRecyclerListener: SelectionRecyclerListener<Item>?) {
        this.selectionRecyclerListener = selectionRecyclerListener
    }


    fun register(list: MutableList<Item>?) {
        dataRaw.addAll(list ?: listOf())
        dataRaw.forEach {
            Log.d("DataRaw", "dataRaw: $it")
            states[it] = false
        }
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        )
            .apply {
                bindFirstTime(this)
            }
    }

    fun setSwipeForOptionsLayout(@LayoutRes swipeForOptionsLayout: Int) {

    }

    protected fun handleSelection(isMultiSelection: Boolean, position: Int) {
        if (isMultiSelection) return
        val currentItem = getItem(position)
        states.keys.forEachIndexed { index, item ->
            if (states[item] == true) {
                notifyItemChanged(index)
            }
            if (item != currentItem) {
                states[item] = false
            }
        }
    }

    override fun bindFirstTime(baseViewHolder: BaseViewHolder) {
        baseViewHolder.itemView.setOnClickListener {
            val currentKey = getItem(baseViewHolder.absoluteAdapterPosition)
            states[currentKey]?.let {
                val isStateChanged = stateClickedHandler(
                    !it,
                    baseViewHolder.absoluteAdapterPosition
                )
                onStateChanged(baseViewHolder, isStateChanged)
            }
        }
    }

    open fun onStateChanged(baseViewHolder: BaseViewHolder, isStateChanged: Boolean) {

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        selectionRecyclerListener?.onBindSelection(
            baseViewHolder = holder,
            item = item,
            isSelected = states[item],
            position = position
        ) ?: onBind(
            holder,
            item,
            position
        )
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
