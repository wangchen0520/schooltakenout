package com.wy.schooltakenout.HomePage.OrderView.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Author: Owen Chan
 * DATE: 2016-11-24.
 */

public class Utils {

    private static Utils mInstance;

    private Utils() {}

    public static Utils getInstance() {
        if (mInstance == null) {
            synchronized (Utils.class) {
                if (mInstance == null) {
                    mInstance = new Utils();
                }
            }
        }
        return mInstance;
    }

    //获取Text高度
    public int getTextHeight(String str, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return (int) (rect.height() / 33f * 29);
    }

    //获取Text宽度
    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public boolean isPointInCircle(PointF pointF, PointF circle, float radius) {
        return Math.pow((pointF.x - circle.x), 2) + Math.pow((pointF.y - circle.y), 2) <= Math.pow(radius, 2);
    }
}
