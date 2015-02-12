package Logging;

import android.text.format.Time;

/**
 * Class that generates a Log Message with the Tag and Msg given to Logger
 *
 * Created by Sommer on 12.02.2015.
 */
public class LogEntry {
    private static final String TAG = "LogEntry";
    private String logMsg;

    public LogEntry(String cat, String tag, String msg) {
        Time time = new Time();
        time.setToNow();

        this.logMsg =  time.year + "." + (time.month + 1) + "." + time.monthDay + "\t" + cat + "/" + tag + ": " + msg;
    }

    @Override
    public String toString() {
        return this.logMsg;
    }
}
