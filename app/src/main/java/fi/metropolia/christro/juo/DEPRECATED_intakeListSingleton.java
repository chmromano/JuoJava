package fi.metropolia.christro.juo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DEPRECATED_intakeListSingleton {

    /*private static final String PREFERENCES_FILE = "intakeListGSON";
    private static final String PREFERENCES_INTAKES_KEY = "intakeJsonKey";

    private static IntakeList intakeListInstance;

    public static IntakeList getInstance(Context context) {
        if(intakeListInstance == null){
            loadData(context);
        }
        return intakeListInstance;
    }

    public static IntakeList loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PREFERENCES_INTAKES_KEY, null);
        Type type = new TypeToken<ArrayList<IntakeObject>>() {}.getType();
    }

    public void saveToFile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        editor.putString(PREFERENCES_INTAKES_KEY, json);
        editor.apply();
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
    }*/
}
