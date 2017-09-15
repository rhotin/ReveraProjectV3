package com.ideaone.reveraprojectapp1;

import android.database.Cursor;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


class RecreationDownload extends AsyncTask<Void, Integer, Void> {
    private ArrayList<RecreationObject> objectArrayList = new ArrayList<>();
    Communicator context;

    URL theUrl;
    String URLLeg = RecreationFragment.URLLegend;
//    AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

    ArrayList<String> locCodes = new ArrayList<>();
    ArrayList<String> locations = new ArrayList<>();

    private int daysToSave = 7;
    private Boolean flag = true;

    DBAdapterRecreation db;

    RecreationDownload(RecreationFragment c) {
        this.context = c;
        db = new DBAdapterRecreation(c.getActivity().getApplicationContext());
    }

    @Override
    protected Void doInBackground(Void... params) {
        /** get legends */

        try {
            theUrl = new URL(RecreationFragment.URLLegend);

            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String location_json = reader.readLine();

            JSONObject location_object = new JSONObject(location_json);
            JSONArray data_arr = location_object.getJSONArray("data");

            int totalLocations = data_arr.length();
            for (int i = 0; i < totalLocations; i++) {
                locCodes.add(data_arr.getJSONObject(i).getString("code"));
                locations.add(data_arr.getJSONObject(i).getString("location"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        URL theUrl;
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date requested_date;

        int dateCounter = RecreationFragment.dateCounter;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int am = c.get(Calendar.AM_PM);

        try {
            if (dateCounter == 0 && hour >= 5 && hour <= 6  && am == Calendar.PM && flag) { // and time is between 5 am
                flag = false;
                for (int i = 0; i < daysToSave; i++) {

                    GregorianCalendar calendarDate;
                    calendarDate = new GregorianCalendar();
                    calendarDate.add(Calendar.DATE, i);
                    requested_date = calendarDate.getTime();

                    Log.e("REC DOWNLOAD","TEST" + requested_date);

                    theUrl = new URL("http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/recreation.json?date=" + sdf_date.format(requested_date) + "&nohtml=1");
                    parseJSON(theUrl);
                }
            } else {
                theUrl = new URL(RecreationFragment.URL);
                parseJSON(theUrl);
            }
            if(hour > 6){
                flag = true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //  @Override
    //  protected void onProgressUpdate(Integer... values) {
    //      super.onProgressUpdate(values);
    //      context.updateProgressTo(values[0]);
    //  }


    private void parseJSON(URL theUrl) {
        final String[] LOCATION_TAG = new String[10];
        String[] locationFullName = new String[10];

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String menu_json = reader.readLine();

            JSONObject menu_object = new JSONObject(menu_json);

            JSONObject menu_jobject = menu_object.getJSONObject("recreation");
            //Pulling menu
            String hr_temp, min_temp;

            for (int i = 0; i < 10; i++) {
                LOCATION_TAG[i] = "location_" + (i + 1);

                int locIndex = locCodes.indexOf(menu_jobject.get(LOCATION_TAG[i]).toString());
                if (locIndex < 0) {
                    locationFullName[i] = "";
                } else {
                    locationFullName[i] = locations.get(locIndex);
                }
            }

            RecreationObject obj = new RecreationObject((String) menu_jobject.get("creatorID"),
                    (String) menu_jobject.get("displayID"), (String) menu_jobject.get("date"),

                    String.valueOf(menu_jobject.get("ev_1")), String.valueOf(menu_jobject.get("min_1")),
                    String.valueOf(menu_jobject.get("hr_1")), menu_jobject.getString("activity_1"),
                    locationFullName[0],
                    String.valueOf(menu_jobject.get("ev_2")), String.valueOf(menu_jobject.get("min_2")),
                    String.valueOf(menu_jobject.get("hr_2")), menu_jobject.getString("activity_2"),
                    locationFullName[1],
                    String.valueOf(menu_jobject.get("ev_3")), String.valueOf(menu_jobject.get("min_3")),
                    String.valueOf(menu_jobject.get("hr_3")), menu_jobject.getString("activity_3"),
                    locationFullName[2],
                    String.valueOf(menu_jobject.get("ev_4")), String.valueOf(menu_jobject.get("min_4")),
                    String.valueOf(menu_jobject.get("hr_4")), menu_jobject.getString("activity_4"),
                    locationFullName[3],
                    String.valueOf(menu_jobject.get("ev_5")), String.valueOf(menu_jobject.get("min_5")),
                    String.valueOf(menu_jobject.get("hr_5")), menu_jobject.getString("activity_5"),
                    locationFullName[4],
                    String.valueOf(menu_jobject.get("ev_6")), String.valueOf(menu_jobject.get("min_6")),
                    String.valueOf(menu_jobject.get("hr_6")), menu_jobject.getString("activity_6"),
                    locationFullName[5],
                    String.valueOf(menu_jobject.get("ev_7")), String.valueOf(menu_jobject.get("min_7")),
                    String.valueOf(menu_jobject.get("hr_7")), menu_jobject.getString("activity_7"),
                    locationFullName[6],
                    String.valueOf(menu_jobject.get("ev_8")), String.valueOf(menu_jobject.get("min_8")),
                    String.valueOf(menu_jobject.get("hr_8")), menu_jobject.getString("activity_8"),
                    locationFullName[7],
                    String.valueOf(menu_jobject.get("ev_9")), String.valueOf(menu_jobject.get("min_9")),
                    String.valueOf(menu_jobject.get("hr_9")), menu_jobject.getString("activity_9"),
                    locationFullName[8],
                    String.valueOf(menu_jobject.get("ev_10")), String.valueOf(menu_jobject.get("min_10")),
                    String.valueOf(menu_jobject.get("hr_10")), menu_jobject.getString("activity_10"),
                    locationFullName[9],

                    menu_jobject.getString("handle"), menu_jobject.getString("ID"));

            objectArrayList.add(obj);
            saveToDB(obj);

        } catch (MalformedURLException | UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToDB(RecreationObject obj) {
        db.open();
        int itemsToStore = 30;
        if (db.getAllItems().getCount() > itemsToStore) {
            db.resetDB();
        }
        Cursor c = db.getItem(obj.dateM);
        if (!c.moveToFirst()) {
            db.insertItem(obj.creatorID, obj.displayID, obj.dateM,
                    obj.ev_1, obj.min_1, obj.hr_1, obj.activity_1, obj.location_1,
                    obj.ev_2, obj.min_2, obj.hr_2, obj.activity_2, obj.location_2,
                    obj.ev_3, obj.min_3, obj.hr_3, obj.activity_3, obj.location_3,
                    obj.ev_4, obj.min_4, obj.hr_4, obj.activity_4, obj.location_4,
                    obj.ev_5, obj.min_5, obj.hr_5, obj.activity_5, obj.location_5,
                    obj.ev_6, obj.min_6, obj.hr_6, obj.activity_6, obj.location_6,
                    obj.ev_7, obj.min_7, obj.hr_7, obj.activity_7, obj.location_7,
                    obj.ev_8, obj.min_8, obj.hr_8, obj.activity_8, obj.location_8,
                    obj.ev_9, obj.min_9, obj.hr_9, obj.activity_9, obj.location_9,
                    obj.ev_10, obj.min_10, obj.hr_10, obj.activity_10, obj.location_10,
                    obj.handle, obj.recId);
        } else {
            db.updateItem(obj.creatorID, obj.displayID, obj.dateM,
                    obj.ev_1, obj.min_1, obj.hr_1, obj.activity_1, obj.location_1,
                    obj.ev_2, obj.min_2, obj.hr_2, obj.activity_2, obj.location_2,
                    obj.ev_3, obj.min_3, obj.hr_3, obj.activity_3, obj.location_3,
                    obj.ev_4, obj.min_4, obj.hr_4, obj.activity_4, obj.location_4,
                    obj.ev_5, obj.min_5, obj.hr_5, obj.activity_5, obj.location_5,
                    obj.ev_6, obj.min_6, obj.hr_6, obj.activity_6, obj.location_6,
                    obj.ev_7, obj.min_7, obj.hr_7, obj.activity_7, obj.location_7,
                    obj.ev_8, obj.min_8, obj.hr_8, obj.activity_8, obj.location_8,
                    obj.ev_9, obj.min_9, obj.hr_9, obj.activity_9, obj.location_9,
                    obj.ev_10, obj.min_10, obj.hr_10, obj.activity_10, obj.location_10,
                    obj.handle, obj.recId);
        }
        db.close();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.updateUI(objectArrayList);
    }

    interface Communicator {
        //   void updateProgressTo(int progress);
        void updateUI(ArrayList<RecreationObject> menuArrayList);
    }
}