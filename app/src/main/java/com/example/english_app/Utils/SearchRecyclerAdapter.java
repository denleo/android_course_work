package com.example.english_app.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.english_app.Entities.ApiResponse.TranslateResult;
import com.example.english_app.Entities.ApiResponse.RusTranslate;
import com.example.english_app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ViewHolder> {

    private TranslateResult translatedData;
    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onClickListener){
        if (onClickListener != null){
            this.itemClickListener = onClickListener;
        }
    }

    public SearchRecyclerAdapter(TranslateResult translatedData){
        this.translatedData = translatedData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<RusTranslate> translatedResult = translatedData.getTranslations();
        holder.translated_word.setText(translatedResult.get(position).getRuTranslate());
        HashMap<String, String> usage_examples = translatedResult.get(position).getUsageExamples();

        int i = 0;
        for (Map.Entry<String,String> pair: usage_examples.entrySet()) {
            if (i == 0){
                holder.en_example1.setText(pair.getKey());
                holder.ru_example1.setText(pair.getValue());
            }
            else if (i == 1){
                holder.en_example2.setText(pair.getKey());
                holder.ru_example2.setText(pair.getValue());
            }
            else{
                break;
            }
            i++;
        }

    }

    @Override
    public int getItemCount() {
        return translatedData.getTranslations().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView translated_word, en_example1, ru_example1, en_example2, ru_example2;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            translated_word = itemView.findViewById(R.id.tv_ru_translate);
            en_example1 = itemView.findViewById(R.id.tv_eng_example1);
            ru_example1 = itemView.findViewById(R.id.tv_ru_example1);
            en_example2 = itemView.findViewById(R.id.tv_eng_example2);
            ru_example2 = itemView.findViewById(R.id.tv_ru_example2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

}
