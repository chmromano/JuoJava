package fi.metropolia.christro.juo;

import java.util.Calendar;

public class IntakeObject {
    private int amount;
    private Calendar calendar;

    public IntakeObject(int amount) {
        this.calendar = Calendar.getInstance();
        this.amount = amount;
    }
}
