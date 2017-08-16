package com.ideaone.reveraprojectapp1;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GallDownloadAlbums extends AsyncTask<Void, Integer, Void> {

    ArrayList<GallAlbumObject> albumsArrayList = new ArrayList<>();
    CommunicatorA context;
    DBAdapterAlbums dbAlb;

    GallDownloadAlbums(GallPhotoFragment c) {
        this.context = c;
        dbAlb = new DBAdapterAlbums(c.getActivity().getBaseContext());
    }

    @Override
    protected Void doInBackground(Void... params) {
        URL theUrl;
        try {
            theUrl = new URL(GallPhotoFragment.AlURL);
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String revera_json = reader.readLine();

            JSONObject revera_object = new JSONObject(revera_json);
            JSONArray data_arr = revera_object.getJSONArray("data");

            String album_creator_id, album_created, album_name, album_display_id, album_web, album_display,
                    album_modified, album_handle, album_id;

            album_creator_id = album_created = album_name = album_display_id = album_web =
                    album_display = album_modified = album_handle = album_id = "";

            int totalAlbums = data_arr.length();
            for (int i = 0; i < totalAlbums; i++) {

                if (data_arr.getJSONObject(i).has("creatorID")) {
                    album_creator_id = data_arr.getJSONObject(i).getString("creatorID");
                }
                if (data_arr.getJSONObject(i).has("created")) {
                    album_created = data_arr.getJSONObject(i).getString("created");
                }
                if (data_arr.getJSONObject(i).has("title")) {
                    album_name = data_arr.getJSONObject(i).getString("title");
                }
                if (data_arr.getJSONObject(i).has("displayID")) {
                    album_display_id = data_arr.getJSONObject(i).getString("displayID");
                }
                if (data_arr.getJSONObject(i).has("web")) {
                    album_web = data_arr.getJSONObject(i).getString("web");
                }
                if (data_arr.getJSONObject(i).has("display")) {
                    album_display = data_arr.getJSONObject(i).getString("display");
                }
                if (data_arr.getJSONObject(i).has("modified")) {
                    album_modified = data_arr.getJSONObject(i).getString("modified");
                }
                if (data_arr.getJSONObject(i).has("handle")) {
                    album_handle = data_arr.getJSONObject(i).getString("handle");
                }
                if (data_arr.getJSONObject(i).has("ID")) {
                    album_id = data_arr.getJSONObject(i).getString("ID");
                }

                GallAlbumObject obj = new GallAlbumObject(album_creator_id, album_created, album_name,
                        album_display_id, album_web, album_display, album_modified, album_handle, album_id);
                albumsArrayList.add(obj);
                saveToAlbums(obj);
                publishProgress((int) (((i + 1.0) / totalAlbums) * 100.0));
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

    public void saveToAlbums(GallAlbumObject obj) {
        try {
            dbAlb.open();
            // dbAlb.resetDB();
            Cursor c = dbAlb.getItem(obj.albumID);
            if (!c.moveToFirst()) {
                Log.e("obj1 ALBUM", "" + obj.albumName);
                dbAlb.insertItem(obj.albumCreatorID, obj.albumCreated, obj.albumName, obj.albumDisplayID,
                        obj.albumWeb, obj.albumDisplay, obj.albumModified, obj.albumHandle, obj.albumID);
            } else {
                Log.e("obj2 ALBUM", "" + obj.albumName);
                dbAlb.updateItem(obj.albumCreatorID, obj.albumCreated, obj.albumName, obj.albumDisplayID,
                        obj.albumWeb, obj.albumDisplay, obj.albumModified, obj.albumHandle, obj.albumID);
            }
            dbAlb.close();
            Log.e("obj ALBUM", "" + obj.albumName);
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
        context.updateAUI(albumsArrayList);
    }

    interface CommunicatorA {
        void updateProgressTo(int progress);

        void updateAUI(ArrayList<GallAlbumObject> albumsArrayList);
    }
}