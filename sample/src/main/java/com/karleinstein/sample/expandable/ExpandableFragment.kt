package com.karleinstein.sample.expandable

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karleinstein.karlrecy.expandable.ExpandableDiffUtil
import com.karleinstein.karlrecy.expandable.ExpandableItem
import com.karleinstein.sample.R
import com.karleinstein.sample.utils.GenerateData

class ExpandableFragment : Fragment() {

    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    val adapter = com.karleinstein.sample.ExpandableListAdapter(object :
        DiffUtil.ItemCallback<ExpandableItem>() {
        override fun areItemsTheSame(
            oldItem: ExpandableItem,
            newItem: ExpandableItem
        ): Boolean {
            return ExpandableDiffUtil.areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(
            oldItem: ExpandableItem,
            newItem: ExpandableItem
        ): Boolean {
            return ExpandableDiffUtil.areContentTheSame(oldItem, newItem)
        }

    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycle)
        adapter.registerExpandable(GenerateData.expandableData)
        recyclerView?.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.layout_add_remove_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                GenerateData.addNewRandomExpandableItem()
                adapter.registerExpandable(
                    GenerateData.expandableData
                )
                return true
            }
            R.id.item_del -> {
                GenerateData.delNewRandomExpandableItem()
                adapter.registerExpandable(GenerateData.expandableData)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
