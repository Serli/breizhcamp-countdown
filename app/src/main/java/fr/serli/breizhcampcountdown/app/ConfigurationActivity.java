package fr.serli.breizhcampcountdown.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ConfigurationActivity extends Activity {

    public static final String TEXT_SIZE_PREFERENCE = "TEXT_SIZE_PREFERENCE";
    public static final String IMAGE_URL_PREFERENCE = "IMAGE_URL_PREFERENCE";
    public static final String IMAGE_ORIENTATION_PREFERENCE = "IMAGE_ORIENTATION_PREFERENCE";
    public static final int VERTICAL_IMAGE_ORIENTATION = -90;
    public static final int HORIZONTAL_IMAGE_ORIENTATION = 0;
    public static final int TEXT_MIN_SIZE = 20;
    public static final int CONF_DEFAULT_TEXT_SIZE = 100;
    public static final int TEXT_MAX_SIZE =150;


    @InjectView(R.id.conf_text_url)
    EditText imageUrlText;
    @InjectView(R.id.conf_preview_button)
    View previewButton;
    @InjectView(R.id.conf_rotate_checkBox)
    CheckBox rotateCheckBox;
    @InjectView(R.id.conf_image_preview)
    ImageView imagePreview;
    @InjectView(R.id.conf_text_preview)
    TextView countDownSizePreview;
    @InjectView(R.id.conf_seekbar)
    SeekBar countDownSizeSeekBar;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);
        ButterKnife.inject(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        String preferredURL = preferences.getString(IMAGE_URL_PREFERENCE, getResources().getString(R.string.conf_default_logo_url));
        imageUrlText.setText(preferredURL);

        int preferredImageOrientation = preferences.getInt(IMAGE_ORIENTATION_PREFERENCE, HORIZONTAL_IMAGE_ORIENTATION);
        rotateCheckBox.setChecked(preferredImageOrientation == VERTICAL_IMAGE_ORIENTATION);

        int preferredTextSize = preferences.getInt(TEXT_SIZE_PREFERENCE, CONF_DEFAULT_TEXT_SIZE);
        countDownSizePreview.setTextSize(preferredTextSize);
        countDownSizeSeekBar.setProgress(getSeekBarProgressFromTextSize(preferredTextSize));

        previewButton.requestFocus();
        imagePreview.setEnabled(false);

        addSeekBarChangeListener();
    }

    private void addSeekBarChangeListener() {
        countDownSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                countDownSizePreview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getTextSizeFromSeekBarProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @OnClick(R.id.conf_preview_button)
    public void onClickPreviewButton(View view) {
        imagePreview.setEnabled(true);
        drawImage(view);
    }

    @OnClick(R.id.conf_save_button)
    public void onClickSaveButton(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        String imgUrl = imageUrlText.getText().toString();
        editor.putString(IMAGE_URL_PREFERENCE, imgUrl);
        editor.putInt(IMAGE_ORIENTATION_PREFERENCE, getRotationValueFromCheckBox());
        editor.putInt(TEXT_SIZE_PREFERENCE, getTextSizeFromSeekBarProgress());
        editor.commit();

        Intent back = new Intent(ConfigurationActivity.this, MainActivity.class);
        startActivity(back);
    }

    @OnClick(R.id.conf_rotate_checkBox)
    public void clickOnCheckBox(View view){
        drawImage(view);
    }

    private int getRotationValueFromCheckBox() {
        return rotateCheckBox.isChecked() ? VERTICAL_IMAGE_ORIENTATION : HORIZONTAL_IMAGE_ORIENTATION;
    }

    private int getTextSizeFromSeekBarProgress() {
        int progress = countDownSizeSeekBar.getProgress();
        int max = countDownSizeSeekBar.getMax();

        return TEXT_MIN_SIZE + (TEXT_MAX_SIZE - TEXT_MIN_SIZE) * progress / max;
    }

    private int getSeekBarProgressFromTextSize(int textSize) {
        return (textSize - TEXT_MIN_SIZE) * countDownSizeSeekBar.getMax() / TEXT_MAX_SIZE;
    }

    private void drawImage(View view) {
        final String imgUrl = imageUrlText.getText().toString();
        Picasso.with(view.getContext())
                .load(imgUrl)
                .rotate(getRotationValueFromCheckBox())
                .into((imagePreview));
    }

}
