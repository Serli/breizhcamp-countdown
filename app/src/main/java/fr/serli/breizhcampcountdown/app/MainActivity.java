package fr.serli.breizhcampcountdown.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    private EditText durationSet;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        setContentView(R.layout.activity_main);

        durationSet = (EditText) findViewById(R.id.duration);

        durationSet.setText(preferences.getString("previousDuration", "10"));

        btnStart = (Button) findViewById(R.id.btn_start);

        durationSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    btnStart.setEnabled(false);
                } else {
                    btnStart.setEnabled(true);
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long duration = Long.parseLong(durationSet.getText().toString());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("previousDuration", duration.toString());
                editor.apply();
                Intent startCountdown = new Intent(MainActivity.this, CountDownActivity.class);
                startCountdown.putExtra("duration", duration);
                startActivity(startCountdown);
                MainActivity.this.onPause();
            }
        });

        (findViewById(R.id.conf)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startConf = new Intent(MainActivity.this, ConfActivity.class);
                startActivity(startConf);
                MainActivity.this.onPause();
            }

        });
    }
}
