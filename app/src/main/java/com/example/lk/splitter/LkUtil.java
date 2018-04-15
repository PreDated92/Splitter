package com.example.lk.splitter;

import android.widget.EditText;

/**
 * Created by leeketee on 4/5/2018.
 */

public class LkUtil {
    public static boolean isEmpty(EditText et) {
        return isEmpty(et.getText().toString());
    }

    public static boolean isEmpty(String text) {
        return (text == null) || text.trim().length() == 0;
    }
}
