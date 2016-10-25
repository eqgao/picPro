package gdut.com.picpro.utils;

import android.util.Log;

import gdut.com.picpro.MyApplication;

/**
 * Created by lchua on 2016/10/21.
 * <p>
 * 日志输出类
 */

public class Logger {
    public static void d(String tag, String message) {
        if (MyApplication.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable e) {
        if (MyApplication.DEBUG) {
            Log.e(tag, message, e);
        }
    }

    public static void i(String tag, String message) {
        if (MyApplication.DEBUG) {
            Log.i(tag, message);
        }
    }

}
