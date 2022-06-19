package com.example.sample2

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.karleinstein.karlrecy.*
import com.karleinstein.karlrecy.expandable.BaseExpandRecyclerAdapter
import com.karleinstein.karlrecy.expandable.ChildItem
import com.karleinstein.karlrecy.expandable.ExpandableItem
import com.karleinstein.karlrecy.expandable.GroupItem
import com.karleinstein.sample.ExpandableDataSample

class ExpandableListAdapter(callback: DiffUtil.ItemCallback<ExpandableItem>) :
    BaseExpandRecyclerAdapter<Any, Any>(callback) {

    override fun buildLayoutRes(position: Int): Int {
        return when (val item = currentList[position]) {
            is ChildItem<*> -> {
                when {
                    checkType<String>(item.input!!) -> R.layout.item_text
                    checkType<Int>(item.input!!) -> {
                        R.layout.item_type_image
                    }
                    else -> R.layout.item_type_image_text
                }
            }
            is GroupItem<*> -> {
                when {
                    checkType<ExpandableDataSample>(item.input!!) -> R.layout.item_group_mix_text_image
                    else -> R.layout.item_group
                }
            }
            else -> R.layout.item_group
        }
    }

    override fun isExpandedListener(baseViewHolder: BaseViewHolder, isExpanded: Boolean) {
        super.isExpandedListener(baseViewHolder, isExpanded)
        val imageOpen = baseViewHolder.itemView.findViewById<ImageView?>(R.id.image_open)
        imageOpen?.setImageResource(
            if (isExpanded) R.drawable.ic_baseline_expand_less_24
            else R.drawable.ic_baseline_expand_more_24
        )
    }

    override fun onBindGroup(itemView: View, item: Any?) {
        if (item is ExpandableDataSample) {
            val textExpandable = itemView.findViewById<TextView?>(R.id.text_title)
            val imageExpandable = itemView.findViewById<ImageView?>(R.id.image_car)
            textExpandable?.text = item.title
            imageExpandable?.setImageResource(
                item.imageSource
            )
        } else {
            val tvTest = itemView.findViewById<TextView>(R.id.tv_test)
            tvTest.text = item.toString()
        }
    }

    override fun onBindChild(itemView: View, item: Any?) {
        item?.let {
            when (it) {
                is ExpandableDataSample -> {
                    val textExpandable = itemView.findViewById<TextView?>(R.id.text_expandable)
                    val imageExpandable = itemView.findViewById<ImageView?>(R.id.image_expandable)
                    textExpandable?.text = it.title
                    imageExpandable?.setImageResource(
                        it.imageSource
                    )
                }
                is Int -> {
                    val imageExpandable = itemView.findViewById<ImageView?>(R.id.image_test)
                    imageExpandable?.setImageResource(
                        it
                    )
                }
                else -> {
                    val tvTest = itemView.findViewById<TextView>(R.id.tv_text)
                    tvTest.text = it.toString()
                }
            }
        }
    }
}
