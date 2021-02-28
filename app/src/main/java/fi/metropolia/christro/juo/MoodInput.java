package fi.metropolia.christro.juo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

@Entity(tableName = "mood_table")
public class MoodInput {

    @PrimaryKey
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "mood")
    private int mood;

    public MoodInput(int mood){
        this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        this.mood = mood;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }
}
