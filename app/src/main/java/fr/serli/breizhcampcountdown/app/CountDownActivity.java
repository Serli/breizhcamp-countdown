package fr.serli.breizhcampcountdown.app;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import fr.serli.breizhcampcountdown.app.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.BitSet;


public class CountDownActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private LinearLayout layout;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        final TextView contentView = (TextView) findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();


        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        layout = (LinearLayout) findViewById(R.id.cd_frame_layout);
        logo = ((ImageView) findViewById(R.id.logo));

        if (preferences.getBoolean("logoOnTop", false)) {
            layout.setOrientation(LinearLayout.VERTICAL);
        }

        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, preferences.getFloat("textSize", 120));

        Intent receptedIntent = getIntent();
        Long duration = receptedIntent.getLongExtra("duration", 3);

        Long durationInMillis = duration*60000;

        new CountDownTimer(durationInMillis, 1000) {
            public void onTick(long millisUntilFinished){
                String minutes = ""+millisUntilFinished/60000;
                String secondes = ""+(millisUntilFinished%60000)/1000;

                if(Integer.parseInt(secondes)<10){
                    secondes="0"+secondes;
                }
                if(Integer.parseInt(minutes)<10){
                    minutes="0"+minutes;
                }
                contentView.setText(minutes + ":" + secondes);

                if (millisUntilFinished<=300000){
                    layout.setBackgroundColor(Color.rgb(240, 250, 60));
                }
            }

            public void onFinish(){
                contentView.setText("Time's up !");

                float textSize = layout.getWidth() / 11;
                contentView.setTextSize(textSize);

                final AnimationDrawable drawable = new AnimationDrawable();
                drawable.addFrame(new ColorDrawable(Color.RED), 400);
                drawable.addFrame(new ColorDrawable(Color.BLACK), 400);
                drawable.setOneShot(false);

                layout.setBackground(drawable);
                drawable.start();
            }
        }.start();

        File img = getFileStreamPath("bcCountdownLogo.png");
        Picasso.with(this.getApplicationContext())
                .invalidate(img);
        Picasso.with(this.getApplicationContext())
                .load(img)
                .into(logo);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
