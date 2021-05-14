package com.example.english_app.Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.english_app.Layouts.BadgeNotifyInterface;
import com.example.english_app.Entities.Database.Word;
import com.example.english_app.R;
import com.example.english_app.Utils.DatabaseUtils.DBAdapter;
import com.example.english_app.Utils.VocabularyRecyclerAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VocabularyFragment extends Fragment implements EditDialog.EditDialogInterface, AddDialog.AddDialogInterface {

    List<Word> vocabularyWords;  // list of words from database

    DBAdapter dbAdapter;
    RecyclerView recyclerView;
    VocabularyRecyclerAdapter recyclerAdapter;
    MediaPlayer mediaPlayer;
    FloatingActionButton fb_Add, fb_deleteAll;
    BadgeNotifyInterface notifyInterface; // interface to notify main activity about number of words

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vocabulary, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        dbAdapter.open();
        vocabularyWords = new ArrayList<>();
        recyclerAdapter = loadRecycleView();
    }

    @Override
    public void onStop() {
        super.onStop();
        dbAdapter.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.delete_swipe);
        dbAdapter = new DBAdapter(getContext());

        fb_Add = view.findViewById(R.id.fab_add_custom);
        fb_deleteAll = view.findViewById(R.id.fab_delete);
        fb_Add.setOnClickListener(new View.OnClickListener() {  // add custom record
            @Override
            public void onClick(View v) {
                AddDialog addDialog = new AddDialog(VocabularyFragment.this);
                addDialog.show(getFragmentManager(), this.getClass().getName());
            }
        });
        fb_deleteAll.setOnClickListener(new View.OnClickListener() {  // delete all notes
            @Override
            public void onClick(View v) {
                try {
                    dbAdapter.deleteAll();
                } catch (Exception e) {
                    Log.e(VocabularyFragment.class.getName(), "Error with deleting from database !", e);
                }
                recyclerAdapter.clearAllItems(); // clear all records
                notifyInterface.notifyBadge(0); // notify main badge
            }
        });

        recyclerView = view.findViewById(R.id.vocabulary_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        getItemTouchHelper().attachToRecyclerView(recyclerView); // добавление свайпов
        notifyInterface = (BadgeNotifyInterface) getContext(); // приводим контекст main activity к интерфейсу BadgeNotify
    }

    private VocabularyRecyclerAdapter loadRecycleView() { // заполняет данными визуальный элемент и возвращает адаптер (RecycleView)
        try {
            vocabularyWords = dbAdapter.getAllWords();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Error with getting words from database", e);
        }

        VocabularyRecyclerAdapter adapter = new VocabularyRecyclerAdapter(vocabularyWords);

        adapter.setOnItemClickListener(new VocabularyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Word oldTranslate = vocabularyWords.get(position);
                EditDialog editDialog = new EditDialog(oldTranslate, position, VocabularyFragment.this);
                editDialog.show(getFragmentManager(), this.getClass().getName());
            }
        });

        recyclerView.setAdapter(adapter);
        return adapter;
    }

    private ItemTouchHelper getItemTouchHelper() {  // возвращает класс, отвечающий за свайпы элементов в RecyclerView
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { // remove swiped element from database and list
                try {
                    dbAdapter.delete(vocabularyWords.get(viewHolder.getAdapterPosition()).getID());
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "Error with word delete (database)", e);
                    return;
                }
                recyclerAdapter.removeItem(viewHolder.getAdapterPosition());
                mediaPlayer.start();
                notifyInterface.notifyBadge(-1);
            }
        });
    }

    @Override
    public void editRecord(Word oldTranslate, Word newTranslate, int position) {
        try {
            if (dbAdapter.checkForExistence(newTranslate) && !oldTranslate.getEnTranslate().equals(newTranslate.getEnTranslate())) {
                Toast toast = Toast.makeText(getContext(), "The word already exists!", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            dbAdapter.update(newTranslate);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Error with word update (database)", e);
            return;
        }

        recyclerAdapter.updateItem(newTranslate, position);
    }

    @Override
    public void AddRecord(Word translation) {
        String toastText = "The word already exists!";
        try {
            if (dbAdapter.checkForExistence(translation)) {
                Toast toast = Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT);
                toast.show();
                return;
            } else {
                dbAdapter.insert(translation);
            }
            toastText = "Added successfully!";
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Error adding to the database", e);
            toastText = "Error adding to the database!";
        }

        recyclerAdapter.addElement(translation);
        notifyInterface.notifyBadge(1);
        Toast toast = Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT);
        toast.show();
    }
}
