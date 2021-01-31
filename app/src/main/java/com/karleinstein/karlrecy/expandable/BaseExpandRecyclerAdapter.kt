package com.karleinstein.karlrecy.expandable

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.karleinstein.karlrecy.BaseDiffUtil
import com.karleinstein.karlrecy.BaseRecyclerAdapter
import com.karleinstein.karlrecy.BaseViewHolder
import com.karleinstein.karlrecy.listener.RecyclerAdapterListener

abstract class BaseExpandRecyclerAdapter<G : Any, C : Any>(
    callback: DiffUtil.ItemCallback<ExpandableItem> = BaseDiffUtil(),
    @LayoutRes swipeForOptionsLayout: Int? = null
) : BaseRecyclerAdapter<ExpandableItem>(callback, swipeForOptionsLayout = swipeForOptionsLayout),
    RecyclerAdapterListener<ExpandableItem> {

    override fun bindFirstTime(baseViewHolder: BaseViewHolder) {
        baseViewHolder.itemView.setOnClickListener {
            val item = getItem(baseViewHolder.absoluteAdapterPosition)
            if (item is GroupItem<*>) {
                item.isExpand = !item.isExpand
                isExpandedListener(baseViewHolder, item.isExpand)
                submitList(
                    dataRaw.setStateChildView(
                        stateClickedHandler(
                            item.isExpand,
                            baseViewHolder.absoluteAdapterPosition
                        ),
                        baseViewHolder.absoluteAdapterPosition
                    )
                )
            }
        }
    }

    open fun isExpandedListener(baseViewHolder: BaseViewHolder, isExpanded: Boolean) {

    }

    fun register(data: List<ExpandableData<out G, out C>>) {
        dataRaw = data.initList()
        submitList(getData(dataRaw))
    }

    override fun onBind(holder: BaseViewHolder, item: ExpandableItem, position: Int) {
        if (item is GroupItem<*>) {
            onBindGroup(holder.itemView, item.toGroupData<G>())
        } else onBindChild(holder.itemView, (item as? ChildItem<*>)?.toChildData<C>())
    }

    abstract fun onBindGroup(itemView: View, item: G?)

    abstract fun onBindChild(itemView: View, item: C?)

    private fun getPosInCurrentList(expandableItem: ExpandableItem): Int {
        return dataRaw.indexOf(expandableItem)
    }

    inline fun <reified T> checkType(obj: Any) = obj is T
}
