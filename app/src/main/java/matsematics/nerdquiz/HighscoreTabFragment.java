package matsematics.nerdquiz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Logging.Logger;

import static matsematics.nerdquiz.FileUtils.readInt32;
import static matsematics.nerdquiz.FileUtils.readString;

public class HighscoreTabFragment extends Fragment {

    public static final String TAB_LAYOUT_NUMBER = "Layout";

    public HighscoreTabFragment(String tabName) {
        switch (tabName) {
            case "Local" : readHighscoresFromFile(); break;
            case "Global": readHighscoresFromDB();   break;
            default:                                 break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getArguments().getInt(TAB_LAYOUT_NUMBER), container, false);
    }

    private void readHighscoresFromFile() {
        FileInputStream ifs;

        try {
            ifs = new FileInputStream(new File(HighscoreActivity.FILEPATH));

            ArrayList<String> arrayList = new ArrayList<>();

            for (int i = 0; i < HighscoreActivity.HIGHSCORE_LIMIT; ++i) {
                String str = readString(ifs);

                if (str == null) {
                    // TODO Fehlermeldung ausgeben
                    ifs.close();
                    return;
                }

                str.concat("\t\t");
                str += readInt32(ifs);

                arrayList.add(str);
            }

            ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
            ListView lv = (ListView) getView().findViewById(R.id.highscore_local_list);
            lv.setAdapter(listAdapter);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Fehlermeldung ausgeben
        }
    }

    private void readHighscoresFromDB() {
        // TODO

        // Dummy-Implementation
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Place 1\t\t" + 100);
        arrayList.add(   "Alex\t\t" +  90);
        arrayList.add(    "Lia\t\t" +  80);
        arrayList.add(   "Axel\t\t" +  70);
        arrayList.add("Place 5\t\t" +  60);
        arrayList.add("Place 6\t\t" +  50);
        arrayList.add("Place 7\t\t" +  40);
        arrayList.add("Place 8\t\t" +  30);
        arrayList.add("Place 9\t\t" +  20);
        arrayList.add(  "Robin\t\t" +  10);

        ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        ListView lv = (ListView) getView().findViewById(R.id.highscore_global_list);
        lv.setAdapter(listAdapter);
    }
}