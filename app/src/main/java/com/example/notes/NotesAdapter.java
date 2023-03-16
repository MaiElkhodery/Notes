package com.example.notes;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Database.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LINEAR = 1;
    private static final int VIEW_TYPE_GRID = 2;
    private ArrayList<Note> items_list;
    private ItemClickListener clickListener;
    Drawable background;
    Context aContext;
    RecyclerView.LayoutManager layoutManager;

    public NotesAdapter(ArrayList<Note> data, ItemClickListener itemClickListener, Context context, RecyclerView.LayoutManager layoutManager) {
        this.items_list = data;
        this.clickListener = itemClickListener;
        aContext = context;
        this.layoutManager=layoutManager;
    }

    public interface ItemClickListener {
        void onNoteClick(Note note);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(aContext);
        if (layoutManager instanceof LinearLayoutManager) {
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
        if (holder.getItemViewType() == VIEW_TYPE_LINEAR) {
            ((LinearViewHolder) holder).bind(data);
        } else {
            ((GridViewHolder) holder).bind(data);
        }
    }
    public void setBackground(RecyclerView.ViewHolder holder, Note currentNote) {
        background = holder.itemView.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(currentNote.getBackground());
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(currentNote.getBackground());
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(currentNote.getBackground());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (aContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return VIEW_TYPE_LINEAR;
        } else {
            return VIEW_TYPE_GRID;
        }
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
            itemView.setBackgroundColor(data.getBackground());
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
            itemView.setBackgroundColor(data.getBackground());
        }
    }
    public Note getNote(int position) {
        return items_list.get(position);
    }
}



