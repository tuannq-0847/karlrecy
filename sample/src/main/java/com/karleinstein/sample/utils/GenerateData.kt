package com.karleinstein.sample.utils

import com.karleinstein.karlrecy.expandable.ChildItem
import com.karleinstein.karlrecy.expandable.ExpandableData
import com.karleinstein.karlrecy.expandable.GroupItem
import com.karleinstein.sample.ExpandableDataSample
import com.karleinstein.sample.R

object GenerateData {

    val expandableData =
        mutableListOf(
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
            ),
            ExpandableData(
                groupItem = GroupItem(
                    ExpandableDataSample(
                        "Mixed Type Section",
                        R.drawable.car5
                    )
                ),
                childItems = *arrayOf(
                    ChildItem(
                        ExpandableDataSample(
                            "My Car",
                            R.drawable.car6
                        )
                    ),
                    ChildItem("Coding")
                )
            )
        )

    fun addNewRandomExpandableItem() {
        val intRandom = (0..10).random()
        if (intRandom % 2 == 0)
            expandableData.add(
                0,
                ExpandableData(
                    groupItem = GroupItem("Group: $intRandom"),
                    childItems = *arrayOf(
                        ChildItem("Child: $intRandom"),
                        ChildItem("Child: $intRandom"),
                    )

                )
            )
        else
            expandableData
                .add(
                    0,
                    ExpandableData(
                        groupItem = GroupItem("Group: $intRandom")
                    )
                )
    }

    fun delNewRandomExpandableItem() {
        if (expandableData.isNotEmpty())
            expandableData.removeAt(0)
    }
}
