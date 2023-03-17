package com.example.notes;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Database.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Note> items_list;
    private ItemClickListener clickListener;
    Drawable background;
    Context aContext;
    RecyclerView.LayoutManager layoutManager;
    private final String SP_NAME = "view";
    SharedPreferences sharedPreferences;
    private static final String VIEW = "linear_layout";
    private boolean linearLayout;

    public NotesAdapter(ArrayList<Note> data, ItemClickListener itemClickListener, Context context, RecyclerView.LayoutManager layoutManager) {
        this.items_list = data;
        this.clickListener = itemClickListener;
        aContext = context;
        this.layoutManager=layoutManager;
        sharedPreferences = aContext.getSharedPreferences(SP_NAME, 0);
        linearLayout =  sharedPreferences.getBoolean(VIEW, false);
    }

    public interface ItemClickListener {
        void onNoteClick(Note note);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(aContext);
        if (linearLayout) {
            Log.d("LinearLayout", "True");
            View itemView = inflater.inflate(R.layout.linear_layout_notes, parent, false);
            return new LinearViewHolder(itemView);
        } else {
            Log.d("GridLayout", "True");
            View itemView = inflater.inflate(R.layout.note_item, parent, false);
            return new GridViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Note data = items_list.get(position);
        setBackground(holder, data);
        holder.itemView.setOnClickListener(view -> clickListener.onNoteClick(data));
        if (linearLayout) {
            ((LinearViewHolder) holder).bind(data);
        } else {
            ((GridViewHolder) holder).bind(data);
        }
    }
    public void setBackground(RecyclerView.ViewHolder holder, Note currentNote) {
        background = holder.itemView.getBackground();
        Drawable drawable = AppCompatResources.getDrawable(aContext, R.drawable.note_item_background);
        drawable.setColorFilter(currentNote.getBackground(), PorterDuff.Mode.SRC_IN);
        holder.itemView.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return items_list.size();
    }


    static class LinearViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle_TV;
        TextView noteDescription_TV;

        public LinearViewHolder(View view) {
            super(view);
            noteTitle_TV = view.findViewById(R.id.noteTitle_textViewLinear);
            noteDescription_TV = view.findViewById(R.id.noteDescription_textViewLinear);
        }

        public void bind(Note data) {
            noteTitle_TV.setText(data.getTitle());
            noteDescription_TV.setText(data.getDescription());
            //itemView.setBackgroundColor(data.getBackground());
        }
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        TextView noteTitle_TV;
        TextView noteDescription_TV;

        public GridViewHolder(View view) {
            super(view);
            noteTitle_TV = view.findViewById(R.id.noteTitle_textView);
            noteDescription_TV = view.findViewById(R.id.noteDescription_textView);
        }

        public void bind(Note data) {
            noteTitle_TV.setText(data.getTitle());
            noteDescription_TV.setText(data.getDescription());
            //itemView.setBackgroundColor(data.getBackground());

        }
    }
    public Note getNote(int position) {
        return items_list.get(position);
    }
}



