package com.karleinstein.karlrecy

import android.view.View
import androidx.recyclerview.widget.DiffUtil

abstract class BaseExpandRecyclerAdapter<G : Any, C : Any>(
    callback: DiffUtil.ItemCallback<ExpandableItem> = BaseDiffUtil()
) : BaseRecyclerAdapter<ExpandableItem>(callback), RecyclerAdapterListener<ExpandableItem> {

    override fun bindFirstTime(baseViewHolder: BaseViewHolder) {
        baseViewHolder.itemView.setOnClickListener {
            val a = getItem(baseViewHolder.absoluteAdapterPosition)
            if (a is GroupItem<*>){
                a.isExpand = !a.isExpand
                isExpandedListener(baseViewHolder, a.isExpand)
                submitList(
                    dataRaw.setStateChildView(
                        stateClickedHandler(
                            a.isExpand,
                            baseViewHolder.absoluteAdapterPosition
                        ),
                        baseViewHolder.absoluteAdapterPosition
                    )
                )
            }
        }
    }

    open fun isExpandedListener(baseViewHolder: BaseViewHolder, isExpanded: Boolean){

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
