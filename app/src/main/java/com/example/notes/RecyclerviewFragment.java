package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecyclerviewFragment extends Fragment implements NotesAdapter.ItemClickListener, NoteFragment.SetNoteFragmentListener{

    public static ArrayList<Note> dataList=new ArrayList<>();
    RecyclerView recyclerView;
    public NotesAdapter adapter;
    public static FragmentRecyclerviewBinding fragmentBinding;
    private MainToolbarBinding toolbarBinding;
    Database database ;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    RecyclerView.LayoutManager layoutManager;
    private long note_id;
    boolean clickAdd;
    private final String SP_NAME = "view";
    SharedPreferences sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener pref_changeListener;
    private static final String VIEW = "linear_layout";
    private boolean linearLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sharedPreferences=context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences settings = getActivity().getSharedPreferences(SP_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(VIEW, linearLayout);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences settings = getActivity().getSharedPreferences(SP_NAME, 0);
        linearLayout = settings.getBoolean(VIEW, false);
        initRecyclerView();
    }

    public static RecyclerviewFragment newInstance() {
        return new RecyclerviewFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentRecyclerviewBinding.inflate(inflater,container,false);
        View view = fragmentBinding.getRoot();
        toolbarBinding = fragmentBinding.mainToolbar;
        database=Database.getINSTANCE(getContext());

        recyclerView= view.findViewById(R.id.notes_recyclerView);
        initRecyclerView();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onClickSettings();
        updateList();
        addNote();
        onClickSettings();
        deleteNote();
    }

    public void initRecyclerView(){
        if(linearLayout){
            layoutManager=new LinearLayoutManager(getContext());
        }
        else{
            layoutManager=new GridLayoutManager(getContext(),2);
        }
        recyclerView.setLayoutManager(layoutManager);
        adapter=new NotesAdapter(dataList,this,getContext(),layoutManager);
        recyclerView.setAdapter(adapter);
    }
    public void updateList(){
        database.Dao().getAllNotes().observe(getViewLifecycleOwner(), notes -> {
            dataList.clear();
            dataList.addAll(notes);
            adapter.notifyDataSetChanged();
        });
    }
    //add new note
    public void addNote(){
        FloatingActionButton addButton = fragmentBinding.addButton;
        addButton.setOnClickListener(view -> {
            clickAdd = true;
            NoteFragment noteFragment = NoteFragment.newInstance(RecyclerviewFragment.this);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,noteFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
    @Override
    public void onSaveButtonClick(String title, int background, String description) {
        Note note = new Note(title,background,description);
        if(clickAdd){
            executorService.execute(() -> database.Dao().addNote(note));
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
        toolbarBinding.settingsIcon.setOnClickListener(view -> {
            Log.d("openSetting", "method");
            SettingsFragment settingsFragment = SettingsFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,settingsFragment)
                    .addToBackStack(null)
                    .commit();
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
                executorService.execute(() -> database.Dao()
                        .deleteNote(adapter.getNote(viewHolder.getAdapterPosition())));
            }
        }).attachToRecyclerView(recyclerView);
    }
}