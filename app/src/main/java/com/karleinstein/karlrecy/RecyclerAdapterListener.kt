package com.karleinstein.karlrecy

import androidx.annotation.LayoutRes

interface RecyclerAdapterListener<Item> {

    fun onBind(holder: BaseViewHolder, item: Item, position: Int)

    @LayoutRes
    fun buildLayoutRes(position: Int): Int

}
