package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SetButtonsActivity extends AppCompatActivity {

    private ArrayList<Integer> customButtonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_buttons);

        customButtonList = loadButtonList();
    }

    private void saveButtonList(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(customButtonList);

        editor.putString(MainActivity.CUSTOM_BUTTONS_LIST_KEY, json);

        editor.apply();
    }

    private ArrayList<Integer> loadButtonList(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFERENCE_FILE , Activity.MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sharedPreferences.getString(MainActivity.CUSTOM_BUTTONS_LIST_KEY, null);

        if (json == null) {
            ArrayList<Integer> arrayList = new ArrayList<>();

            arrayList.add(250);
            arrayList.add(500);
            arrayList.add(100);
            arrayList.add(750);

            return arrayList;
        }

        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();

        return gson.fromJson(json, type);
    }
}