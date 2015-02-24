package Logging;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;

/**
 * Wrapper fuer die Log-Klasse von android.java.util
 * mit Erweiteter Funktionalität zum schreiben von Log-Dateien
 *
 * Created by Sommer on 11.02.2015.
 */
public class Logger {
    private static boolean              LOGGING = false;
    private static ArrayList<String>    logEntries;
    private static Time                 time;

    /**
     * Sets the LOGGING parameter on true or false
     *
     * @param log   Enables or Disables Logging
     */
    private static void setLogging(boolean log) {
        LOGGING = log;
    }

    /**
     * Creates a new Log
     */
    private static void newLog() {
        logEntries = new ArrayList<String>();
    }

    /**
     * Deletes all entries from the Log
     */
    public static void clearLog() {
        logEntries.clear();
    }

    /**
     * Starts logging, creates a new Log if none has been created yet
     */
    public static void startLogging() {
        setLogging(true);
        if (logEntries == null) newLog();
    }

    /**
     * Stops logging, but keeps the Log
     */
    public static void stopLogging() {
        setLogging(false);
    }

    /**
     * Writes the Log to a File
     */
    public static void writeLog() {
        /*
        for (String log : logEntries) {

        }
        */
    }

    private static void LogEntry(String cat, String tag, String msg) {
        time = new Time();
        time.setToNow();

        logEntries.add(String.format("%04d-%02d-%02d %02d:%02d:%02d \t %s/%s: %s",
                time.year, (time.month + 1), time.monthDay, time.hour, time.minute, time.second,
                cat, tag, msg));
    }

    /****************************************
     * Wrapper fuer android.java.util.Log   *
     ***************************************/

    /**
     * Logs a Debug Message
     *
     * @param tag   Class in which this message originated
     * @param msg   Include the Method that produced this message
     */
    public static void d(String tag, String msg) {
        if(LOGGING) {
            if(LOGGING) {
                LogEntry("D", tag, msg);
                Log.d(tag, msg);
            }
        }
    }

    /**
     * Logs a Debug Message
     *
     * @param tag   Class in which this message originated
     * @param msg   Include the Method that produced this message
     * @param tr    Exception that occurred
     */
    public static void d(String tag, String msg, Throwable tr) {
        if(LOGGING)
            if(LOGGING) {
                LogEntry("D", tag, msg);
                Log.d(tag, msg, tr);
            }
    }

    /**
     * Logs an Error Message
     *
     * @param tag   Class that produced this Error
     * @param msg   Include the Method that this Error has occurred in
     */
    public static void e(String tag, String msg) {
        if(LOGGING)
            if(LOGGING) {
                LogEntry("E", tag, msg);
                Log.e(tag, msg);
            }
    }

    /**
     * Logs an Error Message
     *
     * @param tag   Class that produced this Error
     * @param msg   Include the Method that this Error has occurred in
     * @param tr    Exception that occurred
     */
    public static void e(String tag, String msg, Throwable tr) {
        if(LOGGING) {
            LogEntry("E", tag, msg);
            Log.e(tag, msg, tr);
        }
    }

    /**
     * Logs an Info Message
     *
     * @param tag   Class that produced this Info
     * @param msg   Include the Method that produced this message
     */
    public static void i(String tag, String msg) {
        if(LOGGING) {
            LogEntry("I", tag, msg);
            Log.i(tag, msg);
        }
    }

    /**
     * Logs an Info Message
     *
     * @param tag   Class that produced this Info
     * @param msg   Include the Method that produced this message
     * @param tr    Exception that occurred
     */
    public static void i(String tag, String msg, Throwable tr) {
        if(LOGGING) {
            LogEntry("I", tag, msg);
            Log.i(tag, msg, tr);
        }
    }

    /**
     * Logs a Verbose Message
     *
     * @param tag   Class that produced this Verbose
     * @param msg   Include the Method that produced this message
     */
    public static void v(String tag, String msg) {
        if(LOGGING) {
            LogEntry("V", tag, msg);
            Log.v(tag, msg);
        }
    }

    /**
     * Logs a Verbose Message
     *
     * @param tag   Class that produced this Verbose
     * @param msg   Include the Method that produced this message
     * @param tr    Exception that occurred
     */
    public static void v(String tag, String msg, Throwable tr) {
        if(LOGGING)
            if(LOGGING) {
                LogEntry("V", tag, msg);
                Log.v(tag, msg, tr);
            }
    }

    /**
     * Logs a Warning Message - this does not Log the Message in LogEntries
     *
     * @param tag   Class that produced this Warning
     * @param tr    Exception that occurred
     */
    public static void w(String tag, Throwable tr) {
        if(LOGGING)
            Log.w(tag, tr);
    }

    /**
     * Logs a Warning Message
     *
     * @param tag   Class that produced this Warning
     * @param msg   Include the Method that produced this message
     */
    public static void w(String tag, String msg) {
        if(LOGGING)
            if(LOGGING) {
                LogEntry("W", tag, msg);
                Log.w(tag, msg);
            }
    }

    /**
     * Logs a Warning Message
     *
     * @param tag   Class that produced this Warning
     * @param msg   Include the Method that produced this message
     * @param tr    Exception that occurred
     */
    public static void w(String tag, String msg, Throwable tr) {
        if(LOGGING)
            if(LOGGING) {
                LogEntry("W", tag, msg);
                Log.w(tag, msg, tr);
            }
    }

    public static void wtf(String tag, Throwable tr) {
        if(LOGGING)
            Log.wtf(tag, tr);
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     *
     * @param tag   Class this occurred in
     * @param msg   Method this occurred in
     */
    public static void wtf(String tag, String msg) {
        if(LOGGING)
            if(LOGGING) {
                LogEntry("WTF", tag, msg);
                Log.wtf(tag, msg);
            }
    }

    /**
     * What a Terrible Failure: Report an exception that should never happen.
     *
     * @param tag   Class this occurred in
     * @param msg   Method this occurred in
     * @param tr    Exception that occurred
     */
    public static void wtf(String tag, String msg, Throwable tr) {
        if(LOGGING)
            if(LOGGING) {
                LogEntry("WTF", tag, msg);
                Log.wtf(tag, msg, tr);
            }
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
