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
import java.util.Date;
import java.util.List;
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