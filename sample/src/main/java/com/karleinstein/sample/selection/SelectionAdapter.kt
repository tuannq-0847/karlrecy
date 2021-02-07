package com.karleinstein.sample.selection

import android.widget.CheckBox
import com.karleinstein.karlrecy.BaseViewHolder
import com.karleinstein.karlrecy.selection.BaseSelectionRecyclerAdapter
import com.karleinstein.sample.R

class SelectionAdapter : BaseSelectionRecyclerAdapter<String>() {

    override fun buildLayoutRes(position: Int): Int {
        return R.layout.item_selected
    }

    override fun onBindSelection(
        baseViewHolder: BaseViewHolder,
        item: String,
        isSelected: Boolean?,
        position: Int
    ) {
        val checkBox = baseViewHolder.itemView.findViewById<CheckBox>(R.id.check_box)
        checkBox.text = item
        isSelected?.let { checkBox.isChecked = it }
    }

    override fun onStateChanged(baseViewHolder: BaseViewHolder, isStateChanged: Boolean) {
        super.onStateChanged(baseViewHolder, isStateChanged)
        val checkBox = baseViewHolder.itemView.findViewById<CheckBox>(R.id.check_box)
        checkBox.isChecked = isStateChanged
    }
}
