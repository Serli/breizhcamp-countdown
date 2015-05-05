package fr.serli.breizhcampcountdown.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import fr.serli.breizhcampcountdown.app.util.ImageLoadingTask;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class ConfActivity extends Activity {

    Bitmap image;
    EditText txtUrl;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);

        preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

        txtUrl = (EditText) findViewById(R.id.conf_url);

        txtUrl.setText(preferences.getString("imageUrl",""));

        findViewById(R.id.conf_btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgUrl = txtUrl.getText().toString();
                try {
                    URL url = new URL(imgUrl);
                    ImageLoadingTask imgLoader = new ImageLoadingTask();
                    imgLoader.execute(url);
                    image = imgLoader.get();
                    if (image != null) ((ImageView) findViewById(R.id.conf_img_preview)).setImageBitmap(image);
                } catch (MalformedURLException e) {
                    Toast.makeText(v.getContext(), "Url invalide", Toast.LENGTH_SHORT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.conf_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image!=null) {
                    String imgUrl = txtUrl.getText().toString();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("imageUrl", imgUrl);
                    editor.commit();


                    try {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        FileOutputStream output = openFileOutput("bcCountdownLogo.png", MODE_PRIVATE);
                        output.write(byteArray);

                    } catch (FileNotFoundException e) {
                        Toast.makeText(v.getContext(), "File not found", Toast.LENGTH_LONG);
                    } catch (IOException e) {
                        Toast.makeText(v.getContext(), "Error while saving image", Toast.LENGTH_LONG);
                    }
                }
                Intent retour = new Intent(ConfActivity.this,MainActivity.class);
                startActivity(retour);
            }
        });



    }
}
