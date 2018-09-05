package com.herve.coordinator;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by loongggdroid on 2016/5/12.
 */
public class BlurUtil {
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;

        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];

        int stackpointer;

        int stackstart;

        int[] sir;

        int rbs;

        int r1 = radius + 1;

        int routsum, goutsum, boutsum;

        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {

            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;

            for (i = -radius; i <= radius; i++) {

                p = pix[yi + Math.min(wm, Math.max(i, 0))];

                sir = stack[i + radius];

                sir[0] = (p & 0xff0000) >> 16;

                sir[1] = (p & 0x00ff00) >> 8;

                sir[2] = (p & 0x0000ff);

                rbs = r1 - Math.abs(i);

                rsum += sir[0] * rbs;

                gsum += sir[1] * rbs;

                bsum += sir[2] * rbs;

                if (i > 0) {

                    rinsum += sir[0];

                    ginsum += sir[1];

                    binsum += sir[2];

                } else {

                    routsum += sir[0];

                    goutsum += sir[1];

                    boutsum += sir[2];

                }

            }

            stackpointer = radius;


            for (x = 0; x < w; x++) {


                r[yi] = dv[rsum];

                g[yi] = dv[gsum];

                b[yi] = dv[bsum];

                rsum -= routsum;

                gsum -= goutsum;

                bsum -= boutsum;

                stackstart = stackpointer - radius + div;

                sir = stack[stackstart % div];

                routsum -= sir[0];

                goutsum -= sir[1];

                boutsum -= sir[2];

                if (y == 0) {

                    vmin[x] = Math.min(x + radius + 1, wm);

                }

                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;

                sir[1] = (p & 0x00ff00) >> 8;

                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];

                ginsum += sir[1];

                binsum += sir[2];

                rsum += rinsum;

                gsum += ginsum;

                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;

                sir = stack[(stackpointer) % div];

                routsum += sir[0];

                goutsum += sir[1];

                boutsum += sir[2];

                rinsum -= sir[0];

                ginsum -= sir[1];

                binsum -= sir[2];

                yi++;

            }

            yw += w;

        }

        for (x = 0; x < w; x++) {

            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;

            yp = -radius * w;

            for (i = -radius; i <= radius; i++) {

                yi = Math.max(0, yp) + x;


                sir = stack[i + radius];


                sir[0] = r[yi];

                sir[1] = g[yi];

                sir[2] = b[yi];


                rbs = r1 - Math.abs(i);


                rsum += r[yi] * rbs;

                gsum += g[yi] * rbs;

                bsum += b[yi] * rbs;


                if (i > 0) {

                    rinsum += sir[0];

                    ginsum += sir[1];

                    binsum += sir[2];

                } else {

                    routsum += sir[0];

                    goutsum += sir[1];

                    boutsum += sir[2];

                }


                if (i < hm) {

                    yp += w;

                }

            }

            yi = x;

            stackpointer = radius;

            for (y = 0; y < h; y++) {

                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)

                        | (dv[gsum] << 8) | dv[bsum];


                rsum -= routsum;

                gsum -= goutsum;

                bsum -= boutsum;


                stackstart = stackpointer - radius + div;

                sir = stack[stackstart % div];


                routsum -= sir[0];

                goutsum -= sir[1];

                boutsum -= sir[2];


                if (x == 0) {

                    vmin[y] = Math.min(y + r1, hm) * w;

                }

                p = x + vmin[y];


                sir[0] = r[p];

                sir[1] = g[p];

                sir[2] = b[p];


                rinsum += sir[0];

                ginsum += sir[1];

                binsum += sir[2];


                rsum += rinsum;

                gsum += ginsum;

                bsum += binsum;


                stackpointer = (stackpointer + 1) % div;

                sir = stack[stackpointer];


                routsum += sir[0];

                goutsum += sir[1];

                boutsum += sir[2];


                rinsum -= sir[0];

                ginsum -= sir[1];

                binsum -= sir[2];


                yi += w;

            }

        }


        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);

    }


    public static void setTranslateStatusBar(Activity activity, int topId) {
        View status_bar = activity.findViewById(topId);// 标题栏id
        if (status_bar != null) {
            setTranslateStatusBar(activity, status_bar);
        }
    }

    public static void setTranslateStatusBar(Activity activity, View view) {
        // 4.4以上处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // android
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明
            if (view != null) {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = getStatusBarHeight(activity);
                view.setLayoutParams(params);
            }
        }

        //5.0 以上处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem().getDisplayMetrics());
    }
}
