package com.karleinstein.sample

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.karleinstein.karlrecy.BaseViewHolder

class LoadStateAdapter : LoadStateAdapter<BaseViewHolder>() {

    override fun onBindViewHolder(holder: BaseViewHolder, loadState: LoadState) {
        val errorMsg = holder.itemView.findViewById<TextView>(R.id.error_msg)
        val progressBar = holder.itemView.findViewById<ProgressBar>(R.id.progress_bar)
        val retryButton = holder.itemView.findViewById<Button>(R.id.retry_button)
        Log.d("LoadStateAdapter", "onBindViewHolder: $loadState")
        when (loadState) {
            is LoadState.Error -> {
                errorMsg.text = loadState.error.localizedMessage
            }
            else -> {
            }
        }
        progressBar.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState !is LoadState.Loading
        errorMsg.isVisible = loadState !is LoadState.Loading
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BaseViewHolder {
        return BaseViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.repos_load_state_footer_view_item, parent, false),
            parent
        )
    }
}