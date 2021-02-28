package fi.metropolia.christro.juo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherLocationActivity extends AppCompatActivity {

    String url;
    Button btnGo;
    TextView temperature, humidity, pressure, windSpeed;
    EditText locationInput;

    private static final String TAG = "WEATHER";

    private static final String API_URL= "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=35be7f414814f513a3bdf6ce70e1fcec";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_location);

        btnGo = findViewById(R.id.imageView);
        temperature = findViewById(R.id.txtTemperature);
        humidity = findViewById(R.id.txtHumidity);
        pressure = findViewById(R.id.textView5);
        windSpeed = findViewById(R.id.textView6);
        locationInput = findViewById(R.id.editText);

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Btn clicked");

                hideSoftKeyboard(WeatherLocationActivity.this);
                Editable location = locationInput.getText();

                locationInput.clearFocus();

                getWeather(location.toString());

                Log.d(TAG,"current location is"+location.toString());

            }
        });
    }
    public void getWeather(String location){

        url =API_URL+"&q="+location;

        Log.d(TAG,"URL "+url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //calling api

                Log.d(TAG,"RESPONSE "+ response );
                try {

                    JSONObject responseObject = new JSONObject(response);
                    //find country
                    JSONObject  main = responseObject.getJSONObject("main");

                    String temp = main.getString("temp");
                    String humidityValue = main.getString("humidity");
                    String pressureValue = main.getString("pressure");

                    String windSpeedValue = responseObject.getJSONObject("wind").getString("speed");

                    temperature.setText("Temperature "+ temp + " \u2103");
                    humidity.setText("Humidity "+ humidityValue + " %");
                    pressure.setText("Pressure "+pressureValue + " hpa");
                    windSpeed.setText("Wind "+ windSpeedValue + " m/s");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

     RequestQueue requestQueue =Volley.newRequestQueue(WeatherLocationActivity.this);
     requestQueue.add(stringRequest);

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}