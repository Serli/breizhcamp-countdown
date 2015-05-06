package fr.serli.breizhcampcountdown.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class ConfActivity extends Activity {

    private EditText txtUrl;
    private TextView txtPreview;
    private CheckBox checkbox;
    private SeekBar seekbar;
    private View btnPreview;
    private SharedPreferences preferences;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        txtPreview = (TextView) findViewById(R.id.conf_txt_preview);
        txtUrl = (EditText) findViewById(R.id.conf_url);
        checkbox = (CheckBox) findViewById(R.id.conf_chkbox);
        seekbar = (SeekBar) findViewById(R.id.conf_seekbar);
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
                final String imgUrl = txtUrl.getText().toString();
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

                Picasso.with(v.getContext())
                        .load(imgUrl)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                image = bitmap;
                                try {
                                    FileOutputStream out = openFileOutput("bcCountdownLogo.png", Context.MODE_PRIVATE);
                                    image.compress(Bitmap.CompressFormat.PNG, 90, out);
                                    Intent retour = new Intent(ConfActivity.this, MainActivity.class);
                                    startActivity(retour);
                                } catch (FileNotFoundException e) {
                                    Toast.makeText(getApplicationContext(), "Error while saving image", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {
                                Toast.makeText(getApplicationContext(), "Error while saving image", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });


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
