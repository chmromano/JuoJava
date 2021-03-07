package fi.metropolia.christro.juo.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//Guide used: https://developer.android.com/codelabs/android-room-with-a-view#4

/**
 * Represents an IntakeEntity entity and corresponding table in the database, with an ID, amount (of water),
 * and date and time of intake.
 * @author Christopher Mohan Romano
 * @version 1.0
 */
@Entity(tableName = "intakes_table")
public class IntakeEntity {
    //Instance variables and associated columns.
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "amount")
    private int amount;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "time")
    private String time;

    /**
     * Constructor. Creates an IntakeEntity with the specified amount, and automatically sets date
     * and time based on when the IntakeEntity is created.
      * @param amount The intake amount.
     */
    public IntakeEntity(int amount) {
        this.amount = amount;
        this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        this.time = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    /**
     * Getter method for IntakeEntity ID.
     * @return An integer representing the entity ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter method for Intake Entity ID.
     * @param id An integer containing the IntakeEntity ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for IntakeEntity amount.
     * @return An integer representing the entity amount.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Setter method for IntakeEntity amount.
     * @param amount An integer containing the IntakeEntity amount.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Getter method for IntakeEntity date.
     * @return A string representing the entity date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter method for IntakeEntity date.
     * @param date A string containing the IntakeEntity date.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter method for IntakeEntity time.
     * @return A string representing the entity time.
     */
    public String getTime() {
        return time;
    }

    /**
     * Setter method for IntakeEntity time.
     * @param time A string containing the IntakeEntity time.
     */
    public void setTime(String time) {
        this.time = time;
    }
}
