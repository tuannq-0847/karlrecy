@file:Suppress("UNCHECKED_CAST")

package com.karleinstein.karlrecy.expandable

class ExpandableData<G, C : Any>(
    val groupItem: GroupItem<G>,
    vararg val childItems: ChildItem<out C>
)

sealed class ExpandableItem

data class GroupItem<G>(
    val input: G,
    var isExpand: Boolean = false
) : ExpandableItem()

data class ChildItem<C>(val input: C) : ExpandableItem()

fun <C> ChildItem<*>.toChildData() = input as? C

fun <G> GroupItem<*>.toGroupData() = input as? G

fun List<ExpandableItem>.setStateChildView(
    isExpand: Boolean,
    position: Int
): List<ExpandableItem> {
    val current = this[position]
    if (current is GroupItem<*>)
        current.isExpand = isExpand
    return getData(this)
}

fun <G : Any, C : Any> List<ExpandableData<out G, out C>>.initList(): List<ExpandableItem> {
    val res = mutableListOf<ExpandableItem>()
    map {
        it.groupItem.isExpand = false
        res.add(it.groupItem)
        res.addAll(it.childItems)
    }
    return res
}

fun getData(currentList: List<ExpandableItem>): List<ExpandableItem> {
    val result = mutableListOf<ExpandableItem>()
    currentList.forEachIndexed { index, expandableItem ->
        var flexibleIndex = index
        if (expandableItem is GroupItem<*>) {
            result.add(expandableItem)
            if (expandableItem.isExpand) {
                flexibleIndex++
                while (flexibleIndex < currentList.size && currentList[flexibleIndex] is ChildItem<*>) {
                    result.add(currentList[flexibleIndex])
                    flexibleIndex++
                }
            }

        }
    }
    return result
}

object ExpandableDiffUtil {

    fun areItemsTheSame(oldData: ExpandableItem, newData: ExpandableItem): Boolean {
        return when {
            oldData is ChildItem<*> && newData is ChildItem<*> -> {
                oldData.input == newData.input
            }
            oldData is GroupItem<*> && newData is GroupItem<*> -> {
                oldData.input == newData.input
            }
            else -> false
        }
    }

    fun areContentTheSame(oldData: ExpandableItem, newData: ExpandableItem): Boolean {
        return oldData == newData
    }
}
