package matsematics.nerdquiz;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import Logging.Logger;

/**
 * The starting Activity which provides the menu and the basic options for this game
 * Starting a new game
 * Selecting Categories
 * and taking a look at local and global Highscores
 */
public class MainMenuActivity extends FullscreenLayoutActivity {
    private static final String TAG = "MainMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.startLogging();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    /**
     * Quits the Game as if the Back Button was pressed
     *
     * @param view  The button that was pressed
     */
    public void quitGame(View view) {
        Logger.i(TAG, "quitGame");
        onBackPressed();
    }

    /**
     * Shows a List of Categories and provides the option to start a game from there
     *
     * @param view  The button that was pressed
     */
    public void showCategories(View view) {
        Logger.i(TAG, "showCategories");
        startActivity(new Intent(this, CategoryActivity.class));
    }

    /**
     * Shows local and global highscores for all games
     *
     * @param view  The button that was pressed
     */
    public void showHighscores(View view) {
        Logger.i(TAG, "showHighscores");
        //startActivity(new Intent(this, HighscoreActivity.class));
    }

    /**
     * Starts the game with the categories that were selected last
     *
     * @param view  The button that was pressed
     */
    public void startGame(View view) {
        Logger.i(TAG, "startGame");
        startActivity(new Intent(this,StartGameActivity.class));
    }
}
