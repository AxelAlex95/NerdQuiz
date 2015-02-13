package matsematics.nerdquiz;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;

import Logging.Logger;

public class FullscreenLayoutActivity extends ActionBarActivity {
    private static final String TAG = "FullscreenLayoutActivity";

    /**
     * Overrides the onCreate Method to set the Activity to FullScreen
     * as well as hiding the Actionbar and setting the screen orientation to portrait
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setFullscreen();

        // Sets the Application to Potrait and this can not be changed
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Hides the Actionbar of the Activity
     */
    protected void hideActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    /**
     * Sets the Activity to Fullscreen
     */
    protected void setFullscreen()
    {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }
}

