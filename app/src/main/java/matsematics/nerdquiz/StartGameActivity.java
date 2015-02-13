package matsematics.nerdquiz;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import Logging.Logger;

public class StartGameActivity extends FullscreenLayoutActivity{
    int life;
    boolean[] answer;
    int highscore;
    private static final String TAG = "StartGameActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.setLogging(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        highscore=0;
        life=7;
        answer = new boolean[4];
        startAsync();
    }

    private class DoSomething extends AsyncTask<Void, Integer, Void> {
        private Time time = new Time();

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
            Logger.i(TAG, "onPreExecute");
            TextView countdown = (TextView)findViewById(R.id.game_textView_countdown);
            countdown.setText("10");
        }

        protected void onProgressUpdate(Integer... progress) {
            TextView countdown = (TextView)findViewById(R.id.game_textView_countdown);
            countdown.setText(progress[0]+"");
        }


        protected void onPostExecute(Void arg0) {
            time.setToNow();
            Logger.i(TAG, "onPostExecute");
            ArrayList<ToggleButton> tbuttons = new ArrayList<ToggleButton>();
            tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer1));
            tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer2));
            tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer3));
            tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer4));

            int wrongAnswers = 0;
            for (int i = 0; i < tbuttons.size(); ++i) {
                if (tbuttons.get(i).isChecked()!=answer[i]) {
                    wrongAnswers++;
                    tbuttons.get(i).setBackgroundColor(Color.parseColor("#FFE60000"));
                    Logger.i(TAG, "Background Red");
                } else {
                    tbuttons.get(i).setBackgroundColor(Color.parseColor("#FF66FF66"));
                    Logger.i(TAG, "Background Green");
                }
            }

            try{
                time.setToNow();
                Logger.i(TAG, "THREAD SLEEP " + time.minute + ":" + time.second);
                Thread.currentThread().sleep(1000);
            }
            catch(InterruptedException e){e.getStackTrace();}

            time.setToNow();
            Logger.i(TAG, "THREAD ACTIVE " + time.minute + ":" + time.second);

            int i = looseLife(wrongAnswers);
            if (i == 0) {
              //TODO BEENDEN --> Speicher Highscore in Liste und zeige Dialog "Verloren"
            } else {
                highscore += (4 - wrongAnswers);
                nextQuestion();
            }
            // TODO --> Achtung soll auch beim Click des Buttons aufgerufen werden(Abbruch des Countdowns)
        }
    }

    public void startAsync()
    {
        Logger.i(TAG, "startAsync");
        new DoSomething().execute();
    }

    private void nextQuestion() {
        try{
        Thread.sleep(1000);
    }catch(InterruptedException e)

    {
    }
        Logger.i(TAG, "nextQuestion " + Thread.activeCount());
        TextView tv = (TextView) findViewById(R.id.game_textView_question);
        String question="";//TODO - load Question from selected categorys from db
        HashMap<String,Boolean> answers = new HashMap<>();
        //TODO - save id used Questions in File --> saveData(int id) if(!containsData(id))
        //TODO - load answers from db
        //answers.put();
        //answers.put();
        //answers.put();
        //answers.put();
        tv.setText(question);
        ArrayList<ToggleButton> tbuttons = new ArrayList<ToggleButton>();
        tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer1));
        tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer2));
        tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer3));
        tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer4));

        for(ToggleButton tb:tbuttons){
            tb.setBackgroundColor(Color.parseColor("#78FFFFFF"));
        }
        setAnswersRandom(answers);
        startBGAsync();
    }

    /**
     * sets answers random to the buttons and adds information to the global answer array
     * @param answerMap Hashmap of answers and true/false values
     */
    private void setAnswersRandom(HashMap<String, Boolean> answerMap) {
        Logger.i(TAG, "setAnswersRandom");
        ArrayList<ToggleButton> tbuttons = new ArrayList<ToggleButton>();//
        tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer1));
        tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer2));
        tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer3));
        tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer4));
        Random r = new Random();
        Set<String> keys = answerMap.keySet();
        int count = 4;
        for (String key:keys) {
            int number = r.nextInt(count) + 1;
            tbuttons.get(number).setText(key);
            tbuttons.remove(number);
            answer[number - 1] = answerMap.get(key);
            --count;
        }
    }


    /**
     * Method to call if Player looses lifes
     * @param number of lifes Player lost
     * @return 0 if Player has lost his last life
     *         1 else
     */
    private int looseLife(int number){
        Logger.i(TAG, "looseLife");
        ArrayList<ImageView> lifes = new ArrayList<ImageView>();
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart1));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart2));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart3));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart4));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart5));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart6));
        lifes.add((ImageView) findViewById(R.id.game_lifeBar_heart7));

        int count = 0;
        for (int i = 6; i >= 0; --i) {
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

    private final String file = "already_answered_Questions";

    private void saveData(int id) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(file, Context.MODE_PRIVATE);
            outputStream.write((id+",").getBytes(Charset.forName("UTF-8")));
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean containsData(int id) {
        FileInputStream inputStream;
        String s;
        try {
            inputStream = openFileInput(file);
            s = new Scanner(inputStream, "UTF-8").next();
            inputStream.close();
            if(s.contains(id+"")) return true;
            else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private void deleteFile(){
        //-TODO delete File "file"
    }

    private class BetweenQuestions extends AsyncTask<Void, Integer, Void> {
        private String TAG = "BetweenQuestions";
        private Time time = new Time();

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
            Logger.i(TAG, "onPreExecute");
        }

        protected void onProgressUpdate(Integer... progress) {
        }


        protected void onPostExecute(Void arg0) {
            time.setToNow();
            Logger.i(TAG, "onPostExecute");
            ArrayList<ToggleButton> tbuttons = new ArrayList<ToggleButton>();
            tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer1));
            tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer2));
            tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer3));
            tbuttons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer4));

            int wrongAnswers = 0;
            for (int i = 0; i < tbuttons.size(); ++i) {
                if (tbuttons.get(i).isChecked()!=answer[i]) {
                    wrongAnswers++;
                    tbuttons.get(i).setBackgroundColor(Color.parseColor("#FFE60000"));
                    Logger.i(TAG, "Background Red");
                } else {
                    tbuttons.get(i).setBackgroundColor(Color.parseColor("#FF66FF66"));
                    Logger.i(TAG, "Background Green");
                }
            }

            try{
                time.setToNow();
                Logger.i(TAG, "THREAD SLEEP " + time.minute + ":" + time.second);
                Thread.currentThread().sleep(1000);
            }
            catch(InterruptedException e){e.getStackTrace();}

            time.setToNow();
            Logger.i(TAG, "THREAD ACTIVE " + time.minute + ":" + time.second);

            int i = looseLife(wrongAnswers);
            if (i == 0) {
                //TODO BEENDEN --> Speicher Highscore in Liste und zeige Dialog "Verloren"
            } else {
                highscore += (4 - wrongAnswers);
                nextQuestion();
            }
            // TODO --> Achtung soll auch beim Click des Buttons aufgerufen werden(Abbruch des Countdowns)
        }
    }

    public void startBGAsync()
    {
        Logger.i(TAG, "startAsync");
        new BetweenQuestions().execute();
    }
 }


