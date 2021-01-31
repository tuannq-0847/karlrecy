package com.karleinstein.karlrecy.listener

import androidx.annotation.LayoutRes
import com.karleinstein.karlrecy.BaseViewHolder

internal interface RecyclerAdapterListener<Item> {

    fun onBind(holder: BaseViewHolder, item: Item, position: Int)

    @LayoutRes
    fun buildLayoutRes(position: Int): Int

    fun bindFirstTime(baseViewHolder: BaseViewHolder)
}

