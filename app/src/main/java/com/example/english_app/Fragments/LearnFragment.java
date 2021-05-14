package com.example.english_app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.english_app.Layouts.QuizActivity;
import com.example.english_app.Layouts.WritingActivity;
import com.example.english_app.R;

public class LearnFragment extends Fragment {

    CardView quizCardView, writingCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learn, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quizCardView = view.findViewById(R.id.cv_quiz);
        writingCardView = view.findViewById(R.id.cv_writing_test);

        quizCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QuizActivity.class);
                startActivity(intent);
            }
        });

        writingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WritingActivity.class);
                startActivity(intent);
            }
        });
    }

}
