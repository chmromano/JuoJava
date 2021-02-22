package fi.metropolia.christro.juo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity(tableName = "intakes_table")
public class IntakeInput {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "date")
    private String date;

    public IntakeInput(int amount) {
        this.amount = amount;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return this.id;
    }

    public int getAmount() {
        return this.amount;
    }
}
