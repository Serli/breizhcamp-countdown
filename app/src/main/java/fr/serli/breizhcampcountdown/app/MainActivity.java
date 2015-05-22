package fr.serli.breizhcampcountdown.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String PREVIOUS_DURATION_PREFERENCE = "PREVIOUS_DURATION_PREFERENCE";

    @InjectView(R.id.duration)
    EditText durationSet;
    @InjectView(R.id.btn_start)
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        setContentView(R.layout.main);
        ButterKnife.inject(this);


        durationSet.setText(preferences.getString(PREVIOUS_DURATION_PREFERENCE, "10"));

        durationSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s.toString())) {
                    btnStart.setEnabled(false);
                } else {
                    btnStart.setEnabled(true);
                }
            }
        });
    }

    @OnClick(R.id.btn_start)
    public void onClickOnStart() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        Long duration = Long.parseLong(durationSet.getText().toString());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREVIOUS_DURATION_PREFERENCE, duration.toString());
        editor.apply();
        Intent startCountdown = new Intent(MainActivity.this, CountDownActivity.class);
        startCountdown.putExtra(CountDownActivity.DURATION, duration);
        startActivity(startCountdown);
        MainActivity.this.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startConf = new Intent(MainActivity.this, ConfigurationActivity.class);
            startActivity(startConf);
            MainActivity.this.onPause();
        }

        return super.onOptionsItemSelected(item);
    }
}
