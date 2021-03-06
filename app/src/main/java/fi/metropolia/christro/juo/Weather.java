package fi.metropolia.christro.juo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Weather {
    private double temperature;
    private String humidity;
    private int weatherId;

    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?units=metric&appid=35be7f414814f513a3bdf6ce70e1fcec&q=";
    private static final Weather weatherInstance = new Weather();

    public static Weather getInstance() {
        return weatherInstance;
    }

    private Weather() {
        this.humidity = null;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void getWeather(String location, Context context) {
        if (location == null) {
            this.humidity = null;
            return;
        }

        String weatherUrl = API_URL + location;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, weatherUrl, response -> {
            try {
                JSONObject responseObject = new JSONObject(response);
                //find country
                JSONObject jsonMain = responseObject.getJSONObject("main");
                JSONArray jsonWeather = responseObject.getJSONArray("weather");
                JSONObject jsonWeatherId = jsonWeather.getJSONObject(0);

                this.humidity = jsonMain.getString("humidity");

                String stringTemperature = jsonMain.getString("temp");
                try {
                    this.temperature = Double.parseDouble(stringTemperature);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                String stringWeatherId = jsonWeatherId.getString("id");
                try {
                    this.weatherId = Integer.parseInt(stringWeatherId);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
