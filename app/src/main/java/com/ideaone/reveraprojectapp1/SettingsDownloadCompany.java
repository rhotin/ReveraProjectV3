package com.ideaone.reveraprojectapp1;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;

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


public class SettingsDownloadCompany extends AsyncTask<Void, Integer, Void> {

    ArrayList<SettingsCompanyObject> companiesArrayList = new ArrayList<>();
    Communicator context;

    SettingsDownloadCompany(Context c) {
        this.context = (Communicator) c;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String name = "";
        String text = "";
        try {
            URL theUrl = new URL("http://retirement.ideaone.tv/displays/rc1/promos.json");
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String download_json = reader.readLine();

            JSONObject data_object = new JSONObject(download_json);
            JSONArray data_arr = data_object.getJSONArray("promos");

            int totalObjects = data_arr.length();
            for (int i = 0; i < totalObjects; i++) {
                publishProgress((int) (((i + 1.0) / totalObjects) * 100.0));

                if (data_arr.getJSONObject(i).has("name") && data_arr.getJSONObject(i).has("text")) {
                    if (data_arr.getJSONObject(i).getString("name").equals("RetirementGroup")) {
                        name = data_arr.getJSONObject(i).getString("name");
                        text = data_arr.getJSONObject(i).getString("text");

                        String[] companies = text.split(",");
                        for (String company : companies) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                company = Html.fromHtml(company, Html.FROM_HTML_MODE_COMPACT).toString().trim();
                            } else {
                                company = Html.fromHtml(company).toString().trim();
                            }
                            SettingsCompanyObject obj = new SettingsCompanyObject(name, company);
                            companiesArrayList.add(obj);
                        }
                    }
                }
                //  SettingsCompanyObject obj = new SettingsCompanyObject(name, text);
                //  companiesArrayList.add(obj);
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
        context.updateProgressTo(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.updateUI(companiesArrayList);
    }

    interface Communicator {
        public void updateProgressTo(int progress);

        public void updateUI(ArrayList<SettingsCompanyObject> libsArrayList);
    }
}