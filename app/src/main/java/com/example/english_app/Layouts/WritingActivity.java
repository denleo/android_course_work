package com.example.english_app.Layouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.english_app.Entities.Database.Word;
import com.example.english_app.Fragments.RefineDialog;
import com.example.english_app.R;
import com.example.english_app.Utils.DatabaseUtils.DBAdapter;

import java.util.List;

public class WritingActivity extends AppCompatActivity implements RefineDialog.RefineDialogInterface {

    List<Word> wordList; // list of words from database
    int currentPosition, wordsCount, numberOfCorrectAnswers;
    boolean needExit;

    EditText englishInput;
    TextView russianWord, progressBarText;
    Button nextBtn;
    ProgressBar progressBar;
    MediaPlayer mediaPlayer;

    private final View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String userInput = englishInput.getText().toString();
            String correctInput = wordList.get(currentPosition - 1).getEnTranslate();
            if (userInput.isEmpty()) { // exit if user input is empty
                Toast toast = Toast.makeText(WritingActivity.this, "Please enter the word!", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if (userInput.toLowerCase().equals(correctInput.toLowerCase())) { // if answer is correct
                setCorrectAnswerBg();
                mediaPlayer = MediaPlayer.create(WritingActivity.this, R.raw.correct_answer);
                mediaPlayer.start();
                numberOfCorrectAnswers++;

                if (needExit) {
                    exitProcess();
                    return;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setDataOnView(++currentPosition); // увеличиваем позицию на 1 и обновляем новыми данными
                        checkForExit();
                    }
                }, 1000);
            } else { // if answer is incorrect
                mediaPlayer = MediaPlayer.create(WritingActivity.this, R.raw.wrong_answer);
                mediaPlayer.start();
                RefineDialog refineDialog = new RefineDialog(correctInput, userInput);
                refineDialog.show(getSupportFragmentManager(), WritingActivity.this.getClass().getName());
            }

            nextBtn.setClickable(false); // для исключения повторных нажатий после ответа
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        getSupportActionBar().hide(); //hide the title bar
        englishInput = findViewById(R.id.et_writing);
        russianWord = findViewById(R.id.tv_writing_targetWord);
        progressBarText = findViewById(R.id.tv_writing_progress);
        progressBar = findViewById(R.id.writing_progressBar);
        nextBtn = findViewById(R.id.button_next);
        nextBtn.setOnClickListener(nextButtonListener);

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        try {
            wordList = dbAdapter.getAllWords();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Error with getting words from database", e);
        }
        dbAdapter.close();

        wordsCount = wordList.size();

        if (wordsCount == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "Too few words to start the test!", Toast.LENGTH_LONG); // для запуска теста необходимо хотя бы одно слово
            toast.show();
            finish();
            return;
        }

        progressBar.setMax(wordsCount);
        currentPosition = 1;
        numberOfCorrectAnswers = 0;
        checkForExit();
        setDataOnView(currentPosition);
    }


    private void setDataOnView(int currentPosition) { // подгружает данные на визуальные компоненты
        russianWord.setText(wordList.get(currentPosition - 1).getRuTranslate());
        String progressText = currentPosition + "/" + wordList.size();
        progressBarText.setText(progressText);
        progressBar.setProgress(currentPosition);
        englishInput.setText("");
        nextBtn.setClickable(true);
        setDefaultAnswerBg();
    }

    private void setCorrectAnswerBg() { // устанавливает фон правильного ответа
        englishInput.setBackground(ContextCompat.getDrawable(this, R.drawable.correct_option_border_bg));
        englishInput.setClickable(false);
    }

    private void setDefaultAnswerBg() { // устанавливает дефолтный фон ответа
        englishInput.setBackground(ContextCompat.getDrawable(this, R.drawable.default_option_border_bg));
        englishInput.setClickable(true);
    }

    private void checkForExit() { // if next task is the last
        if (currentPosition == wordsCount) {
            nextBtn.setText("Finish");
            needExit = true;
        }
    }

    private void exitProcess() { // execute when activity close
        Intent scoreIntent = new Intent(this, ScoreResultActivity.class);
        scoreIntent.putExtra("QuestionsCount", wordList.size());
        scoreIntent.putExtra("Score", numberOfCorrectAnswers);
        startActivity(scoreIntent); // запускаем итоговое activity (ScoreResultActivity)
        this.finish(); // завершаем текущее активити
    }

    @Override
    public void processAnswer(boolean userAnswer) { // обработка ответа от RefineDialog
        if (userAnswer) {
            numberOfCorrectAnswers++;
        }

        if (needExit) {
            exitProcess();
            return;
        }

        setDataOnView(++currentPosition);
        checkForExit();
    }
}