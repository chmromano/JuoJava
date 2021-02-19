package fi.metropolia.christro.juo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Calendar;

@Entity(tableName = "intakes_table")
public class IntakeInput {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int amount;

    private int year;

    private int month;

    @ColumnInfo(name = "day_of_month")
    private int dayOfMonth;

    private int hour;

    private int minute;

    public IntakeInput(int amount) {
        this.amount = amount;
        this.year = Calendar.getInstance().get(Calendar.YEAR);
        this.month = Calendar.getInstance().get(Calendar.MONTH);
        this.dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        this.hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        this.minute = Calendar.getInstance().get(Calendar.MINUTE);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
