package com.example.notes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notes.Database.Database;
import com.example.notes.databinding.FragmentRecyclerviewBinding;
import com.example.notes.databinding.MainToolbarBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    public Database database ;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private RecyclerviewFragment recyclerviewFragment;
    public static final String RECYCLER_FRAG_TAG = "recyclerview";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatabase();
        viewNotes();
    }

    public void initDatabase(){
        database=Database.getINSTANCE(this);
    }
    //open/show recycler view
    public void viewNotes(){
        recyclerviewFragment = RecyclerviewFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,recyclerviewFragment)
                .addToBackStack(RECYCLER_FRAG_TAG)
                .commit();
    }

}