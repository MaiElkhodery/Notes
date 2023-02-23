package com.example.notes.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addNote(Note note);
    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllNotes();
    @Delete
    void deleteNote(Note note);
    @Query("DELETE FROM Note")
    void deleteAllNotes();
    @Query("UPDATE Note SET title =:title,description=:description, background =:background WHERE id =:id ")
    void update(long id, String title, String description,int background);
    @Query("SELECT * FROM Note where id = :id")
    Note getNote(long id);
}
