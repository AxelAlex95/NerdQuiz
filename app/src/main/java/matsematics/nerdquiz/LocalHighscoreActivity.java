package matsematics.nerdquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Logging.Logger;


public class LocalHighscoreActivity extends FullscreenLayoutActivity {
    private static final String                     TAG             = "LocalHighscoreActivity";
    private SimpleAdapter                           adapterEntries;
    private static ArrayList<HashMap<String, String>>      entries;
    private static final String myFile = "highscore_local";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_highscore);

        entries = getLocalEntries();

        adapterEntries = new SimpleAdapter(this, entries, R.layout.row_highscore, new String[] {"name", "score"}, new int[] {R.id.NAME_CELL, R.id.SCORE_CELL});

        ListView lvNames = (ListView) findViewById(R.id.listViewScores);
        lvNames.setAdapter(adapterEntries);
    }

    /**
     * Reads the file from Shared Prefs
     *
     * @return
     */
    private ArrayList<HashMap<String, String>> getLocalEntries() {
        Logger.i(TAG, "getLocalEntries ");

        ArrayList<HashMap<String, String>> entries = new ArrayList<HashMap<String, String>>();

        for (int i = 1; i <= 10; i = i + 1) {
            final String name = "Placeholder " + i;
            final int score = i * 10;

            if (entries.isEmpty() || score < Integer.parseInt(entries.get(entries.size() - 1).get("score"))) {
                entries.add(new HashMap<String, String>() {{
                    put("name", name);
                    put("score", score + "");
                }});
            }

            for (int j = 0; j < entries.size(); j++) {
                if (Integer.parseInt(entries.get(j).get("score")) < score) {
                    entries.add(j, new HashMap<String, String>() {{
                        put("name", name);
                        put("score", score + "");
                    }});
                    break;
                }
            }
        }

        return entries;
    }

    private void saveHighscores(ArrayList<HashMap<String, String>> scoreList) {

    }

    public void addHighscore(String name, int score) {
        Logger.i(TAG, "addHighscore ");

    }
}
