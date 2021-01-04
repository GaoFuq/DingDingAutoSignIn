package com.gfq.usefuldemocollections

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gfq.usefuldemocollections.kt.net.ApiService
import com.gfq.usefuldemocollections.kt.MyVM
import com.gfq.usefuldemocollections.kt.User
import com.gfq.usefuldemocollections.kt.net.Repository
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        btn.setOnClickListener { dgView.expandWidth() }

//        tv_expandW.setOnClickListener { dgLayout.expandWidth() }
//        tv_expandH.setOnClickListener { dgLayout.expandHeight() }
//        tv_reset.setOnClickListener { dgLayout.reset() }

//
//       iv.setOnClickListener{
//           Repository.getJoke().observe(this, Observer {
//               Log.e("accessibility_service_config",it)
//           })
//       }


    }

}




