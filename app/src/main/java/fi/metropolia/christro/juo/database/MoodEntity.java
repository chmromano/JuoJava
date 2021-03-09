package fi.metropolia.christro.juo.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Represents a MoodEntity entity and corresponding table in the database with a date and a mood.
 *
 * @author Christopher Mohan Romano
 * @version 1.0
 */
//Guide used: https://developer.android.com/codelabs/android-room-with-a-view#4
@Entity(tableName = "mood_table")
public class MoodEntity {
    /**
     * Primary key date. Set automatically.
     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private String date;
    /**
     * Integer ID of the entered mood.
     */
    @ColumnInfo(name = "mood")
    private int mood;

    /**
     * Constructor. Creates a MoodEntity with the specified mood (1 = bad, 2 = normal, 3 = good),
     * and automatically sets date based on when the MoodEntity is created.
     *
     * @param mood An integere containing the MoodEntity mood.
     */
    public MoodEntity(int mood) {
        this.mood = mood;
        this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    /**
     * Getter method for MoodEntity date.
     *
     * @return A string representing the entity date.
     */
    @NonNull
    public String getDate() {
        return date;
    }

    /**
     * Setter method for MoodEntity date.
     *
     * @param date A string containing the MoodEntity date.
     */
    public void setDate(@NonNull String date) {
        this.date = date;
    }

    /**
     * Getter method for MoodEntity mood.
     *
     * @return An integer representing the entity mood.
     */
    public int getMood() {
        return mood;
    }

    /**
     * Setter method for IntakeEntity mood.
     *
     * @param mood An integer containing the MoodEntity mood.
     */
    public void setMood(int mood) {
        this.mood = mood;
    }
}
