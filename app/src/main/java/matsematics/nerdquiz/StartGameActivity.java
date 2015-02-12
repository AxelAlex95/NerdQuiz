package matsematics.nerdquiz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StartGameActivity extends FullscreenLayoutActivity{
    int life;
    private static final String TAG = "StartGameActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        life=7;
        startAsync();
    }

    private class DoSomething extends AsyncTask<Void, Integer, Void> {
        protected Void doInBackground(Void... arg0) {
            for (int i=10; i>=0; --i) {
                try {
                    publishProgress(i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        protected void onPreExecute() {
            TextView countdown = (TextView)findViewById(R.id.game_textView_countdown);
            countdown.setText("10");
        }

        protected void onProgressUpdate(Integer... progress) {
            TextView countdown = (TextView)findViewById(R.id.game_textView_countdown);
            countdown.setText(progress[0]+"");
        }

        protected void onPostExecute(Void arg0){
            //TODO - Verarbeitung der Antworten und Leben abziehen
            // --> Achtung soll auch beim Click des Buttons aufgerufen werden(Abbruch des Countdowns)
            //TODO - Call next Question if not dead 
        }
    }

    public void startAsync()
    {
        new DoSomething().execute();
    }

    private void nextQuestion(){/** TODO -  load Question from Database
                                    TODO -  startCountdown**/}


    /**
     * Method to call if Player looses lifes
     * @param number of lifes Player lost
     * @return 0 if Player has lost his last life
     *         1 else
     */
    private int looseLife(int number){
        ArrayList<ImageView> lifes = new ArrayList<ImageView>();
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart1));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart2));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart3));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart4));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart5));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart6));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart7));

        int count = 0;
        for (int i = 7; i > 0; --i) {
            if (count == number)
              break;

            if (lifes.get(i).getVisibility() == View.VISIBLE) {
                lifes.get(i).setVisibility(View.INVISIBLE);
                ++count;
            }
        }
        if (count != number)
          return 0;
        else
          return 1;
    }


}
