package com.example.english_app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.english_app.Entities.Database.Word;
import com.example.english_app.R;

public class AddDialog extends DialogFragment {

    private Object obj;

    private EditText edit_eng;
    private EditText edit_rus;
    private AddDialogInterface addDialogInterface;

    public AddDialog(Object obj) {
        super();
        this.obj = obj;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Create translation")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String rus, eng;
                        rus = edit_rus.getText().toString();
                        eng = edit_eng.getText().toString();

                        addDialogInterface.AddRecord(new Word(-1, rus, eng));
                    }
                });

        edit_eng = view.findViewById(R.id.et_dialog_english);
        edit_rus = view.findViewById(R.id.et_dialog_rus);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            addDialogInterface = (AddDialogInterface) obj;
        } catch (ClassCastException e) {
            throw new ClassCastException(obj.toString() + "must implements AddDialogInterface!");
        }
    }

    public interface AddDialogInterface{
        void AddRecord(Word translation);
    }
}
