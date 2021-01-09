package com.karleinstein.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.codelabs.paging.model.Repo
import com.karleinstein.karlrecy.*
import com.karleinstein.karlrecy.paging.PagingRecycleAdapter

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //    val btn = findViewById<Button>(R.id.button)
//    val btn2 = findViewById<Button>(R.id.button2)
//    val btn3 = findViewById<Button>(R.id.button3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        btn.setOnClickListener(this)
//        btn2.setOnClickListener(this)
//        btn3.setOnClickListener(this)
        val pagingAdapter = PagingRecycleAdapter(
            object : DiffUtil.ItemCallback<Repo>() {
                override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                    return oldItem == newItem
                }
            }
        )

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
        findViewById<RecyclerView>(R.id.recycle).run {
            lifecycleScope.launch {
                a().collectLatest {
                    Log.d("TAG", "onCreate: collectLatest: $it")
                    pagingAdapter.submitData(it)
                }
            }
            this.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = com.karleinstein.sample.LoadStateAdapter(),
                footer = com.karleinstein.sample.LoadStateAdapter()
            )
        }
    }

    private fun a(): Flow<PagingData<Repo>> {
        val repository = Injection.provideViewModelFactory()
        return repository.getSearchResultStream("Android").cachedIn(lifecycleScope)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
//            R.id.button -> {
//
//            }
//            R.id.button2 -> {
//
//            }
//            R.id.button3 -> {
//
//            }
        }
    }
}
