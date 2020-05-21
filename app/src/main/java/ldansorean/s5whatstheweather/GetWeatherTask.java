package ldansorean.s5whatstheweather;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Asynchronous task making a call to openweathermap API to get the weather for a given city.
 */
public class GetWeatherTask extends AsyncTask<String, Void, String> {

    private final static String WEATHER_API_GET_BY_CITY_URL = "https://samples.openweathermap.org/data/2.5/weather?q={city}&appid=439d4b804bc8187953eb36d2a8c26a02";
    private final static String CITY_KEY = "{city}";

    @Override
    protected String doInBackground(String... cities) {
        try {
            String urlStr = WEATHER_API_GET_BY_CITY_URL.replace(CITY_KEY, cities[0]);
            InputStream is = new URL(urlStr).openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();

            String currentLine = reader.readLine();
            while (currentLine != null) {
                result.append(currentLine);
                currentLine = reader.readLine();
            }

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject jsonRoot = new JSONObject(result);
            JSONArray weather = jsonRoot.getJSONArray("weather");
            for (int i = 0; i < weather.length(); i++) {
                JSONObject weatherPart = weather.getJSONObject(i);
                Log.i("app", "Main is " + weatherPart.getString("main"));
                Log.i("app", "Description is " + weatherPart.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("app", "Error parsing JSON response (" + result + ")");
        }
    }
}
