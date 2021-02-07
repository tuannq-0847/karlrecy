package com.karleinstein.karlrecy.listener

import com.karleinstein.karlrecy.BaseViewHolder

interface SelectionRecyclerListener<Item> {

    fun onBindSelection(
        baseViewHolder: BaseViewHolder,
        item: Item,
        isSelected: Boolean?,
        position: Int
    )
}
