package matsematics.nerdquiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

/**
 * The Game class, it handles the progress of a single Game
 * Reads and writes Questions into the GameScreen
 * Processes the user input etc.
 */
public class StartGameActivity extends FullscreenLayoutActivity{
    private static final String     TAG = "StartGameActivity";
    int                             life;
    boolean[]                       answer;
    int                             highscore;
    ArrayList<ToggleButton>         tButtons;

    // ASyncTasks are saved so they can be canceled in onDestroy()
    AsyncTask                       DQTask;
    AsyncTask                       BQTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.startLogging();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.highscore = 0;
        this.life = 7;
        this.answer = new boolean[4];

        this.tButtons = new ArrayList<ToggleButton>();
        this.tButtons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer1));
        this.tButtons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer2));
        this.tButtons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer3));
        this.tButtons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer4));

        startAsyncMain();
    }

    /**
     * Overrides the onDestroy() Method to cancel all still active Tasks, so it wont continue
     * running in the Background
     */
    @Override
    public void onDestroy() {
        if (BQTask.getStatus().equals(AsyncTask.Status.RUNNING))
            BQTask.cancel(true);
        if (DQTask.getStatus().equals(AsyncTask.Status.RUNNING))
            DQTask.cancel(true);
        super.onDestroy();
    }

    /**
     * Resets the Background Color of all Buttons back to default
     * Initializes the new Question and its answers for the next round
     */
    private void nextQuestion() {
        Logger.i(TAG, "nextQuestion ");
        TextView tv = (TextView) findViewById(R.id.game_textView_question);
        String question="Test";//TODO - load Question from selected categorys from db
        HashMap<String,Boolean> answers = new HashMap<>();
        //TODO - save id used Questions in File --> saveData(int id) if(!containsData(id))
        //TODO - load answers from db
        //answers.put();
        //answers.put();
        //answers.put();
        //answers.put();
        tv.setText(question);

        for(ToggleButton tb : tButtons){
            tb.setBackgroundColor(Color.parseColor("#78FFFFFF"));
            tb.setChecked(false);
        }

        setAnswersRandom(answers);
    }

    /**
     * sets answers random to the buttons and adds information to the global answer array
     * @param answerMap Hashmap of answers and true/false values
     */
    private void setAnswersRandom(HashMap<String, Boolean> answerMap) {
        Logger.i(TAG, "setAnswersRandom");
        ArrayList<ToggleButton> tempButtons = (ArrayList<ToggleButton>) tButtons.clone();//

        Random r = new Random();
        Set<String> keys = answerMap.keySet();
        int count = 4;
        for (String key:keys) {
            int number = r.nextInt(count) + 1;
            tempButtons.get(number).setText(key);
            tempButtons.remove(number);
            answer[number - 1] = answerMap.get(key);
            --count;
        }
    }

    /**
     * Method to call if Player looses lifes
     * @param   number of lifes Player lost
     * @return  0 if Player has lost his last life
     *          1 else
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
            FileUtils.writeString(outputStream,id+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * checks if categoryID is File
     * @param id
     * @return
     */
    private boolean containsData(int id) {
        FileInputStream inputStream;
        String s;
        try {
            inputStream = openFileInput(file);
            s = new Scanner(inputStream, "UTF-8").next();
            inputStream.close();
            String[]ids = s.split(",");
            for(String category:ids){
                if(category.equals(id+""))return true;
            }return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Method to create a toast out of the AsyncTasks which cannot operate the GUI
     *
     * @param msg   Message that shall be Toasted
     */
    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Starts the AsyncTask that runs while a Question is being answered by the user
     */
    public void startAsyncMain()
    {
        Logger.i(TAG, "startAsyncMain");
        DQTask = new DuringQuestions().execute();
    }

    /**
     * Starts the AsyncTask that processes the result of the user input for a question
     */
    public void startAsyncBackground()
    {
        Logger.i(TAG, "startAsyncBackground");
        BQTask = new BetweenQuestions().execute();
    }

    /************************************************************************************
     *  AsyncTask DuringQuestions                                                       *
     ***********************************************************************************/
    /**
     * AsyncTask for the Duration of a Question
     * Counts down a timer in a background Thread
     */
    private class DuringQuestions extends AsyncTask<Void, Integer, Void> {
        private static final String TAG     = "DuringQuestions";
        private TextView            countdown;

        /**
         * Background Thread that counts down the Timer every 1 Second
         */
        protected Void doInBackground(Void... arg0) {
            Logger.i(TAG, "doInBackground");
            for (int i = 10; i >= 0; --i) {
                try {
                    publishProgress(i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * This method is executed before the Background Thread is started
         */
        protected void onPreExecute() {
            Logger.i(TAG, "onPreExecute");
            this.countdown = (TextView) findViewById(R.id.game_textView_countdown);
            this.countdown.setText("10");

            nextQuestion();
        }

        /**
         * Method that is called from the background Thread and updates the Counter Element
         *
         * @param progress  Time remaining to answer the current question
         */
        protected void onProgressUpdate(Integer... progress) {
            Logger.i(TAG, "onProgressUpdate");
            this.countdown.setText(progress[0] + "");
        }

        /**
         * Method that is started after the background Thread has been initialized
         */
        protected void onPostExecute(Void arg0) {
            Logger.i(TAG, "onPostExecute");
            startAsyncBackground();
        }
    }

    /************************************************************************************
     *  AsyncTask BetweenQuestions                                                      *
     ***********************************************************************************/
    /**
     * AsyncTask that processes the given answers
     * Changes the color of the buttons based on the user input
     */
    private class BetweenQuestions extends AsyncTask<Void, Integer, Void> {
        private static final String TAG     = "BetweenQuestions";
        private int                 wrongAnswers;
        private int                 hasLives;

        /**
         * Does nothing for a set time frame
         */
        protected Void doInBackground(Void... arg0) {
            Logger.i(TAG, "doInBackground");
            //for (int i = 30; i >= 0; --i) {
            try {
                onProgressUpdate(1);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //}

            return null;
        }

        /**
         * Method is called before the new Thread is initialized
         * The Button colors are changed based on the user input
         */
        protected void onPreExecute() {
            Logger.i(TAG, "onPreExecute");

            this.wrongAnswers = 0;

            for (int i = 0; i < tButtons.size(); ++i) {
                if (tButtons.get(i).isChecked() != answer[i]) {
                    wrongAnswers++;
                    tButtons.get(i).setBackgroundColor(Color.parseColor("#FFE60000"));
                } else {
                    tButtons.get(i).setBackgroundColor(Color.parseColor("#FF66FF66"));
                }
            }

            this.hasLives = looseLife(this.wrongAnswers);
        }

        /**
         * Does nothing in this context
         *
         * @param progress
         */
        protected void onProgressUpdate(Integer... progress) {
            Logger.i(TAG, "onProgressUpdate ");
        }

        /**
         * Button Colors are reset to Default
         * Next AsyncMain() will be initiated if lifes are remaining
         */
        protected void onPostExecute(Void arg0) {
            Logger.i(TAG, "onPostExecute");

            if (hasLives == 0) {
                // TODO BEENDEN --> Speicher Highscore in Liste und zeige Dialog "Verloren"
                toast("Game Over");
            } else {
                highscore += (4 - this.wrongAnswers);
                toast("Next Question");
                startAsyncMain();
            }
            // TODO --> Achtung soll auch beim Click des Buttons aufgerufen werden(Abbruch des Countdowns)
        }
    }
 }


