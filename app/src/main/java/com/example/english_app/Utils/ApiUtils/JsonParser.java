package com.example.english_app.Utils.ApiUtils;

import android.util.Log;

import com.example.english_app.Entities.ApiResponse.TranslateResult;
import com.example.english_app.Entities.ApiResponse.RusTranslate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JsonParser {
    public static ArrayList<TranslateResult> ParseResponse(String response){
        ArrayList<TranslateResult> translateData = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray matches = jsonObject.getJSONArray("outputs");
            jsonObject = matches.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject.getJSONObject("output");
            matches = jsonObject2.getJSONArray("matches");

            JSONObject match, source, target, expression;
            JSONArray targets, expressions;

            for (int i = 0; i < matches.length(); i++) {
                TranslateResult temp = new TranslateResult();
                match = matches.getJSONObject(i);
                source = match.getJSONObject("source");
                temp.setEng_word(source.getString("lemma"));;  // set english word
                temp.setTranscription(source.getString("phonetic"));  // set transcription
                temp.setPartOfSpeech(source.getString("pos"));  // set part of speech

                ArrayList<RusTranslate> wordTranslates = new ArrayList<>();  // rus definitions array
                targets = match.getJSONArray("targets");
                for (int j = 0; j < targets.length(); j++) {
                    RusTranslate wordTranslate = new RusTranslate();  // init definition
                    target = targets.getJSONObject(j);
                    wordTranslate.setRuTranslate(target.getString("lemma"));  // set rus translation

                    expressions = target.getJSONArray("expressions");
                    HashMap<String, String> hashMap = new HashMap<>();
                    for (int k = 0; k < expressions.length(); k++) {
                        expression = expressions.getJSONObject(k);
                        hashMap.put(expression.getString("source"), expression.getString("target"));  // set usage examples
                    }
                    wordTranslate.setUsageExamples(hashMap);
                    wordTranslates.add(wordTranslate);
                }

                temp.setTranslations(wordTranslates);
                translateData.add(temp);
            }

        } catch (JSONException e) {
            Log.e(JsonParser.class.getName(), "Error with parsing response", e);
            return null;
        }

        return translateData;
    }
}
