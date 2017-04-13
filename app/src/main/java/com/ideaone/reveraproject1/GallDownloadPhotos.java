package com.ideaone.reveraproject1;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GallDownloadPhotos extends AsyncTask<Void, Integer, Void> {

    /*
    urls_single_album_thumbnail.add("http://static-a.reveraconnect.com/250/" + result.get(i) + ".jpg");
    urls_full.add("http://static-a.reveraconnect.com/full/" + result.get(i) + ".jpg");
     */

    ArrayList<GallPhotoObject> photosArrayList = new ArrayList<>();
    Communicator context;
    DBAdapterPhotos dbPhoto;

    GallDownloadPhotos(GallPhotoFragment c) {
        this.context = c;
        dbPhoto = new DBAdapterPhotos(c.getActivity().getBaseContext());
    }

    @Override
    protected Void doInBackground(Void... params) {
        URL theUrl;
        try {
            theUrl = new URL(GallPhotoFragment.newRURL);
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String revera_json = reader.readLine();
            JSONObject revera_object = new JSONObject(revera_json);

            Log.e("revera_object", "" + revera_object);

            JSONArray data_arr = revera_object.getJSONArray("photos");

            int totalPhotos = data_arr.length();
            for (int i = 0; i < totalPhotos; i++) {
                String creatorID = data_arr.getJSONObject(i).getString("creatorID");
                String created = data_arr.getJSONObject(i).getString("created");
                String mClass = data_arr.getJSONObject(i).getString("class");
                String type = data_arr.getJSONObject(i).getString("type");
                String photo_ext = data_arr.getJSONObject(i).getString("ext");
                String photo_name = data_arr.getJSONObject(i).getString("handle");
                String photoID = data_arr.getJSONObject(i).getString("ID");
                String image_url = "http://static-a.reveraconnect.com/250/" + photo_name + "." + photo_ext;
                //  String image_url_full = "http://static-a.reveraconnect.com/full/" + photo_name +"."+ photo_ext;

                URL downloadURL = new URL(image_url);
                HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
                InputStream inputStream = conn.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);

                /*
                URL downloadURL2 = new URL(image_url_full);
                HttpURLConnection conn2 = (HttpURLConnection) downloadURL2.openConnection();
                InputStream inputStream2 = conn2.getInputStream();
                Bitmap bmp2 = BitmapFactory.decodeStream(inputStream2);
                */


                GallPhotoObject obj = new GallPhotoObject(creatorID, created, mClass, type,
                        photo_ext, photo_name, photoID, bmp, null);
                photosArrayList.add(obj);
                saveToPhotos(obj);
                publishProgress((int) (((i + 1.0) / totalPhotos) * 100.0));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveToPhotos(GallPhotoObject obj) {
        try {
            byte[] imgStream = new byte[0];
            if (obj.thumbnail != null) {
                try {
                    Bitmap photo = obj.thumbnail;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    imgStream = stream.toByteArray();
                    //stream.reset();
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            }

            dbPhoto.open();
            // dbAlb.resetDB();
            Cursor c = dbPhoto.getItem(obj.photoName);
            if (!c.moveToFirst()) {
                Log.e("obj1 photo", "" + obj.photoName);
                dbPhoto.insertItem(obj.creatorID, obj.created, obj.mClass, obj.type,
                        obj.photoExt, obj.photoName, obj.ID, imgStream, null);
            } else {
                Log.e("obj2 photo", "" + obj.photoName);
                dbPhoto.updateItem(obj.creatorID, obj.created, obj.mClass, obj.type,
                        obj.photoExt, obj.photoName, obj.ID, imgStream, null);
            }
            dbPhoto.close();
            Log.e("obj photo", "" + obj.photoName);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        context.updateProgressTo(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.updateUI(photosArrayList);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
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

    interface Communicator {
        void updateProgressTo(int progress);

        void updateUI(ArrayList<GallPhotoObject> photosArrayList);
    }
}