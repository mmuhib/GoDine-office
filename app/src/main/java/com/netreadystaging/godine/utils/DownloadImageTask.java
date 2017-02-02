package com.netreadystaging.godine.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


import java.io.InputStream;


/**
 * Created by sony on 12-01-2017.
 */
public class DownloadImageTask  extends AsyncTask<String, Void, Bitmap> {
    private final ProgressBar progressBar;
    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage, ProgressBar progressBar) {
        this.bmImage = bmImage;
        this.progressBar = progressBar ;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
            //         e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {

        bmImage.setImageBitmap(result);
        progressBar.setVisibility(View.GONE);
    }
}