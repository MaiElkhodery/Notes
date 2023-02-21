package com.example.notes.Database;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    //there is no dates in database so i want to convert it to and from long when dealing with database
    @TypeConverter
    public static Date toDate(Long milliseconds){
        return milliseconds == null ?null:new Date(milliseconds);
    }
    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ?null:date.getTime();
    }
}
