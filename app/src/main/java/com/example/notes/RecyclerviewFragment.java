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
import com.example.notes.databinding.MainToolbarBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecyclerviewFragment extends Fragment implements NotesAdapter.ItemClickListener, NoteFragment.SetNoteFragmentListener {

    public static ArrayList<Note> dataList=new ArrayList<>();
    RecyclerView recyclerView;
    public static NotesAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private FragmentRecyclerviewBinding fragmentBinding;
    private MainToolbarBinding toolbarBinding;
    Database database ;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Note newNoteAdded;
    private long note_id;


    public static RecyclerviewFragment newInstance() {
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
        toolbarBinding = fragmentBinding.mainToolbar;
        database=Database.getINSTANCE(getContext());
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
        updateList();
        FloatingActionButton addButton = fragmentBinding.addButton;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });
    }

    public void initRecyclerView(View view){
        dataList.add(new Note("Soft Skills",5689,"rrrrrrr"));
        dataList.add(new Note("Soft Skills",5689,"rrrrrrr"));
        dataList.add(new Note("Soft Skills",5689,"rrrrrrr"));
        dataList.add(new Note("Soft Skills",5689,"rrrrrrr"));
        recyclerView= view.findViewById(R.id.notes_recyclerView);
        adapter=new NotesAdapter(dataList,this);
        recyclerView.setAdapter(adapter);
        layoutManager= new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

    }
    public void updateList(){
        database.Dao().getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                dataList.clear();
                dataList.addAll(notes);
                adapter.notifyDataSetChanged();
            }
        });
    }
    //add new note
    public void addNote(){
        NoteFragment noteFragment = NoteFragment.newInstance(this);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,noteFragment)
                .addToBackStack(null)
                .commit();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                newNoteAdded = new Note();
                database.Dao().addNote(newNoteAdded);
                note_id = newNoteAdded.getId();
            }
        });
        //updateList();
    }
    //open note
    @Override
    public void onNoteClick(Note note) {
        NoteFragment noteFragment = NoteFragment.openFragment(this,note.getTitle(),
                note.getBackground(),note.getDescription());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,noteFragment)
                .addToBackStack(null)
                .commit();
        note_id = note.getId();
    }

    @Override
    public void onSaveButtonClick(String title, int background, String description) {
        database.Dao().update(note_id,title,description,background);
        //updateList();
    }
}