package com.example.english_app.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.english_app.R;

public class RefineDialog extends DialogFragment {

    String correctOption, userOption;
    boolean userAnswer;
    private TextView tv_correct_option;
    private TextView tv_user_option;

    RefineDialogInterface refineDialogInterface;

    public RefineDialog(String correctOption, String userOption){
        if(correctOption != null && userOption != null){
            this.correctOption = correctOption;
            this.userOption = userOption;
        }
        else{
            throw new NullPointerException();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        refineDialogInterface.processAnswer(userAnswer);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.refine_dialog, null);

        builder.setView(view)
                .setTitle("The answer is correct?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userAnswer = true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        tv_correct_option = view.findViewById(R.id.tv_correct_input);
        tv_user_option = view.findViewById(R.id.tv_user_input);
        tv_correct_option.setText(correctOption);
        tv_user_option.setText(userOption);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            refineDialogInterface = (RefineDialogInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.getClass().getName() + "must implements RefineDialogInterface!");
        }
        userAnswer = false;
    }

    public interface RefineDialogInterface{
        void processAnswer(boolean userAnswer);
    }

}
