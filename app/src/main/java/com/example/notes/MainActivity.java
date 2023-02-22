package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.example.notes.Database.Database;
import com.example.notes.Database.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerviewFragment.SetListener, NoteFragment.SetNoteFragmentListener {
    public Database database ;
    private ArrayList<Note> listOfData = RecyclerviewFragment.dataList;
    private NotesAdapter adapter=RecyclerviewFragment.adapter;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private RecyclerviewFragment recyclerviewFragment;
    private long noteId;
    int positionOfLastItem;
    public static final String RECYCLER_FRAG_TAG = "recyclerview";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        initDatabase();
        viewNotes();
    }

    public void initDatabase(){
        database=Database.getINSTANCE(this);
    }
    //open/show recycler view
    public void viewNotes(){
        recyclerviewFragment = RecyclerviewFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,recyclerviewFragment)
                .addToBackStack(RECYCLER_FRAG_TAG)
                .commit();
    }

    public void updateData(){
        database.Dao().getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Log.d("updateList","the problem in update list in main");
                RecyclerviewFragment.dataList.clear();
                RecyclerviewFragment.dataList.addAll(notes);
                RecyclerviewFragment.adapter.notifyDataSetChanged();
                positionOfLastItem = RecyclerviewFragment.adapter.getItemCount()-1;
            }
        });
    }
    public void addNote() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
        updateData();
    }

    //to add a new note
    @Override
    public void onAddButtonClick() {

        NoteFragment noteFragment = NoteFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,noteFragment)
                .addToBackStack(null)
                .commit();
    }
    //open note
    @Override
    public void onNoteClick() {

    }
    //save updates on note
    @Override
    public void onSaveButtonClick(String title,String description) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
        updateData();
    }
}