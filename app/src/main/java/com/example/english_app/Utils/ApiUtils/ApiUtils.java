package com.example.english_app.Utils.ApiUtils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiUtils {
    private static final String API_BASE_URL = "https://systran-systran-platform-for-language-processing-v1.p.rapidapi.com/resources/dictionary/lookup";
    private static final String SOURCE_LANG = "en";
    private static final String TARGET_LANG = "ru";
    private static final String API_KEY = "b836dc4bddmsh25de442aab81accp1fab1djsn09d573840e71";
    private static final String API_HOST = "systran-systran-platform-for-language-processing-v1.p.rapidapi.com";

    public static String createURL(String word){
        Uri builtUri = Uri.parse(API_BASE_URL)
                .buildUpon()
                .appendQueryParameter("source", SOURCE_LANG)
                .appendQueryParameter("target", TARGET_LANG)
                .appendQueryParameter("input", word)
                .build();

        return builtUri.toString();
    }

    public static String getResponseFromURL(String str_url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        try{
            Request request = new Request.Builder()
                    .url(str_url)
                    .get()
                    .addHeader("x-rapidapi-key", API_KEY)
                    .addHeader("x-rapidapi-host", API_HOST)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch (UnknownHostException e){
            Log.e(ApiUtils.class.getName(), "Unknown host", e);
            return null;
        }
        finally {
            client.dispatcher();
        }

    }

}
