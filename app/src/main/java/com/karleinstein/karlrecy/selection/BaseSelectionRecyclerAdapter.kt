package com.karleinstein.karlrecy.selection

import androidx.recyclerview.widget.DiffUtil
import com.karleinstein.karlrecy.BaseDiffUtil
import com.karleinstein.karlrecy.BaseRecyclerAdapter
import com.karleinstein.karlrecy.BaseViewHolder
import com.karleinstein.karlrecy.listener.SelectionRecyclerListener

abstract class BaseSelectionRecyclerAdapter<Item : Any>(
    callBack: DiffUtil.ItemCallback<Item> = BaseDiffUtil(),
    onClickItem: (item: Item) -> Unit = {}
) : BaseRecyclerAdapter<Item>(callBack, onClickItem), SelectionRecyclerListener<Item> {

    var isEnableMultiSelect = false
    override fun onBind(holder: BaseViewHolder, item: Item, position: Int) {
        //Do nothing
    }

    override fun bindFirstTime(baseViewHolder: BaseViewHolder) {
        super.bindFirstTime(baseViewHolder)
        setOnSelectionListener(this)
    }

    override fun onStateChanged(baseViewHolder: BaseViewHolder, isStateChanged: Boolean) {
        super.onStateChanged(baseViewHolder, isStateChanged)
        handleSelection(isEnableMultiSelect, baseViewHolder.absoluteAdapterPosition)
    }
}
