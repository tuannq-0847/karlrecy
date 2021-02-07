package com.karleinstein.karlrecy.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.karleinstein.karlrecy.BaseDiffUtil
import com.karleinstein.karlrecy.BaseViewHolder
import com.karleinstein.karlrecy.listener.RecyclerAdapterListener

abstract class PagingRecycleAdapter<Item : Any>(
    diffCallback: DiffUtil.ItemCallback<Item> = BaseDiffUtil()
) : PagingDataAdapter<Item, BaseViewHolder>(diffCallback),
    RecyclerAdapterListener<Item> {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.run {
            onBind(holder, this, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(viewType, parent, false)
        ).apply { bindFirstTime(this) }
    }

    override fun bindFirstTime(baseViewHolder: BaseViewHolder) {}

    override fun getItemViewType(position: Int): Int {
        return buildLayoutRes(position)
    }
}
