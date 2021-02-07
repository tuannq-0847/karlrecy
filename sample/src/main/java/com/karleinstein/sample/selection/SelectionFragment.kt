package com.karleinstein.sample.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.karleinstein.karlrecy.BaseRecyclerAdapter
import com.karleinstein.karlrecy.BaseViewHolder
import com.karleinstein.sample.R
import com.karleinstein.sample.utils.GenerateData

class SelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SelectionAdapter()
        view.findViewById<RecyclerView>(R.id.recycle_sel).adapter = adapter
        adapter.register(GenerateData.getSelectionData())
    }
}
