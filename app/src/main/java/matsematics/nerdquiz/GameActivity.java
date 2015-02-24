package matsematics.nerdquiz;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class GameActivity extends FullscreenLayoutActivity implements HighscoreDialog.inputResult {
    private static final String                 TAG = "GameActivity";
    private int                                 life;
    private static int                          highscore;
    private ArrayList<ToggleButton>             tButtons;
    private ArrayList<ImageView>                lives;

    // Questions from Textfile
    private HashMap<String,HashMap<String, Boolean>>    questionList;
    private ArrayList<String>                           questions;
    private String                                      currentQuestion;

    // ASyncTasks are saved so they can be canceled in onDestroy()
    private AsyncTask                           DQTask;
    private AsyncTask                           BQTask;

    private boolean                             DQActive;
    private boolean                             BQActive;

    // Saving the Thread reference to let it sleep while BetweenQuestions is running
    private Thread                              GUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.GUI = Thread.currentThread();
        this.BQTask = null;
        this.DQTask = null;

        this.highscore = 0;
        this.life = 7;

        initAnswerButtons();
        initLife();

        readQuestions();
        startAsyncMain();
    }

    /**
     * Initializes the life bar
     */
    private void initLife() {
        Logger.i(TAG, "initLife");
        this.lives = new ArrayList<ImageView>();

        lives.add((ImageView) findViewById(R.id.game_lifeBar_heart1));
        lives.add((ImageView) findViewById(R.id.game_lifeBar_heart2));
        lives.add((ImageView) findViewById(R.id.game_lifeBar_heart3));
        lives.add((ImageView) findViewById(R.id.game_lifeBar_heart4));
        lives.add((ImageView) findViewById(R.id.game_lifeBar_heart5));
        lives.add((ImageView) findViewById(R.id.game_lifeBar_heart6));
        lives.add((ImageView) findViewById(R.id.game_lifeBar_heart7));
    }

    /*+
    Initializes the AnswerButtons
     */
    private void initAnswerButtons() {
        Logger.i(TAG, "initAnswerbuttons");
        this.tButtons = new ArrayList<ToggleButton>();

        this.tButtons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer1));
        this.tButtons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer2));
        this.tButtons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer3));
        this.tButtons.add((ToggleButton) findViewById(R.id.game_toggleButton_answer4));
    }

    /**
     * Provides the functionality for the answer buttons to change the color depending on their checked state
     *
     * @param view  Button that was clicked
     */
    public void buttonToggle(View view) {
        Logger.i(TAG, "buttonToggle");
        if (BQTask == null || !BQTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            ToggleButton temp = (ToggleButton) view;
            if (temp.isChecked()) {
                temp.setBackgroundResource(R.drawable.yellow);
            }
            else {
                temp.setBackgroundResource(R.drawable.blue);
            }
        }
    }

    /**
     * Overrides the onDestroy() Method to cancel all still active Tasks so it will not continue
     * to run in the Background
     */
    @Override
    public void onDestroy() {
        Logger.d(TAG, "onDestroy");
        BQActive = DQActive = false;

        if (BQTask != null && BQTask.getStatus().equals(AsyncTask.Status.RUNNING))
            BQTask.cancel(true);
        if (DQTask != null && DQTask.getStatus().equals(AsyncTask.Status.RUNNING))
            DQTask.cancel(true);

        super.onDestroy();
    }


    /**
     * Skips the remaining timer and starts the BetweenQuestions Task
     *
     * @param view  Confirm button
     */
    public void onConfirm(View view) {
        Logger.i(TAG, "onConfirm");

        DQActive = false;
    }

    /**
     * Resets the Background Color of all Buttons back to default
     * Initializes the new Question and its answers for the next round
     */
    private void nextQuestion() {
        Logger.i(TAG, "nextQuestion");

        // Getting the Question and writing it into the designated TextView
        TextView tv = (TextView) findViewById(R.id.game_textView_question);

        Random r = new Random();
        Integer rnd = r.nextInt(questions.size());

        currentQuestion = questions.get(rnd);
        tv.setText(currentQuestion);

        questions.remove(currentQuestion);

        // Resetting the AnswerButtons to their default state
        for(ToggleButton tb : tButtons){
            tb.setBackgroundResource(R.drawable.blue);
            tb.setChecked(false);
        }

        setAnswersRandom(questionList.get(currentQuestion));
    }

    /**
     * sets answers random to the buttons and adds information to the global answer array
     *
     * @param answerMap     Hashmap of answers and true/false values
     */
    private void setAnswersRandom(HashMap<String, Boolean> answerMap) {
        Logger.i(TAG, "setAnswersRandom");
        ArrayList<ToggleButton> tempButtons = (ArrayList<ToggleButton>) tButtons.clone();

        Random r = new Random();
        Set<String> keys = answerMap.keySet();
        int count = 4;

        for (String key : keys) {
            int number = r.nextInt(count);

            // Setting the text for each state of a ToggleButton
            tempButtons.get(number).setText(key);
            tempButtons.get(number).setTextOn(key);
            tempButtons.get(number).setTextOff(key);

            tempButtons.remove(number);
            --count;
        }
    }

    /**
     * Method to call if Player looses lives
     *
     * @param   number of lives Player lost
     * @return  0 if Player has lost his last life
     *          1 else
     */
    private void looseLife(int number){
        Logger.i(TAG, "looseLife");

        int count = 0;
        for (int i = 6; i >= 0; --i) {
            if (count == number)
                break;
            if (lives.get(i).getVisibility() == View.VISIBLE) {
                lives.get(i).setVisibility(View.INVISIBLE);
                --life;
                ++count;
            }
        }
    }

    /**
     * Indicates if the user has lives left over in the current round
     *
     * @return boolean  User has lives left over
     */
    private boolean hasLives() {
        Logger.i(TAG, "hasLives");
        return this.life > 0;
    }

    private final String file = "already_answered_Questions";

    private void saveData(int id) {
        Logger.i(TAG, "saveData");
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(file, Context.MODE_PRIVATE);
            FileUtils.writeString(outputStream,id+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the Questions and Answers from the Fragen.txt
     * which is included in the applications assets folder
     */
    private void readQuestions() {
        Logger.i(TAG, "readQuestions");
        questionList = new HashMap<String, HashMap<String, Boolean>>();
        questions = new ArrayList<String>();
        AssetManager assetManager = getAssets();
        String s;
        Scanner sc;
        InputStream input;

        try {
            input = assetManager.open("Fragen.txt");
            String answer;

            sc = new Scanner(input, "UTF-8");

            while (sc.hasNextLine()) {
                s = sc.nextLine();
                if (s.length() > 0 && s.charAt(0) == ('#')) {
                    s = s.substring(1).trim();
                    questions.add(s);
                    questionList.put(s, new HashMap<String, Boolean>());
                    for (int i = 0; i < 4; ++i) {
                        answer = sc.nextLine();
                        Boolean isCorrect = (answer.charAt(0) == '1');
                        questionList.get(s).put(answer.substring(1).trim(), isCorrect);
                    }
                }
            }
        } catch (IOException e) {
            Logger.i(TAG, "readQuestions", e);
            e.printStackTrace();
        }
    }

    /**
     * checks if categoryID is File
     * @param id
     * @return
     */
    private boolean containsData(int id) {
        Logger.i(TAG, "containsData");
        FileInputStream inputStream;
        String s;
        try {
            inputStream = openFileInput(file);
            s = new Scanner(inputStream, "UTF-8").next();
            inputStream.close();
            String[] ids = s.split(",");
            for(String category : ids) {
                if(category.equals(id + ""))
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /************************************************************************************
     *  Dialog Related Methods                                                      *
     ***********************************************************************************/

    public void doDialogAlert() {
        Logger.i(TAG, "doDialogAlert");

        HighscoreDialog newFragment = new HighscoreDialog();
        newFragment.setCancelable(false);
        newFragment.show(getFragmentManager(), "highscoreDialog");
    }

    public static int getHighscore() {
        return highscore;
    }

    @Override
    public void Submit() {
        Logger.i(TAG, "Submit");
        onBackPressed();
    }

    @Override
    public void Cancel() {
        Logger.i(TAG, "Cancel");
        onBackPressed();
    }

    /************************************************************************************
     *  AsyncTask start Methods                                                      *
     ***********************************************************************************/

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

                    if (DQActive) {
                        publishProgress(i);
                        Thread.sleep(1000);
                    } else {
                        publishProgress(0);
                    }
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
            DQActive = true;

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
            DQActive = false;
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

        /**
         * Does nothing for a set time frame
         */
        protected Void doInBackground(Void... arg0) {
            Logger.i(TAG, "doInBackground");
            try {
                onProgressUpdate(1);
                Thread.sleep(2000);
                GUI.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Method is called before the new Thread is initialized
         * The Button colors are changed based on the user input
         */
        protected void onPreExecute() {
            Logger.i(TAG, "onPreExecute");
            BQActive = true;

            int wrongAnswers = 0;

            for (int i = 0; i < tButtons.size(); ++i) {
                if (tButtons.get(i).isChecked() != questionList.get(currentQuestion).get(tButtons.get(i).getTextOn())) {
                    wrongAnswers++;
                    tButtons.get(i).setBackgroundResource(R.drawable.red);
                } else {
                    tButtons.get(i).setBackgroundResource(R.drawable.dark_green);
                }
            }

            highscore += (4 - wrongAnswers);
            looseLife(wrongAnswers);
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
         * Next AsyncMain() will be initiated if lives are remaining
         */
        protected void onPostExecute(Void arg0) {
            Logger.i(TAG, "onPostExecute");

            if (!hasLives()) {
                BQActive = DQActive = false;
                doDialogAlert();
            } else if (!(questions.size() > 0)) {
                BQActive = DQActive = false;
                doDialogAlert();
            } else {
                BQActive = false;
                startAsyncMain();
            };
        }
    }
}