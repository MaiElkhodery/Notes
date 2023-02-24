package com.example.notes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Database.Database;
import com.example.notes.Database.Note;
import com.example.notes.databinding.FragmentRecyclerviewBinding;
import com.example.notes.databinding.MainToolbarBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecyclerviewFragment extends Fragment implements SettingsFragment.SetSettingsListener, NotesAdapter.ItemClickListener, NoteFragment.SetNoteFragmentListener{

    public static ArrayList<Note> dataList=new ArrayList<>();
    RecyclerView recyclerView;
    public static NotesAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public static FragmentRecyclerviewBinding fragmentBinding;
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
        onClickSettings();
        deleteNote();
    }

    public void initRecyclerView(View view){
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
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                newNoteAdded = new Note();
                database.Dao().addNote(newNoteAdded);
                note_id = newNoteAdded.getId();
                Log.d("getId_newNote","ID : "+note_id);
            }
        });
        NoteFragment noteFragment = NoteFragment.newInstance(this);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,noteFragment)
                .addToBackStack(null)
                .commit();
    }
    //open note
    @Override
    public void onNoteClick(Note note) {
        note_id = note.getId();
        Log.d("getId_onNoteClick","ID : "+note_id);
        NoteFragment noteFragment = NoteFragment.openFragment(this,note.getTitle(),
                note.getBackground(),note.getDescription());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,noteFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSaveButtonClick(String title, int background, String description) {
        Note note = new Note(title,background,description);
        note.setId(note_id);
        database.Dao().addNote(note);
        Log.d("getId_onSave","ID : "+note_id);
    }

    public void onClickSettings(){
        ImageView settingIcon = toolbarBinding.settingsIcon;
        settingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = SettingsFragment.newInstance(RecyclerviewFragment.this);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,settingsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void changeLayoutToLinear() {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }
    @Override
    public void changeLayoutToGrid() {
        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void deleteNote(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        database.Dao()
                                .deleteNote(adapter.getNote(viewHolder.getAdapterPosition()));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }
}