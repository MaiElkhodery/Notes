package com.example.notes;


import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Database.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.Holder>{

    private ArrayList<Note> items_list;
    private ItemClickListener clickListener;
    Drawable background;

    public NotesAdapter(ArrayList<Note> data, ItemClickListener itemClickListener){
        this.items_list=data;
        this.clickListener=itemClickListener;
    }
    public interface ItemClickListener{
        void onNoteClick(Note note);
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
        holder.noteTitle_TV.setText(currentItem.getTitle());
        holder.noteDescription_TV.setText(currentItem.getDescription());
        //setBackground(holder,currentItem);
        holder.itemView.setBackgroundColor(currentItem.getBackground());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onNoteClick(currentItem);
            }
        });
    }

    public void setBackground(Holder holder,Note currentNote){
        background = holder.itemView.getBackground();

        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(currentNote.getBackground());
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(currentNote.getBackground());
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(currentNote.getBackground());
        }
    }
    @Override
    public int getItemCount() {
        return items_list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView noteTitle_TV;
        TextView noteDescription_TV;
        public Holder(View view){
            super(view);
            noteTitle_TV=view.findViewById(R.id.noteTitle_textView);
            noteDescription_TV=view.findViewById(R.id.noteDescription_textView);
        }

        public TextView getNoteTitle_TV() {
            return noteTitle_TV;
        }

        public TextView getNoteDescription_TV() {
            return noteDescription_TV;
        }

    }
    public Note getNote(int position){
        return items_list.get(position);
    }
}



