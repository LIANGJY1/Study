package com.example.study.util.tool;

import android.content.Context;

public class DensityUtil {

    /**
     * Depending on the resolution of the phone, change px to dp
     */
    public static int pxToDp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }
}
