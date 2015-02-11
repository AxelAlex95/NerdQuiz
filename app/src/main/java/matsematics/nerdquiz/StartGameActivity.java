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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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


}
