package matsematics.nerdquiz;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 *         Section that provide the core implementation as well as
 *         Logging features in case that Logging is enabled
 *
 * Created by Sommer on 11.02.2015.
 */
public class FullscreenLayoutActivity extends ActionBarActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            hideActionBar();
            setFullscreen();
        }
        protected void hideActionBar()
        {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }

        protected void setFullscreen()
        {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        }
    }

