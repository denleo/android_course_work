package com.example.english_app.Layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.english_app.R;

public class ScoreResultActivity extends AppCompatActivity {

    private TextView score_result;
    private Button finish_button;
    private ScoreResultActivity self_pointer;
    private int questionsCount, score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_result);
        getSupportActionBar().hide();
        score_result = findViewById(R.id.tv_score);
        finish_button = findViewById(R.id.button_finish);
        self_pointer = this;

        finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self_pointer.finish();
            }
        });

        questionsCount = getIntent().getIntExtra("QuestionsCount", 0);
        score = getIntent().getIntExtra("Score", 0);
        String score_str = "Your score is " + score + "/" + questionsCount;
        score_result.setText(score_str);
    }
}