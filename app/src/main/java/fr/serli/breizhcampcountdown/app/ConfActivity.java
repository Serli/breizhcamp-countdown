package fr.serli.breizhcampcountdown.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.squareup.picasso.Picasso;


public class ConfActivity extends Activity {

    EditText txtUrl;
    TextView txtPreview;
    CheckBox checkbox;
    SeekBar seekbar;
    View btnPreview;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        txtPreview = (TextView) findViewById(R.id.conf_txt_preview);
        txtUrl = (EditText) findViewById(R.id.conf_url);
        checkbox = (CheckBox) findViewById(R.id.conf_chkbox);
        seekbar = (SeekBar)findViewById(R.id.conf_seekbar);
        btnPreview = findViewById(R.id.conf_btn_preview);

        checkbox.setChecked(preferences.getBoolean("logoOnTop", false));

        float textSize = preferences.getFloat("textSize", 120);
        txtPreview.setTextSize(textSize);
        seekbar.setProgress(Math.round(textSize));

        txtUrl.setText(preferences.getString("imageUrl", "http://www.breizhcamp.org/img/logo.png"));

        btnPreview.requestFocus();

        txtUrl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgUrl = txtUrl.getText().toString();
                Picasso.with(v.getContext())
                        .load(imgUrl)
                        .into((ImageView) findViewById(R.id.conf_img_preview));
            }
        });

        findViewById(R.id.conf_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                String imgUrl = txtUrl.getText().toString();
                editor.putString("imageUrl", imgUrl);
                editor.apply();
                editor.putFloat("textSize", txtPreview.getTextSize());
                editor.apply();
                editor.putBoolean("logoOnTop", checkbox.isChecked());
                editor.apply();
                Intent retour = new Intent(ConfActivity.this, MainActivity.class);
                startActivity(retour);
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float coefficient = progress;
                if (coefficient == 0) {
                    coefficient = 1;
                }
                txtPreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, coefficient);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
