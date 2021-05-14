package com.example.english_app.Entities.ApiResponse;

import java.util.HashMap;

public class RusTranslate {
    private String ru_translate;
    private HashMap<String, String> usage_examples;

    public RusTranslate(String translate, HashMap<String, String> usage_example){
        ru_translate = translate;
        this.usage_examples = usage_example;
    }

    public RusTranslate() {}

    public String getRuTranslate(){
        return ru_translate;
    }

    public HashMap<String, String> getUsageExamples(){
        return usage_examples;
    }

    public void setRuTranslate(String ru_translate) {
        this.ru_translate = ru_translate;
    }

    public void setUsageExamples(HashMap<String, String> usage_examples) {
        this.usage_examples = usage_examples;
    }
}

