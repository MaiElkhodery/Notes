package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
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

    private long note_id;
    boolean clickAdd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean linearLayout;
    private final String SP_NAME = "view";
    private static final String VIEW ="linear_layout";


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
        initRecyclerView(view);
        ///////////////////////
        sharedPreferences = getActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        linearLayout = sharedPreferences.getBoolean(VIEW,false);
        if(linearLayout){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateList();
        addNote();
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
        FloatingActionButton addButton = fragmentBinding.addButton;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAdd = true;
                NoteFragment noteFragment = NoteFragment.newInstance(RecyclerviewFragment.this);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer,noteFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    @Override
    public void onSaveButtonClick(String title, int background, String description) {
        Note note = new Note(title,background,description);
        if(clickAdd){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    database.Dao().addNote(note);
                }
            });
            clickAdd=false;
        }
        else{
            note.setId(note_id);
            database.Dao().addNote(note);
        }
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,RecyclerviewFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
    //open note
    @Override
    public void onNoteClick(Note note) {
        note_id = note.getId();
        NoteFragment noteFragment = NoteFragment.openFragment(this,note.getTitle(),
                note.getBackground(),note.getDescription());
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,noteFragment)
                .addToBackStack(null)
                .commit();
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

    @Override
    public void changeView(SwitchCompat switcher) {
        sharedPreferences = getActivity().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        linearLayout = sharedPreferences.getBoolean(VIEW,false);
        if(linearLayout){
            switcher.setChecked(true);
        }
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sharedPreferences.edit();
                if(linearLayout){
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
                    adapter.notifyDataSetChanged();
                    editor.putBoolean(VIEW,false);
                }
                else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter.notifyDataSetChanged();
                    editor.putBoolean(VIEW,true);
                }
                editor.apply();
            }
        });
    }
}