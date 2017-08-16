package com.ideaone.reveraprojectapp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


class NewsDownload extends AsyncTask<Void, Integer, Void> {

    private ArrayList<NewsObject> newsArrayList = new ArrayList<>();
    NewsDownload.Communicator context;

    NewsDownload(NewsFragment c) {
        this.context = c;
    }

    @Override
    protected Void doInBackground(Void... params) {
        URL theUrl;
        try {
            theUrl = new URL(NewsFragment.NEWS_URL);
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String news_json = reader.readLine();
            Log.e("hello News", news_json);
            JSONObject news_object = new JSONObject(news_json);
            JSONArray data_arr = news_object.getJSONArray("articles");
            //JSONArray data_arr = announcement_object.getJSONArray("promos");

            int totalNews = data_arr.length();
            for (int i = 0; i < totalNews; i++) {
                String author = data_arr.getJSONObject(i).getString("author");
                String title = data_arr.getJSONObject(i).getString("title");
                String description = data_arr.getJSONObject(i).getString("description");
                String url = data_arr.getJSONObject(i).getString("url");
                String image_url = data_arr.getJSONObject(i).getString("urlToImage");

                Bitmap bmp = null;
                URL downloadURL = new URL(image_url);
                HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
                InputStream inputStreamI = conn.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                // options.inJustDecodeBounds = true;
                // BitmapFactory.decodeStream(inputStream, null, options);
                options.inScaled = false;
                options.inSampleSize = calculateInSampleSize(options, 200, 200);
                // options.inJustDecodeBounds = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                try {
                    bmp = BitmapFactory.decodeStream(inputStreamI, null, options);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

                try {
                    NewsObject obj = new NewsObject(author, title, description, url, image_url, bmp);
                    newsArrayList.add(obj);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException | OutOfMemoryError | JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of urlToImage
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            context.updateUI(newsArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface Communicator {
        //   void updateProgressTo(int progress);

        void updateUI(ArrayList<NewsObject> photosArrayList);
    }
}