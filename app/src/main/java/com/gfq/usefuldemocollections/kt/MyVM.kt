package com.gfq.usefuldemocollections.kt

import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.arch.core.util.Function
import androidx.core.app.JobIntentService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MyVM : ViewModel() {
    private val _user = MutableLiveData<User>();

    val userName = Transformations.map(_user) { it.name }

    fun updateUser(user: User) {
        _user.value = user
    }
}

data class User(var name: String = "张三", var age: Int = 1)


