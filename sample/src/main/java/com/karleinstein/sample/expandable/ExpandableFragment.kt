package com.karleinstein.sample.expandable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karleinstein.karlrecy.*
import com.karleinstein.sample.R

class ExpandableFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val expandableData =
            listOf(
                ExpandableData(
                    groupItem = GroupItem("Text Type"),
                    childItems = *arrayOf(
                        ChildItem("Kotlin"),
                        ChildItem("PHP"),
                        ChildItem("C/C++"),
                        ChildItem("Swift"),
                        ChildItem("Javascript")
                    )
                ),
                ExpandableData(
                    groupItem = GroupItem("Image Type"),
                    childItems = *arrayOf(
                        ChildItem(R.drawable.ac_milan),
                        ChildItem(R.drawable.juvetus),
                        ChildItem(R.drawable.barcelona)
                    )
                )
                ,
                ExpandableData(
                    groupItem = GroupItem("Mixed Type"),
                    childItems = *arrayOf(
                        ChildItem(
                            com.karleinstein.sample.ExpandableDataSample(
                                "Manchester United",
                                R.drawable.mu
                            )
                        ),
                        ChildItem("Coding")
                    )
                ),
                ExpandableData(
                    groupItem = GroupItem(
                        com.karleinstein.sample.ExpandableDataSample(
                            "Mixed Type Section",
                            R.drawable.car5
                        )
                    ),
                    childItems = *arrayOf(
                        ChildItem(
                            com.karleinstein.sample.ExpandableDataSample(
                                "My Car",
                                R.drawable.car6
                            )
                        ),
                        ChildItem("Coding")
                    )
                )
            )
        adapter.register(expandableData)
        view.findViewById<RecyclerView>(R.id.recycle).run {
            this.adapter = adapter
        }
    }
}
