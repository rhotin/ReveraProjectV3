/*package com.ideaone.reveraproject1;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class RecreationFragmentOld extends Fragment {

    String locationSelected = HomeFragment.locationSelected.equalsIgnoreCase("") ?
            "leaside" : HomeFragment.locationSelected;

    int itemsToStore = 30;

    final String URLLegend = "http://revera.mxs-s.com/displays/" + locationSelected + "/legends.json";

    TextView recreation_title, tv_rec;
  //  TextView date;
    long yourmilliseconds;
    Date resultdate, requested_date, resultdate1;
    SimpleDateFormat sdf_clock, sdf_date, sdf, sdf2, sdf3, titleDate;
    String URL;
    GregorianCalendar calendar;
    int dateCounter = 0;
    ImageView calendarSide, next, prev;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener datePick;
    HttpGetTask getAsyncTask;
    DBAdapterRecreation db;

    ArrayList<String> locCodes = new ArrayList<>();
    ArrayList<String> locations = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.recreation_fragment, container, false);
        titleDate = new SimpleDateFormat("EEE (MMM dd)");

        db = new DBAdapterRecreation(getActivity().getBaseContext());

        calendarSide = (ImageView) V.findViewById(R.id.menu_side_show_calendar);
        myCalendar = Calendar.getInstance();
        datePick = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                getRecreationForChosenDate(myCalendar);
            }

        };

        calendarSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), datePick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        recreation_title = (TextView) V.findViewById(R.id.recreation_view_title);
        tv_rec = (TextView) V.findViewById(R.id.tvRecreation);

        sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, dateCounter);
        requested_date = calendar.getTime();
        URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/recreation.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
        Log.e("RecreationURL", "" + URL);
        if (isNetworkAvailable()) {
            UInoWifi(sdf_date.format(requested_date));
            getAsyncTask = new HttpGetTask();
            getAsyncTask.execute();
        } else {
            UInoWifi(sdf_date.format(requested_date));
        }

      //  date = (TextView) V.findViewById(R.id.menu_side_date);
        sdf = new SimpleDateFormat("MMM dd");
        sdf2 = new SimpleDateFormat("EEE");
        resultdate1 = calendar.getTime();
        //date.setText(Html.fromHtml("<b>" + sdf2.format(resultdate1) + "<br/>" + sdf.format(resultdate1) + "</b>"));
   //     date.setText(Html.fromHtml("<b>RECREATION</b>"));
   //     date.setGravity(Gravity.CENTER);
      //  date.setTextSize(25);
        sdf3 = new SimpleDateFormat("EEEE");
        recreation_title.setText(Html.fromHtml("<b>Recreation</b>" + "<b>" + " for " + "Today" + "</b>"));

        prev = (ImageView) V.findViewById(R.id.menu_side_prev_day_button);
        prev.setImageDrawable(getResources().getDrawable(R.drawable.side_left_arrow));

        //next prev button
        next = (ImageView) V.findViewById(R.id.menu_side_next_day_button);
        next.setImageDrawable(getResources().getDrawable(R.drawable.side_right_arrow));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setImageDrawable(getResources().getDrawable(R.drawable.side_right_arrow));
                prev.setImageDrawable(getResources().getDrawable(R.drawable.side_left_arrow));
                tv_rec.setText(R.string.recreationLoading);
                calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, ++dateCounter);
                requested_date = calendar.getTime();
                URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/recreation.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
                resultdate1 = calendar.getTime();
                if (dateCounter - 1 == -1) {
                    recreation_title.setText(Html.fromHtml("<b>Recreation</b>" + "<b>" + " for " + "Today" + "</b>"));
                } else {
                    sdf3 = new SimpleDateFormat("EEEE");
                    recreation_title.setText(Html.fromHtml("<b>Recreation</b>" + "<b>" + " for " + sdf3.format(resultdate1) + " (" + sdf.format(resultdate1) + ")</b>"));
                }
                //date.setText(Html.fromHtml("<b>" + sdf2.format(resultdate1) + "<br/>" + sdf.format(resultdate1) + "</b>"));
       //         date.setText(Html.fromHtml("<b>RECREATION</b>"));
       //         date.setGravity(Gravity.CENTER);
       //         date.setTextSize(25);
                UInoWifi(sdf_date.format(requested_date));
                if (isNetworkAvailable()) {
                    try {
                        getAsyncTask.cancel(true);
                        getAsyncTask = new HttpGetTask();
                        getAsyncTask.execute();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    UInoWifi(sdf_date.format(requested_date));
                }
                hideNavBar();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_rec.setText(R.string.recreationLoading);
                calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, --dateCounter);
                requested_date = calendar.getTime();
                URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/recreation.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
                resultdate1 = calendar.getTime();
                if (dateCounter - 1 == -1) {
                    recreation_title.setText(Html.fromHtml("<b>Recreation</b>" + "<b>" + " for " + "Today" + "</b>"));
                } else {
                    sdf3 = new SimpleDateFormat("EEEE");
                    recreation_title.setText(Html.fromHtml("<b>Recreation</b>" + "<b>" + " for " + sdf3.format(resultdate1) + " (" + sdf.format(resultdate1) + ")</b>"));
                }
                //date.setText(Html.fromHtml("<b>" + sdf2.format(resultdate1) + "<br/>" + sdf.format(resultdate1) + "</b>"));
        //        date.setText(Html.fromHtml("<b>RECREATION</b>"));
        //        date.setGravity(Gravity.CENTER);
        //        date.setTextSize(25);
                UInoWifi(sdf_date.format(requested_date));
                if (isNetworkAvailable()) {
                    try {
                        getAsyncTask.cancel(true);
                        getAsyncTask = new HttpGetTask();
                        getAsyncTask.execute();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    UInoWifi(sdf_date.format(requested_date));
                }
                hideNavBar();
            }
        });

        ImageView calendar_image_view = (ImageView) V.findViewById(R.id.menu_side_show_calendar);
        calendar_image_view.setImageDrawable(getResources().getDrawable(R.drawable.side_calendar));

        return V;
    }

    private void getRecreationForChosenDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat date1 = new SimpleDateFormat("MMM dd");
        SimpleDateFormat day1 = new SimpleDateFormat("EEEE");
        URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/recreation.json?date=" + sdf.format(calendar.getTime()) + "&nohtml=1";
        tv_rec.setText(R.string.recreationLoading);
        recreation_title.setText(Html.fromHtml("<b>Recreation</b>" + "<b>" + " for " + day1.format(calendar.getTime()) + " (" + date1.format(calendar.getTime()) + ")</b>"));
        //date.setText(Html.fromHtml("<b>" + day1.format(calendar.getTime()) + "<br/>" + date1.format(calendar.getTime()) + "</b>"));
  //      date.setText(Html.fromHtml("<b>RECREATION</b>"));
  //      date.setGravity(Gravity.CENTER);
  //      date.setTextSize(25);
        next.setImageDrawable(getResources().getDrawable(R.drawable.today));
        prev.setImageDrawable(null);
        dateCounter = -1;
        UInoWifi(sdf_date.format(requested_date));
        if (isNetworkAvailable()) {
            try {
                getAsyncTask.cancel(true);
                getAsyncTask = new HttpGetTask();
                getAsyncTask.execute();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            UInoWifi(sdf_date.format(requested_date));
        }
        hideNavBar();
    }

    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

        String URLLeg = URLLegend;
        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                //////Updated/////
                java.net.URL URLLegend = new URL(URLLeg);
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(URLLegend.openConnection().getInputStream(), "UTF-8"));
                String location_json = reader.readLine();

                JSONObject location_object = new JSONObject(location_json);
                JSONArray data_arr = location_object.getJSONArray("data");

                int totalLocations = data_arr.length();
                for (int i = 0; i < totalLocations; i++) {
                    locCodes.add(data_arr.getJSONObject(i).getString("code"));
                    locations.add(data_arr.getJSONObject(i).getString("location"));
                }
                /////End Updated///
                return mClient.execute(request, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (null != mClient)
                mClient.close();
            UIupdate(result);
        }
    }

    private class JSONResponseHandler implements ResponseHandler<List<String>> {

        private static final String RECREATION_TAG = "recreation";
        private final String[] EVENT_TAG = new String[10];
        private final String[] ACTIVITY_TAG = new String[10];
        private final String[] LOCATION_TAG = new String[10];
        private final String[] HOUR_TAG = new String[10];
        private final String[] MIN_TAG = new String[10];

        String[] locationFullName = new String[10];

        @Override
        public List<String> handleResponse(HttpResponse response)
                throws IOException {
            List<String> result = new ArrayList<String>();
            String JSONResponse = new BasicResponseHandler().handleResponse(response);
            try {
                // Get top-level JSON Object - a Map
                JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
                // Get single data - a Map
                JSONObject menu_jobject = responseObject.getJSONObject(RECREATION_TAG);
                //Pulling menu
                String hr_temp, min_temp;

                for (int i = 0; i < 10; i++) {
                    EVENT_TAG[i] = "ev_" + (i + 1);
                    LOCATION_TAG[i] = "location_" + (i + 1);
                    ACTIVITY_TAG[i] = "activity_" + (i + 1);
                    HOUR_TAG[i] = "hr_" + (i + 1);
                    MIN_TAG[i] = "min_" + (i + 1);

                    if (menu_jobject.get(HOUR_TAG[i]).toString().length() == 1) {
                        hr_temp = "0" + menu_jobject.get(HOUR_TAG[i]).toString();
                    } else {
                        hr_temp = menu_jobject.get(HOUR_TAG[i]).toString();
                    }
                    if (menu_jobject.get(MIN_TAG[i]).toString().length() == 1) {
                        min_temp = "0" + menu_jobject.get(MIN_TAG[i]).toString();
                    } else {
                        min_temp = menu_jobject.get(MIN_TAG[i]).toString();
                    }
                    result.add(hr_temp + ":" + min_temp);
                    result.add((String) menu_jobject.get(ACTIVITY_TAG[i]));

                    int locIndex = locCodes.indexOf(menu_jobject.get(LOCATION_TAG[i]).toString());
                    if (locIndex < 0) {
                        result.add("");
                        locationFullName[i] = "";
                    } else {
                        result.add(locations.get(locIndex));
                        locationFullName[i] = locations.get(locIndex);
                    }
                    result.add(menu_jobject.get(EVENT_TAG[i]).toString());
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
                db.open();
                if (db.getAllItems().getCount() > itemsToStore) {
                    db.resetDB();
                }
                Cursor c = db.getItem(obj.dateM);
                if (!c.moveToFirst()) {
                    Log.e("menu1", "insert rec");
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
                    Log.e("menu1", "update rec");
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
               // dbAlb.close();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private void UInoWifi(String mDate) {
        List<String> result = new ArrayList<>();
        final String[] EVENT_TAG = new String[10];
        final String[] ACTIVITY_TAG = new String[10];
        final String[] LOCATION_TAG = new String[10];
        final String[] HOUR_TAG = new String[10];
        final String[] MIN_TAG = new String[10];
        db.open();
        Cursor c = db.getItem(mDate);
        if (c.moveToFirst()) {
            String hr_temp, min_temp;

            for (int i = 0; i < 10; i++) {
                EVENT_TAG[i] = "event" + (i + 1);
                MIN_TAG[i] = "min" + (i + 1);
                HOUR_TAG[i] = "hr" + (i + 1);
                ACTIVITY_TAG[i] = "activity" + (i + 1);
                LOCATION_TAG[i] = "location" + (i + 1);

                if (String.valueOf(c.getInt(c.getColumnIndex(HOUR_TAG[i]))).length() == 1) {
                    hr_temp = "0" + String.valueOf(c.getInt(c.getColumnIndex(HOUR_TAG[i])));
                } else {
                    hr_temp = String.valueOf(c.getInt(c.getColumnIndex(HOUR_TAG[i])));
                }
                if (String.valueOf(c.getInt(c.getColumnIndex(MIN_TAG[i]))).length() == 1) {
                    min_temp = "0" + String.valueOf(c.getInt(c.getColumnIndex(MIN_TAG[i])));
                } else {
                    min_temp = String.valueOf(c.getInt(c.getColumnIndex(MIN_TAG[i])));
                }
                result.add(hr_temp + ":" + min_temp);
                result.add(c.getString(c.getColumnIndex(ACTIVITY_TAG[i])));
                result.add(c.getString(c.getColumnIndex(LOCATION_TAG[i])));
                result.add(c.getString(c.getColumnIndex(EVENT_TAG[i])));
            }
        }
       // dbAlb.close();
        UIupdate(result);
    }

    private void UIupdate(List<String> result) {
        if (result == null || result.size() == 0) {
            tv_rec.setText(Html.fromHtml("<b><u>LOADING.....</u></b>"));
        } else {
            int resultItems = 4;
            tv_rec.setText("");
            for (int loopCount = 0; loopCount < result.size() / resultItems; loopCount++) {
                if (result.get((loopCount) * resultItems + 1).length() != 0) {
                    if (!result.get((loopCount) * resultItems + 2).equals("")) {
                        SpannableString s = new SpannableString(Html.fromHtml("<B>" + result.get((loopCount) * resultItems) + "</B>" + getString(R.string.tab) + getString(R.string.tab) + result.get((loopCount) * resultItems + 1) + " - " + result.get((loopCount) * resultItems + 2) + "<br>"));
                        s.setSpan(new android.text.style.LeadingMarginSpan.Standard(0, 100), 0, s.length(), 0);
                        tv_rec.append(s);
                        //tv_rec.append(Html.fromHtml("<p><B>" + result.get((loopCount) * resultItems) + "</B>" + getString(R.string.tab) + getString(R.string.tab) + result.get((loopCount) * resultItems + 1) + " - " + result.get((loopCount) * resultItems + 2) + "<br></p>"));
                    } else {
                        SpannableString s = new SpannableString(Html.fromHtml("<B>" + result.get((loopCount) * resultItems) + "</B>" + getString(R.string.tab) + getString(R.string.tab) + result.get((loopCount) * resultItems + 1) + "<br>"));
                        s.setSpan(new android.text.style.LeadingMarginSpan.Standard(0, 100), 0, s.length(), 0);
                        tv_rec.append(s);
                        //tv_rec.append(Html.fromHtml("<p><B>" + result.get((loopCount) * resultItems) + "</B>" + getString(R.string.tab) + getString(R.string.tab) + result.get((loopCount) * resultItems + 1) + "<br></p>"));
                    }
                }
            }
        }
    }

    public boolean isNetworkAvailable() {
        getActivity().getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void hideNavBar() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        View decorView = getActivity().getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getAsyncTask != null)
            getAsyncTask.cancel(true);
    }

}
*/