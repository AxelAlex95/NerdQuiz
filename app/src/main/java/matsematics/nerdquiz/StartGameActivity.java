package matsematics.nerdquiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Logging.Logger;

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
            TextView countdown = (TextView)findViewById(R.id.Countdown);
            countdown.setText("10");
        }

        protected void onProgressUpdate(Integer... progress) {
            TextView countdown = (TextView)findViewById(R.id.Countdown);
            countdown.setText(progress[0]+"");
        }

        protected void onPostExecute(Void arg0){
            // Verarbeitung der Antworten
        }
    }

    public void startAsync()
    {
        new DoSomething().execute();
    }

    public void startQuiz(){}

    /**
     * Method to call if Player looses lifes
     * @param number of lifes Player lost
     * @return 0 if Player has lost his last life
     *         1 else
     */
    private int looseLife(int number){
        ArrayList<ImageView> lifes = new ArrayList<ImageView>();
        lifes.add((ImageView) findViewById(R.id.life1));
        lifes.add((ImageView) findViewById(R.id.life2));
        lifes.add((ImageView) findViewById(R.id.life3));
        lifes.add((ImageView) findViewById(R.id.life4));
        lifes.add((ImageView) findViewById(R.id.life5));
        lifes.add((ImageView) findViewById(R.id.life6));
        lifes.add((ImageView) findViewById(R.id.life7));
        int count=0;
        for(int i=7;i>0;--i){
            if(count==number)break;
            if(lifes.get(i).getVisibility()==View.VISIBLE){
                lifes.get(i).setVisibility(View.INVISIBLE);
                count++;
            }
        }
        if(count!=number)return 0;
        else return 1;
    }


}
