package matsematics.nerdquiz;

import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class HighscoreActivity extends FullscreenLayoutActivity implements ActionBar.TabListener {

    public static final int    HIGHSCORE_LIMIT = 20;
    public static final String FILEPATH        = "";

    private ArrayList<Fragment> tabs;

    private void addTab(int fragment, String tabName) {
        Bundle args = new Bundle();
        args.putInt(HighscoreTabFragment.TAB_LAYOUT_NUMBER, fragment);

        HighscoreTabFragment tab = new HighscoreTabFragment(tabName);
        tab.setArguments(args);

        tabs.add(tab);

        ActionBar actionBar = getSupportActionBar();
        actionBar.addTab(actionBar.newTab().setText(tabName).setTabListener(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.show();

        tabs = new ArrayList<>();

        addTab(R.layout.fragment_highscore_local , "Local");
        addTab(R.layout.fragment_highscore_global, "Global");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.pager, tabs.get(tab.getPosition()))
                                   .commit();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

}
