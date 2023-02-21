package com.example.notes.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@TypeConverters({DateConverter.class})
@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private Date last_modifying_date;
    private String title;
    private int background;
    private String description;

    public Note(@NonNull Date last_modifying_date, String title, int background, String description) {
        this.last_modifying_date = last_modifying_date;
        this.title = title;
        this.background = background;
        this.description = description;
    }

    public Note() {}

    public long getId() {
        return id;
    }

    @NonNull
    public Date getLast_modifying_date() {
        return last_modifying_date;
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

    public void setLast_modifying_date(@NonNull Date last_modifying_date) {
        this.last_modifying_date = last_modifying_date;
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
