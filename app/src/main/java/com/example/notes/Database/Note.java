package com.example.notes.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@TypeConverters({DateConverter.class})
@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private int background;
    private String description;

    public Note( String title, int background, String description) {
        this.title = title;
        this.background = background;
        this.description = description;
    }

    @Ignore
    public Note() {}

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getBackground() {
        return background;
    }

    public String getDescription() {
        return description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
