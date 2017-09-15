package com.ideaone.reveraprojectapp1;

import android.content.Context;
import android.os.AsyncTask;

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


public class SettingsDownloadLocation extends AsyncTask<Void, Integer, Void> {

    ArrayList<SettingsLocationObject> locationArrayList = new ArrayList<>();
    Communicator context;

    SettingsDownloadLocation(Context c) {
        this.context = (Communicator) c;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<String> location = new ArrayList<>();
        try {
            URL theUrl = new URL(SettingsActivity.theLocationUrl);
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String download_json = reader.readLine();

            JSONObject data_object = new JSONObject(download_json);
            JSONArray data_arr = data_object.getJSONArray("displays");

            int totalObjects = data_arr.length();
            for (int i = 0; i < totalObjects; i++) {
                publishProgress((int) (((i + 1.0) / totalObjects) * 100.0));
                if (data_arr.getJSONObject(i).has("handle")) {
                    location.add(data_arr.getJSONObject(i).getString("handle"));
                    SettingsLocationObject obj = new SettingsLocationObject(data_arr.getJSONObject(i).getString("handle"));
                    locationArrayList.add(obj);
                }
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

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        context.updateProgressToLoc(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.updateUILoc(locationArrayList);
    }

    interface Communicator {
        public void updateProgressToLoc(int progress);

        public void updateUILoc(ArrayList<SettingsLocationObject> locArrayList);
    }
}