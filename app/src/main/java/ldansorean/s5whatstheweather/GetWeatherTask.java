package ldansorean.s5whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Asynchronous task making a call to openweathermap API to get the weather for a given city.
 */
public class GetWeatherTask extends AsyncTask<String, Void, String> {

    private final static String WEATHER_API_GET_BY_CITY_URL = "https://openweathermap.org/data/2.5/weather?q={city}&appid=439d4b804bc8187953eb36d2a8c26a02";
    private final static String CITY_KEY = "{city}";
    private TextView weatherView;

    public GetWeatherTask(TextView weatherView) {
        this.weatherView = weatherView;
    }

    @Override
    protected String doInBackground(String... cities) {
        try {
            String encodedCityName = URLEncoder.encode(cities[0], "UTF-8");
            String urlStr = WEATHER_API_GET_BY_CITY_URL.replace(CITY_KEY, encodedCityName);
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

        String weatherDescription = "";
        if (result == null || result.length() == 0) {
            showErrorMessage();
        } else {
            try {
                JSONObject jsonRoot = new JSONObject(result);
                JSONArray weather = jsonRoot.getJSONArray("weather");
                for (int i = 0; i < weather.length(); i++) {
                    JSONObject weatherPart = weather.getJSONObject(i);
                    weatherDescription += weatherPart.getString("main") + ": " + weatherPart.getString("description") + "\r\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("app", "Error parsing JSON response (" + result + ")");
                showErrorMessage();
            }
        }
        weatherView.setText(weatherDescription);
    }

    private void showErrorMessage() {
        Toast.makeText(weatherView.getContext(), "Could not find weather for that city.", Toast.LENGTH_SHORT).show();
    }
}
