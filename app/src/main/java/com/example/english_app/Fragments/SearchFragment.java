package com.example.english_app.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.english_app.Layouts.BadgeNotifyInterface;
import com.example.english_app.Entities.ApiResponse.RusTranslate;
import com.example.english_app.Entities.ApiResponse.TranslateResult;
import com.example.english_app.Entities.Database.Word;
import com.example.english_app.R;
import com.example.english_app.Utils.ApiUtils.JsonParser;
import com.example.english_app.Utils.DatabaseUtils.DBAdapter;
import com.example.english_app.Utils.SearchRecyclerAdapter;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.english_app.Utils.ApiUtils.ApiUtils.createURL;
import static com.example.english_app.Utils.ApiUtils.ApiUtils.getResponseFromURL;

public class SearchFragment extends Fragment {

    private DBAdapter dbAdapter; // adapter to open and use database
    private ArrayList<TranslateResult> translatedData; // response from API
    private int activeArrayIndex; // active index of translatedData that is shown on the screen

    private EditText et_enter;
    private Button search_button;
    private ProgressBar progressBar;
    private TextView eng_word;
    private TextView transcription;
    private TextView partOfSpeech;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbAdapter = new DBAdapter(getContext());

        et_enter = view.findViewById(R.id.et_enter);
        search_button = view.findViewById(R.id.button);
        progressBar = view.findViewById(R.id.progressBar);
        eng_word = view.findViewById(R.id.tv_engWord);
        transcription = view.findViewById(R.id.tv_transcription);
        partOfSpeech = view.findViewById(R.id.tv_partOfSpeech);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        search_button.setOnClickListener(new View.OnClickListener() { // кнопка перевода, отправка запроса на сервер (async)
            @Override
            public void onClick(View v) {
                String generatedURL = createURL(et_enter.getText().toString());
                new SearchFragment.AsyncQueryTask().execute(generatedURL);
            }
        });

        partOfSpeech.setOnClickListener(new View.OnClickListener() { // кнопка смены части речи
            @Override
            public void onClick(View v) {
                if (translatedData != null) {
                    changePartOfSpeech(translatedData);
                }
            }
        });
        partOfSpeech.setVisibility(View.INVISIBLE);
    }


    class AsyncQueryTask extends AsyncTask<String, Void, String> { // асинхронный процесс выполнения запроса на сервер

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String response = null;
            try {
                response = getResponseFromURL(strings[0]);
            } catch (IOException e) {
                Log.e(this.getClass().getName(), "Error with getting response", e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            progressBar.setVisibility(View.INVISIBLE);

            translatedData = null;
            if (response != null) {
                translatedData = JsonParser.ParseResponse(response);
            }

            if (response == null || translatedData == null) {
                Toast toast = Toast.makeText(getContext(), "Translate error, please try again!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            loadDataOnViews(translatedData, 0); // load 0 element of translatedData
            activeArrayIndex = 0; // set active index == 0
        }
    }

    private void loadDataOnViews(ArrayList<TranslateResult> translatedData, int arrayIndex) { // заполняет данными визуальные элементы (RecycleView + TV), arrayIndex - индекс в массиве переводов (разные части речи)
        SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(translatedData.get(arrayIndex));
        adapter.setOnItemClickListener(new SearchRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { // метод нажатия на конкретную карточку перевода, position - позиция отдельного ViewHolder (переводы)
                String engTranslate = translatedData.get(arrayIndex).getEngWord();
                ArrayList<RusTranslate> translates = translatedData.get(arrayIndex).getTranslations();
                String rusTranslate = translates.get(position).getRuTranslate();

                String toastText = "The word already exists!";
                try {

                    dbAdapter.open();
                    Word add_word = new Word(-1, rusTranslate, engTranslate);
                    if (dbAdapter.checkForExistence(add_word)) {
                        Toast toast = Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    } else {
                        dbAdapter.insert(add_word);
                    }

                    BadgeNotifyInterface notifyInterface = (BadgeNotifyInterface) getContext();
                    notifyInterface.notifyBadge(1);
                    toastText = "Added successfully!";
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "Error adding to the database", e);
                    toastText = "Error adding to the database!";
                }
                finally {
                    dbAdapter.close();
                }

                Toast toast = Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        recyclerView.setAdapter(adapter);

        eng_word.setText(translatedData.get(arrayIndex).getEngWord());
        transcription.setText(translatedData.get(arrayIndex).getTranscription());
        partOfSpeech.setText(translatedData.get(arrayIndex).partOfSpeech());
        partOfSpeech.setVisibility(View.VISIBLE);
    }


    private void changePartOfSpeech(ArrayList<TranslateResult> translatedData) // меняет часть речи переведенного слова (загружает другой recycleView)
    {
        if (activeArrayIndex < translatedData.size()) {
            loadDataOnViews(translatedData, activeArrayIndex);
            this.activeArrayIndex++;
        } else {
            this.activeArrayIndex = 0;
        }
    }
}
