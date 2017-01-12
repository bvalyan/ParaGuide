package com.optimalotaku.paraguide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

/**
 * Created by Jerek on 1/12/2017.
 */

public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {
    public ImageLoaderResponse delegate = null;
    private CardData cardData;

    public ImageLoader(CardData cData){
        this.cardData = cData;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        //Take url and convert it to a bitmap image for CotD Menu button on Home Screen
        Bitmap bitImg;
        try {
            URL url = new URL(cardData.getImageUrl());
            bitImg = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bitImg;
        } catch (Exception e) {
            Log.e("Error:", "Error: Cannot convert Image URL: "+cardData.getImageUrl()+" to BitImage\n"+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(Bitmap bitImg) {
        delegate.processImageLoaderFinish(bitImg);
    }
}
