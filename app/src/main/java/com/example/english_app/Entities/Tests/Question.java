package com.example.english_app.Entities.Tests;

public class Question {
    private int id; // question id
    private String englishWord; // activity_main word
    private String option1; // options in rus
    private String option2;
    private String option3;
    private String option4;
    private int correctOption; // number of correct answer

    public Question(int id, String englishWord, String option1, String option2, String option3, String option4, int correctOption) {
        this.id = id;
        this.englishWord = englishWord;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        if (correctOption >= 1 && correctOption <= 4){
            this.correctOption = correctOption;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }
}
