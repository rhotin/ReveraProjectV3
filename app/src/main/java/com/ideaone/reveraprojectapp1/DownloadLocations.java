package com.ideaone.reveraprojectapp1;

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

/**
 * Created by Roman on 2016-05-02.
 */
public class DownloadLocations  extends AsyncTask<Void, Integer, ArrayList> {
    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<String> location = new ArrayList<>();
        try {
            URL theUrl = new URL("http://revera.mxs-s.com/displays.json");
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String location_json = reader.readLine();

            JSONObject location_object = new JSONObject(location_json);
            JSONArray data_arr = location_object.getJSONArray("displays");

            int totalLocations = data_arr.length();
            for (int i = 0; i < totalLocations; i++) {
                location.add(data_arr.getJSONObject(i).getString("handle"));
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
        return location;
    }
}
