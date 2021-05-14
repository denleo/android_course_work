package com.example.english_app.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.english_app.Entities.Database.Word;
import com.example.english_app.R;

import java.util.List;

public class VocabularyRecyclerAdapter extends RecyclerView.Adapter<VocabularyRecyclerAdapter.ViewHolder> {

    private List<Word> wordArrayList;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onClickListener){
        if (onClickListener != null){
            this.itemClickListener = onClickListener;
        }
    }

    public VocabularyRecyclerAdapter(List<Word> words){
        wordArrayList = words;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vocabulary_item, parent, false);
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ru =  wordArrayList.get(position).getRuTranslate();
        String eng = wordArrayList.get(position).getEnTranslate();
        holder.engTranslate.setText(eng);
        holder.rusTranslate.setText(ru);
    }

    @Override
    public int getItemCount() {
        return wordArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView rusTranslate, engTranslate;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            rusTranslate = itemView.findViewById(R.id.tv_rusTranslation);
            engTranslate = itemView.findViewById(R.id.tv_engTranslation);

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


    public void clearAllItems(){
        wordArrayList.clear();
        notifyDataSetChanged();
    }

    public void addElement(Word newTranslation){
        wordArrayList.add(newTranslation);
        notifyItemInserted(wordArrayList.size() - 1);
    }

    public void removeItem(int position){
        wordArrayList.remove(position);
        notifyItemRangeChanged(0, wordArrayList.size());
        notifyItemRemoved(position);
    }

    public void updateItem(Word newTranslation, int position){
        wordArrayList.set(position, newTranslation);
        notifyItemChanged(position);
    }

}
