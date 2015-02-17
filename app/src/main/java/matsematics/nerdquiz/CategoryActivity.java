package matsematics.nerdquiz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logger.i(TAG, "onCreate");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGame(View view){
        Logger.i(TAG, "startGame");
        Intent intent = new Intent(this,StartGameActivity.class);
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
     *
     * @return  ArrayList with all Categories
     */
    private void getCategories() {
        Logger.i(TAG, "getCategories");
        categories = new ArrayList<String>();

        categories.add("How I met your mother");
        categories.add("IT");
        categories.add("Retro Games");
        categories.add("Star Wars");
        categories.add("Start Game");
        categories.add("The Big Bang Theory");
        categories.add("Tolkien\'s Legendarium");
    }
}
