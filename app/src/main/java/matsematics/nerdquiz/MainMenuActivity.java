package matsematics.nerdquiz;

import android.content.Intent;
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
 *
 * @author Nell, Schwenke, Schulze-Dephoff, Sommer
 */
public class MainMenuActivity extends FullscreenLayoutActivity {
    private static final String TAG = "MainMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.startLogging();
        Logger.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    /**
     * Quits the Game as if the Back Button was pressed
     *
     * @param view  The button that was pressed
     */
    public void quitGame(View view) {
        Logger.i(TAG, "quitGame");
        Logger.writeLog();
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
     * @param view  The button that was presse
     */
    public void showHighscores(View view) {
        Logger.i(TAG, "showHighscores");
        startActivity(new Intent(this, LocalHighscoreActivity.class));
    }

    /**
     * Starts the game with the categories that were selected last
     *
     * @param view  The button that was pressed
     */
    public void startGame(View view) {
        Logger.i(TAG, "startGame");
        startActivity(new Intent(this, GameActivity.class));
    }
}
