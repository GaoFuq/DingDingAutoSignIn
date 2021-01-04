package com.gfq.usefuldemocollections;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liuxiaobo on 2018/11/1.
 */

public class BugTest {

    public void getBug(Context context) {
//        throw new NullPointerException();
        Toast.makeText(context, "xxxxx", Toast.LENGTH_SHORT).show();
    }
}
