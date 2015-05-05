package fr.serli.breizhcampcountdown.app.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dev on 05/05/15.
 */
public class ImageLoadingTask extends AsyncTask<URL, Void, Bitmap> {


    @Override
    protected Bitmap doInBackground(URL... params) {
        try {
            HttpURLConnection connection = (HttpURLConnection) params[0].openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
