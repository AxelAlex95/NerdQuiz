package Logging;

import android.util.Log;

/**
 * Wrapper fuer die Log-Klasse von android.java.util
 * mit Erweiteter Funktionalität zum schreiben von Log-Dateien
 *
 * Created by Sommer on 11.02.2015.
 */
public abstract class Logger implements ILogger {
    private static final String TAG = "Logger";
    private static boolean LOGGING = false;

    public static void setLogging(boolean log) {
        LOGGING = log;
    }

    /****************************************
     * Wrapper fuer android.java.util.Log   *
     ***************************************/
    public static void d(String tag, String msg) {
        if(LOGGING)
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if(LOGGING)
            Log.d(tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        if(LOGGING)
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        if(LOGGING)
            Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if(LOGGING)
            Log.i(tag, msg, tr);
    }

    public static void v(String tag, String msg) {
        if(LOGGING)
            Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if(LOGGING)
            Log.v(tag, msg, tr);
    }

    public static void w(String tag, Throwable tr) {
        if(LOGGING)
            Log.w(tag, tr);
    }

    public static void w(String tag, String msg) {
        if(LOGGING)
            Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if(LOGGING)
            Log.w(tag, msg, tr);
    }

    public static void wtf(String tag, Throwable tr) {
        if(LOGGING)
            Log.wtf(tag, tr);
    }

    public static void wtf(String tag, String msg) {
        if(LOGGING)
            Log.wtf(tag, msg);
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        if(LOGGING)
            Log.wtf(tag, msg, tr);
    }

    public static void getStackTraceString(Throwable tr) {
        if(LOGGING)
            Log.getStackTraceString(tr);
    }

    public static void isLoggable(String tag, int level) {
        if(LOGGING)
            Log.isLoggable(tag, level);
    }
}
