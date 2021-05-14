package com.example.english_app.Utils.TestUtils;

import android.content.Context;
import android.util.Log;

import com.example.english_app.Entities.Database.Word;
import com.example.english_app.Entities.Tests.Question;
import com.example.english_app.Utils.DatabaseUtils.DBAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {

    static DBAdapter dbAdapter;
    static List<Word> wordArrayList;

    public static List<Question> generateQuestions(Context context) { // generate questions with unique options for test

        dbAdapter = new DBAdapter(context);
        dbAdapter.open();
        try {
            wordArrayList = dbAdapter.getAllWords();
        } catch (Exception e) {
            Log.e(QuestionGenerator.class.getName(), "Error with getting words from database", e);
        }
        dbAdapter.close();

        if (wordArrayList.size() < 4) {
            return null; // return null if we can't generate unique options
        }

        List<Question> questions = new ArrayList<>();
        Random random = new Random();

        int currentIndex = 0;
        for (Word word : wordArrayList) {

            String engWord = word.getEnTranslate();
            String[] options = new String[4];

            int correctAnswer = random.nextInt(4) + 1; // generate correct answer (1..4)
            options[correctAnswer - 1] = word.getRuTranslate();

            int[] alreadyGeneratedIndexes = new int[]{-1, -1, -1};
            int q = 0;
            for (int i = 0; i < 4; i++) {

                if (i == correctAnswer - 1) {
                    continue;
                }

                int randomIndex;
                do {
                    randomIndex = random.nextInt(wordArrayList.size());
                } while (checkIndex(randomIndex, alreadyGeneratedIndexes) || randomIndex == wordArrayList.indexOf(word));
                alreadyGeneratedIndexes[q] = randomIndex;
                q++;

                options[i] = wordArrayList.get(randomIndex).getRuTranslate();
            }

            questions.add(new Question(currentIndex + 1, engWord, options[0], options[1], options[2], options[3], correctAnswer));
            currentIndex++;
        }

        return questions;
    }

    private static boolean checkIndex(int index, int[] indexes) {
        for (int temp : indexes) {
            if (index == temp) {
                return true;
            }
        }
        return false;
    }
}
