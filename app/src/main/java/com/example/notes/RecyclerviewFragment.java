package com.example.notes;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.Database.Database;
import com.example.notes.Database.Note;
import com.example.notes.databinding.FragmentRecyclerviewBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerviewFragment extends Fragment implements NotesAdapter.ItemClickListener {

    public static ArrayList<Note> dataList=new ArrayList<>();
    RecyclerView recyclerView;
    public static NotesAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private FragmentRecyclerviewBinding fragmentBinding;
    private static SetListener listener ;
    Database database ;

    public interface SetListener{
        void onAddButtonClick();
        void onNoteClick();
    }

    public static RecyclerviewFragment newInstance(SetListener clickListener) {
        listener =  clickListener;
        RecyclerviewFragment fragment = new RecyclerviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentBinding = FragmentRecyclerviewBinding.inflate(inflater,container,false);
        View view = fragmentBinding.getRoot();
        database=Database.getINSTANCE(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
        showList();
        FloatingActionButton addButton = fragmentBinding.addButton;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddButtonClick();
            }
        });
    }

    public void initRecyclerView(View view){
        dataList.add(new Note(new Date(2020,3,21),"Soft Skills",5689,"rrrrrrr"));
        dataList.add(new Note(new Date(2020,3,21),"Soft Skills",5689,"rrrrrrr"));
        dataList.add(new Note(new Date(2020,3,21),"Soft Skills",5689,"rrrrrrr"));
        dataList.add(new Note(new Date(2020,3,21),"Soft Skills",5689,"rrrrrrr"));
        recyclerView= view.findViewById(R.id.notes_recyclerView);
        adapter=new NotesAdapter(dataList,this);
        recyclerView.setAdapter(adapter);
        layoutManager= new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

    }
    public void showList(){
        database.Dao().getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                dataList.clear();
                dataList.addAll(notes);
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onItemClick(Note note) {
        listener.onNoteClick();
    }
}