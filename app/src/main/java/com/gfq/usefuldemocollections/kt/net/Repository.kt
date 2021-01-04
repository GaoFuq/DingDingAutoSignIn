package com.gfq.usefuldemocollections.kt.net

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {



    private fun <T> fire(context: CoroutineContext, block: suspend () -> T) =
        liveData<T>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            emit(result as T)
        }
}