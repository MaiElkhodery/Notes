package com.example.notes;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Database.Database;
import com.example.notes.Database.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Holder>{

    private ArrayList<Note> items_list;
    private ItemClickListener clickListener;

    public NotesAdapter(ArrayList<Note> data, ItemClickListener itemClickListener){
        this.items_list=data;
        this.clickListener=itemClickListener;
    }
    public interface ItemClickListener{
        public void onItemClick(Note note);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Note currentItem = items_list.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items_list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView noteTitle_TV;
        TextView noteDescription_TV;
        TextView noteLastEdit_TV;
        public Holder(View view){
            super(view);
            noteTitle_TV=view.findViewById(R.id.noteTitle_textView);
            noteDescription_TV=view.findViewById(R.id.noteDescription_textView);
            noteLastEdit_TV=view.findViewById(R.id.noteLastEditDate_textView);
        }

        public TextView getNoteTitle_TV() {
            return noteTitle_TV;
        }

        public TextView getNoteDescription_TV() {
            return noteDescription_TV;
        }

        public TextView getNoteLastEdit_TV() {
            return noteLastEdit_TV;
        }
    }
    public Note getNote(int position){
        return items_list.get(position);
    }
}



