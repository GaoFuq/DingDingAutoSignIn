package com.gfq.usefuldemocollections.kt

import android.widget.Toast
import com.gfq.usefuldemocollections.App

fun String.toast(){
    Toast.makeText(App.context,this,Toast.LENGTH_SHORT).show()
}