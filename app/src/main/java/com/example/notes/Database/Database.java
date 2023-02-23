package com.example.notes.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Note.class},version = 1)
public abstract class Database extends RoomDatabase {

    public abstract DAO Dao();
    private volatile static Database INSTANCE;
    public static Database getINSTANCE(final Context context){

        if (INSTANCE == null) {
            synchronized (Database.class) {
                //if statement prevents repeating of creating the database multiple times when rotating
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, Database.class, "NotesDatabase")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
