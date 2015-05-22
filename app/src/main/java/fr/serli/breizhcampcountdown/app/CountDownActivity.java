package fr.serli.breizhcampcountdown.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class CountDownActivity extends AppCompatActivity {
    public static final int NTH_RATIO_TO_TURN_RED = 10;
    public static final int NTH_RATIO_TO_TURN_YELLOW = 5;

    public static final String DURATION = "duration";

    @InjectView(R.id.count_down_logo)
    ImageView logo;
    @InjectView(R.id.count_down_text)
    TextView contentView;

    private final NumberFormat numberFormat = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count_down);
        ButterKnife.inject(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);


        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        int textSize = preferences.getInt(ConfigurationActivity.TEXT_SIZE_PREFERENCE, ConfigurationActivity.CONF_DEFAULT_TEXT_SIZE);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);

        Intent receptedIntent = getIntent();
        final Long duration = receptedIntent.getLongExtra(DURATION, 3);

        final Long durationInMillis = duration * 60000;

        new CountDownTimer(durationInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                String minutes = numberFormat.format(millisUntilFinished / 60000);

                contentView.setText(minutes);

                if (millisUntilFinished <= durationInMillis / NTH_RATIO_TO_TURN_RED) {
                    contentView.setBackgroundColor(Color.RED);
                } else if (millisUntilFinished <= durationInMillis / NTH_RATIO_TO_TURN_YELLOW) {
                    contentView.setBackgroundColor(Color.YELLOW);
                }

            }

            public void onFinish() {
                contentView.setText(getString(R.string.count_downt_times_up));

                float textSize = contentView.getWidth() / 20;
                contentView.setTextSize(textSize);

                final AnimationDrawable drawable = new AnimationDrawable();
                drawable.addFrame(new ColorDrawable(Color.RED), 400);
                drawable.addFrame(new ColorDrawable(Color.BLACK), 400);
                drawable.setOneShot(false);

                contentView.setBackground(drawable);
                drawable.start();
            }
        }.start();

        String imageUrl = preferences.getString(ConfigurationActivity.IMAGE_URL_PREFERENCE, getResources().getString(R.string.conf_default_logo_url));
        int rotate = preferences.getInt(ConfigurationActivity.IMAGE_ORIENTATION_PREFERENCE, ConfigurationActivity.VERTICAL_IMAGE_ORIENTATION);

        Picasso.with(this.getApplicationContext())
                .load(imageUrl)
                .rotate(rotate)
                .into(logo);
    }
}
