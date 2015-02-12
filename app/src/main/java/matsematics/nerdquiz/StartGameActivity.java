package matsematics.nerdquiz;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import Logging.Logger;

public class StartGameActivity extends FullscreenLayoutActivity{
    int life;
    boolean[] answer;
    int highscore;
    private static final String TAG = "StartGameActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        highscore=0;
        life=7;
        answer = new boolean[4];
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
            ArrayList<ToggleButton> tbuttons = new ArrayList<ToggleButton>();//
            tbuttons.add((ToggleButton) findViewById(R.id.answer1));
            tbuttons.add((ToggleButton) findViewById(R.id.answer2));
            tbuttons.add((ToggleButton) findViewById(R.id.answer3));
            tbuttons.add((ToggleButton) findViewById(R.id.answer4));
            int wrongAnsweres=0;
            for(int i=0;i<tbuttons.size();++i){
                if(tbuttons.get(i).isSelected()!=answer[i]){
                    wrongAnsweres++;
                    tbuttons.get(i).setBackgroundColor(Color.parseColor("#E60000"));
                }else{
                    tbuttons.get(i).setBackgroundColor(Color.parseColor("#66FF66"));
                }
            }
            int i = looseLife(wrongAnsweres);
            if(i==0) {//TODO BEENDEN --> Speicher Highscore in Liste und zeige Dialog "Verloren"
            }
            else{
                highscore+=(4-wrongAnsweres);
                nextQuestion();
            }
            // TODO --> Achtung soll auch beim Click des Buttons aufgerufen werden(Abbruch des Countdowns)
        }
    }

    public void startAsync()
    {
        new DoSomething().execute();
    }

    private void nextQuestion(){
        TextView tv = (TextView) findViewById(R.id.question);
        String question="";//TODO - load Question from selected categorys from db
        HashMap<String,Boolean> answers = new HashMap<String,Boolean>();
        //TODO - save id used Questions in List
        //TODO - load answers from db
        //answers.put();
        //answers.put();
        //answers.put();
        //answers.put();
        tv.setText(question);
        setAnswersRandom(answers);
        startAsync();
    }

    /**
     * sets answers random to the buttons and adds information to the global answer array
     * @param answerMap Hashmap of answers and true/false values
     */
    private void setAnswersRandom(HashMap<String, Boolean> answerMap) {
        ArrayList<ToggleButton> tbuttons = new ArrayList<ToggleButton>();//
        tbuttons.add((ToggleButton) findViewById(R.id.answer1));
        tbuttons.add((ToggleButton) findViewById(R.id.answer2));
        tbuttons.add((ToggleButton) findViewById(R.id.answer3));
        tbuttons.add((ToggleButton) findViewById(R.id.answer4));
        Random r = new Random();
        Set<String> keys = answerMap.keySet();
        int count = 4;
        for(String key:keys){
            int number = r.nextInt(count)+1;
            tbuttons.get(number).setText(key);
            tbuttons.remove(number);
            answer[number-1]=answerMap.get(key);
            count--;
        }
    }


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
