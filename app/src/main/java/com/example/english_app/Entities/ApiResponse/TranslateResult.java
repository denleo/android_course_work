package com.example.english_app.Entities.ApiResponse;

import java.util.ArrayList;

public class TranslateResult {
    private String eng_word;
    private String transcription;
    private String partOfSpeech;
    private ArrayList<RusTranslate> translations;

    public TranslateResult(){}

    public String getEngWord(){
        return eng_word;
    }

    public String getTranscription(){
        return transcription;
    }

    public String partOfSpeech(){
        return partOfSpeech;
    }

    public ArrayList<RusTranslate> getTranslations(){
        return translations;
    }

    public void setEng_word(String eng_word) {
        this.eng_word = eng_word;
    }

    public void setTranslations(ArrayList<RusTranslate> translations) {
        this.translations = translations;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }
}
