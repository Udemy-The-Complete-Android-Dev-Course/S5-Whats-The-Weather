package ldansorean.s5whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private EditText cityName;
    private TextView weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        weather = findViewById(R.id.weather);
    }

    public void getTheWeather(View view) {
        try {
            new GetWeatherTask(weather).execute(cityName.getText().toString());

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(weather.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            weather.setText("");
        }
    }
}
