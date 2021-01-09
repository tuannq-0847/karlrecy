package com.karleinstein.karlrecy

import androidx.annotation.LayoutRes

internal interface RecyclerAdapterListener<Item> {

    fun onBind(holder: BaseViewHolder, item: Item, position: Int)

    @LayoutRes
    fun buildLayoutRes(position: Int): Int

    fun bindFirstTime(baseViewHolder: BaseViewHolder)
}
