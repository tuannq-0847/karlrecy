package com.karleinstein.sample.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import com.karleinstein.sample.model.Repo
import com.karleinstein.karlrecy.BaseViewHolder
import com.karleinstein.karlrecy.paging.PagingRecycleAdapter
import com.karleinstein.sample.LoadStateAdapter
import com.karleinstein.sample.R
import com.karleinstein.sample.injection.Injection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingRecyclerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagingAdapter = object : PagingRecycleAdapter<Repo>() {
            override fun onBind(holder: BaseViewHolder, item: Repo, position: Int) {
                val text_test = holder.itemView.findViewById<TextView>(R.id.tv_text)
                text_test.text = item.fullName
            }

            override fun buildLayoutRes(position: Int): Int {
                return R.layout.item_text
            }

        }
        pagingAdapter

        view.findViewById<RecyclerView>(R.id.recycle).run {
            lifecycleScope.launch {
                a().collectLatest {
                    pagingAdapter.submitData(it)
                }
            }
            this.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter(),
                footer = LoadStateAdapter()
            )
        }
    }

    private fun a(): Flow<PagingData<Repo>> {
        val repository = Injection.provideViewModelFactory()
        return repository.getSearchResultStream("Android").cachedIn(lifecycleScope)
    }
}