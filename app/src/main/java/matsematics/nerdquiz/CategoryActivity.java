package matsematics.nerdquiz;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import Logging.Logger;


public class CategoryActivity extends FullscreenLayoutActivity {
    private static final String TAG = "CategoryActivity";
    private ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.startLogging();
        Logger.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getCategories();
        listCategories();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Logger.i(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.i(TAG, "onCreate");

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGame(View view){
        Logger.i(TAG, "startGame");
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }

    /**
     * Creates a Checkbox for each category and gives the CheckBox the corresponding ID from the ArrayList
     */
    private void listCategories() {
        Logger.i(TAG, "listCategories");
        LinearLayout categories_layout = (LinearLayout)findViewById(R.id.category_layout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 2;
        params.topMargin = 2;

        for (int i = 0; i < categories.size(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setId(i);
            checkBox.setText(categories.get(i));
            categories_layout.addView(checkBox, params);
            checkBox.setPadding(45,0,0,0);
            checkBox.setBackgroundResource(R.drawable.green);
            checkBox.setChecked(true);
        }
    }

    /**
     * ArrayList with all Categories
     */
    private void getCategories() {
        Logger.i(TAG, "getCategories");
        categories = new ArrayList<String>();

        AssetManager assetManager = getAssets();
        String s;
        Scanner sc;
        InputStream input;

        try {
            input = assetManager.open("Kategorien.txt");
            String answer;

            sc = new Scanner(input, "UTF-8");

            while (sc.hasNextLine()) {
                s = sc.nextLine();
                if (s.length() > 0 ) {
                    categories.add(s.trim());
                }
            }
        } catch (IOException e) {
            Logger.i(TAG, "getCategories", e);
            e.printStackTrace();
        }
    }
}
