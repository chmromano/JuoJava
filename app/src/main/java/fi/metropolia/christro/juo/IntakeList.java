package fi.metropolia.christro.juo;

import java.util.ArrayList;
import java.util.List;

public class IntakeList {
    private static final IntakeList intakeListInstance = new IntakeList();

    public static IntakeList getInstance() {
        return intakeListInstance;
    }

    private List<IntakeObject> intakes;

    private IntakeList() {
        this.intakes = new ArrayList<>();
    }

    public void addIntake(int amount) {
        this.intakes.add(new IntakeObject(amount));
    }

    public List<IntakeObject> getAllIntakes(){
        return this.intakes;
    }

    //Method ot save list to file

    //Method to read list from file
}
