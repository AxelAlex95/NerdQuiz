package matsematics.nerdquiz;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class HighscoreActivity extends ActionBarActivity implements ActionBar.TabListener {

  private class HighscoreEntry {
    private String name;
    private int score;

    public HighscoreEntry(String name, int score) {
      this.name  = name;
      this.score = score;
    }

    public String getName() {
      return name;
    }

    public int getScore() {
      return score;
    }

    @Override
    public String toString() {
      return name + ";" + score;
    }
  }

  private class HighscoreEntryComparator implements Comparator<HighscoreEntry> {
    @Override
    public int compare(HighscoreEntry lhs, HighscoreEntry rhs) {
      if (lhs.getScore() == rhs.getScore()) {
        return lhs.getName().compareTo(rhs.getName());
      } else if (lhs.getScore() > rhs.getScore()) {
        return -1; // lhs before rhs
      } else {
        // lhs.getScore() < rhs.getScore()
        return 1;
      }
    }
  }

  /**
   * The {@link android.support.v4.view.PagerAdapter} that will provide
   * fragments for each of the sections. We use a
   * {@link FragmentPagerAdapter} derivative, which will keep every
   * loaded fragment in memory. If this becomes too memory intensive, it
   * may be best to switch to a
   * {@link android.support.v4.app.FragmentStatePagerAdapter}.
   */
  SectionsPagerAdapter mSectionsPagerAdapter;

  /**
   * The {@link ViewPager} that will host the section contents.
   */
  ViewPager mViewPager;

  private static final String LOCAL_HIGHSCORES_FILENAME = "NerdQuiz_LocalHighscores";
  private static final String TAB_TITLE_LOCAL  = "Local";
  private static final String TAB_TITLE_GLOBAL = "Global";
  private static final int HIGHSCORE_LIMIT = 50;

  private ArrayList<HighscoreEntry> highscores;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_highscore);

    // Set up the action bar.
    final ActionBar actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    // When swiping between different sections, select the corresponding
    // tab. We can also use ActionBar.Tab#select() to do this if we have
    // a reference to the Tab.
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
      }
    });

    highscores = new ArrayList<>();

    ActionBar.Tab tabLocal = actionBar.newTab().setText(TAB_TITLE_LOCAL).setTabListener(this);
    actionBar.addTab(tabLocal);

    ActionBar.Tab tabGlobal = actionBar.newTab().setText(TAB_TITLE_GLOBAL).setTabListener(this);
    actionBar.addTab(tabGlobal);

    ListView lv = (ListView) findViewById(R.id.highscore_list);
    lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highscores));
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_highscore, menu);
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

  private HighscoreEntry parseHighscoreEntry(String line) {
    String splits[] = line.split(";");
    return new HighscoreEntry(splits[0], Integer.parseInt(splits[0]));
  }

  private void readLocalHighscoresFromLocal() {
    FileInputStream is;

    try {
      is = openFileInput(LOCAL_HIGHSCORES_FILENAME);
      String line;
      while ((line = FileUtils.readString(is)) != null) { // EOF
        highscores.add(parseHighscoreEntry(line));
      }
      is.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void readGlobalHighscoresFromDB() {
    // TODO get Highscores from DB

    // Dummy implementation
    highscores.add(new HighscoreEntry("Place 1", 100));
    highscores.add(new HighscoreEntry("Place 6",  50));
    highscores.add(new HighscoreEntry("Place 7",  40));
    highscores.add(new HighscoreEntry("Place 8",  30));
    highscores.add(new HighscoreEntry("Place 9",  20));
    highscores.add(new HighscoreEntry(   "Alex",  70));
    highscores.add(new HighscoreEntry(   "Axel",  80));
    highscores.add(new HighscoreEntry( "Cedric",  60));
    highscores.add(new HighscoreEntry(    "Lia",  90));
    highscores.add(new HighscoreEntry(  "Robin",  10));
  }

  private void onTabContentChange(String tabContent) {
    highscores.clear();

    if (tabContent.equals(TAB_TITLE_LOCAL)) {
      readLocalHighscoresFromLocal();
    } else if (tabContent.equals(TAB_TITLE_GLOBAL)) {
      readGlobalHighscoresFromDB();
    } else {
      throw new RuntimeException("Unknown Error");
    }

    if (highscores.size() >= HIGHSCORE_LIMIT)
      throw new RuntimeException("Highscore Limit exceeded");

    Collections.sort(highscores, new HighscoreEntryComparator());
  }

  @Override
  public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    // When the given tab is selected, switch to the corresponding page in
    // the ViewPager.
    mViewPager.setCurrentItem(tab.getPosition());
    onTabContentChange(tab.getText().toString());
  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
  }

  @Override
  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
   * one of the sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      // getItem is called to instantiate the fragment for the given page.
      // Return a PlaceholderFragment (defined as a static inner class below).
      return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
      // Show 2 total pages.
      return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      Locale l = Locale.getDefault();
      switch (position) {
        case 0:
          return getString(R.string.local).toUpperCase(l);
        case 1:
          return getString(R.string.global).toUpperCase(l);
      }
      return null;
    }
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_highscore, container, false);
    }
  }

}
