package com.karleinstein.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.karleinstein.sample.expandable.ExpandableFragment
import com.karleinstein.sample.paging.PagingRecyclerFragment

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var btn: Button? = null
    var btn2: Button? = null
    var btn3: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        btn = findViewById<Button>(R.id.button)
        btn2 = findViewById<Button>(R.id.button2)
        btn3 = findViewById<Button>(R.id.button3)
        btn?.setOnClickListener(this)
        btn2?.setOnClickListener(this)
        btn3?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.main_container, ExpandableFragment())
                    .addToBackStack(null)
                    .commit()
            }
            R.id.button2 -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.main_container, PagingRecyclerFragment())
                    .addToBackStack(null)
                    .commit()
            }
            R.id.button3 -> {

            }
        }
    }
}
