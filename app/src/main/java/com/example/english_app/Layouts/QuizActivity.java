package com.example.english_app.Layouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.english_app.Entities.Tests.Question;
import com.example.english_app.R;
import com.example.english_app.Utils.TestUtils.QuestionGenerator;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Question> questionList; // список вопросов (Question model)
    private int currentQuestion; // номер текущего вопроса
    private int selectedOption; // номер выбранного ответа
    private int result_score; // кол-во правильных ответов
    private boolean nextQuestion;

    private MediaPlayer mediaPlayer;
    private TextView englishWord, option1, option2, option3, option4, progressText;
    private ProgressBar progressBar;
    private Button confirmButton;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().hide();

        questionList = QuestionGenerator.generateQuestions(this);
        if (questionList == null){
            Toast toast = Toast.makeText(getApplicationContext(), "Too few words to start the test!", Toast.LENGTH_LONG); // если количество слов < 2 нельзя сгенерировать неверные опции
            toast.show();
            finish();
            return;
        }

        englishWord = findViewById(R.id.tv_targetWord);
        option1 = findViewById(R.id.tv_option_one);
        option2 = findViewById(R.id.tv_option_two);
        option3 = findViewById(R.id.tv_option_three);
        option4 = findViewById(R.id.tv_option_four);
        progressText = findViewById(R.id.tv_quiz_progress);
        progressBar = findViewById(R.id.test_progressBar);
        confirmButton = findViewById(R.id.btn_confirm);
        progressBar.setMax(questionList.size());

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
        confirmButton.setOnClickListener(this);

        currentQuestion = 1;
        nextQuestion = false;
        result_score = 0;
        setQuestionToTheViews(currentQuestion);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_option_one:
                setSelectedOptionBg(option1, 1);
                break;
            case R.id.tv_option_two:
                setSelectedOptionBg(option2, 2);
                break;
            case R.id.tv_option_three:
                setSelectedOptionBg(option3, 3);
                break;
            case R.id.tv_option_four:
                setSelectedOptionBg(option4, 4);
                break;

            case R.id.btn_confirm:

                if (currentQuestion > questionList.size()) {
                    Intent scoreIntent = new Intent(this, ScoreResultActivity.class);
                    scoreIntent.putExtra("QuestionsCount", questionList.size());
                    scoreIntent.putExtra("Score", result_score);
                    startActivity(scoreIntent); // запускаем итоговое activity (ScoreResultActivity)
                    this.finish(); // завершаем текущее активити
                    return;
                }
                if (nextQuestion){ // переход к следующему вопросу
                    setQuestionToTheViews(currentQuestion);
                    nextQuestion = false;
                    return;
                }

                if (selectedOption != 0){ // пользователь выбрал ответ

                    if (questionList.get(currentQuestion - 1).getCorrectOption() == selectedOption){ // правильный ответ
                        setAnswerView(selectedOption, R.drawable.correct_option_border_bg);
                        mediaPlayer = MediaPlayer.create(this, R.raw.correct_answer);
                        mediaPlayer.start();
                        result_score++;
                    }
                    else // неверный ответ
                    {
                        setAnswerView(selectedOption, R.drawable.incorrect_option_border_bg);
                        setAnswerView(questionList.get(currentQuestion - 1).getCorrectOption(), R.drawable.correct_option_border_bg);
                        mediaPlayer = MediaPlayer.create(this, R.raw.wrong_answer);
                        mediaPlayer.start();
                    }

                    setClickableOptions(false);
                    if (currentQuestion == questionList.size()){ // текущий вопрос был последним
                        confirmButton.setText("Finish");
                    }
                    else{
                        confirmButton.setText("Next");
                    }
                    currentQuestion++;
                    nextQuestion = true;
                }
        }
    }

    private void setQuestionToTheViews(int questionNumber){ // set question model data to views

        Question question = questionList.get(questionNumber - 1);
        progressBar.setProgress(questionNumber);
        progressText.setText(questionNumber + "/" + questionList.size());

        englishWord.setText(question.getEnglishWord());

        option1.setText(question.getOption1());
        option2.setText(question.getOption2());
        option3.setText(question.getOption3());
        option4.setText(question.getOption4());
        setClickableOptions(true);

        confirmButton.setText("Confirm");

        setDefaultOptionsBg();
    }

    private void setDefaultOptionsBg(){ // set default look to all TextViews
        ArrayList<TextView> options = new ArrayList<TextView>();
        options.add(0, option1);
        options.add(1, option2);
        options.add(2, option3);
        options.add(3, option4);
        for (TextView option: options) {
            option.setTextColor(ContextCompat.getColor(this, R.color.colorGrayDark));
            option.setBackground(ContextCompat.getDrawable(this, R.drawable.default_option_border_bg));
        }

        this.selectedOption = 0;
    }

    private void setSelectedOptionBg(TextView tv, int selectedOption){ // set selected look to TextView and number of selected option
        setDefaultOptionsBg();
        tv.setTextColor(ContextCompat.getColor(this, R.color.black));
        tv.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg));
        this.selectedOption = selectedOption;
    }


    private void setAnswerView(int answer, int background){
        switch (answer){
            case 1:
                option1.setBackground(ContextCompat.getDrawable(this, background));
                break;
            case 2:
                option2.setBackground(ContextCompat.getDrawable(this, background));
                break;
            case 3:
                option3.setBackground(ContextCompat.getDrawable(this, background));
                break;
            case 4:
                option4.setBackground(ContextCompat.getDrawable(this, background));
                break;
        }
    }

    private void setClickableOptions(boolean value){ // set Clickable property to all TextViews
        option1.setClickable(value);
        option2.setClickable(value);
        option3.setClickable(value);
        option4.setClickable(value);
    }
}