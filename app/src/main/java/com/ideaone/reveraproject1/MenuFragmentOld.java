/*package com.ideaone.reveraproject1;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MenuFragmentOld extends Fragment {

    String locationSelected = HomeFragment.locationSelected.equalsIgnoreCase("") ? "leaside" : HomeFragment.locationSelected;

    int itemsToStore = 20;

    //ArrayList<MenuObject> menuArrayList = new ArrayList<>();

    TextView menu_title, menu_lunch, menu_dinner;
    Date resultdate, requested_date, resultdate1;
    SimpleDateFormat sdf_date, sdf, sdf2, sdf3;
    static String URL;
    GregorianCalendar calendar;
    int dateCounter = 0;
    ImageView calendarSide, next, prev;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener datePick;
    MenuDownload getAsyncTask;
    DBAdapterMenu db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.menu_fragment, container, false);

        // final String locationSelected = HomeFragment.locationSelected.equalsIgnoreCase("") ?
        //         getString(R.string.ReveraLocation) : HomeFragment.locationSelected;

        db = new DBAdapterMenu(getActivity().getBaseContext());

        menu_title = (TextView) V.findViewById(R.id.menu_title);
        menu_lunch = (TextView) V.findViewById(R.id.menuLunch);
        menu_dinner = (TextView) V.findViewById(R.id.menuDinner);

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
                getMenuForChosenDate(myCalendar);
            }
        };

        calendarSide.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), datePick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                hideNavBar();
            }
        });

        //UInoWifi(sdf_date.format(requested_date));
        sdf_date = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        calendar = new GregorianCalendar();


        //   date = (TextView) V.findViewById(R.id.menu_side_date);
        sdf = new SimpleDateFormat("MMM dd", Locale.CANADA);
        sdf2 = new SimpleDateFormat("EEE", Locale.CANADA);
        resultdate1 = calendar.getTime();
        //   date.setText(Html.fromHtml("<b>MENU</b>"));
        //  date.setGravity(Gravity.CENTER);
        //  date.setTextSize(35);
        sdf3 = new SimpleDateFormat("EEEE", Locale.CANADA);
        menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + "Today" + "</b>"));
        menu_title.setTextSize(40);
        menu_title.setGravity(Gravity.CENTER);

        prev = (ImageView) V.findViewById(R.id.menu_side_prev_day_button);
        next = (ImageView) V.findViewById(R.id.menu_side_next_day_button);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setImageDrawable(getResources().getDrawable(R.drawable.side_right_arrow));
                prev.setImageDrawable(getResources().getDrawable(R.drawable.side_left_arrow));
                menu_lunch.setText("Loading Menu");
                menu_dinner.setText("");
                calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, ++dateCounter);
                requested_date = calendar.getTime();
                URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/menu.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
                resultdate1 = calendar.getTime();
                //date.setText(Html.fromHtml("<b>" + sdf2.format(resultdate1) + "<br/>" + sdf.format(resultdate1) + "</b>"));
                //     date.setText(Html.fromHtml("<b>MENU</b>"));
                //     date.setGravity(Gravity.CENTER);
                //     date.setTextSize(35);
                if (dateCounter - 1 == -1) {
                    menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + "Today" + "</b>"));
                } else {
                    sdf3 = new SimpleDateFormat("EEEE", Locale.CANADA);
                    menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + sdf3.format(resultdate1) + " (" + sdf.format(resultdate1) + ")</b>"));
                }
                menu_title.setTextSize(40);
                menu_title.setGravity(Gravity.CENTER);

                UInoWifi(sdf_date.format(requested_date));

                if (isNetworkAvailable()) {
                    try {
                        getAsyncTask.cancel(true);
                        getAsyncTask = new MenuDownload();
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
                menu_lunch.setText("Loading Menu");
                menu_dinner.setText("");
                calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, --dateCounter);
                requested_date = calendar.getTime();
                URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/menu.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
                resultdate1 = calendar.getTime();
                //date.setText(Html.fromHtml("<b>" + sdf2.format(resultdate1) + "<br/>" + sdf.format(resultdate1) + "</b>"));
                //    date.setText(Html.fromHtml("<b>MENU</b>"));
                //    date.setGravity(Gravity.CENTER);
                //    date.setTextSize(35);
                if (dateCounter - 1 == -1) {
                    menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + "Today" + "</b>"));
                } else {
                    sdf3 = new SimpleDateFormat("EEEE", Locale.CANADA);
                    menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + sdf3.format(resultdate1) + " (" + sdf.format(resultdate1) + ")</b>"));
                }
                menu_title.setTextSize(40);
                menu_title.setGravity(Gravity.CENTER);

                UInoWifi(sdf_date.format(requested_date));

                if (isNetworkAvailable()) {
                    try {
                        getAsyncTask.cancel(true);
                        getAsyncTask = new MenuDownload();
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

        menu_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //load 3 days at a time if its between 1am - 2am
        if (isNetworkAvailable()) {
            int a = calendar.get(Calendar.AM_PM);
            Log.e("MenuUUUUURL", "" + Calendar.HOUR + "--" + URL);
            if ((calendar.get(Calendar.HOUR_OF_DAY) >= 10)) { //&& (calendar.get(Calendar.HOUR_OF_DAY) < 11) && (a == Calendar.PM)
                for (int i = 0; i < 3; i++) {
                    calendar.add(Calendar.DATE, dateCounter + i);
                    requested_date = calendar.getTime();
                    URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/menu.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
                    getAsyncTask = new MenuDownload();
                    getAsyncTask.execute();
                    Log.e("MenuURL@@@@", "" + calendar.get(Calendar.HOUR_OF_DAY) + "++"+ calendar.get(Calendar.HOUR_OF_DAY) + "--" + URL);
                }
            }
        }

        dateCounter = 0;
        calendar.add(Calendar.DATE, dateCounter);
        requested_date = calendar.getTime();
        URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/menu.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
        Log.e("MenuURL", "" + URL);

        if (isNetworkAvailable()) {
            UInoWifi(sdf_date.format(requested_date));
            getAsyncTask = new MenuDownload();
            getAsyncTask.execute();
        } else {
            UInoWifi(sdf_date.format(requested_date));
        }

        return V;
    }



    private void getMenuForChosenDate(Calendar calendar) {
        hideNavBar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        SimpleDateFormat date1 = new SimpleDateFormat("MMM dd", Locale.CANADA);
        requested_date = calendar.getTime();
        URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/menu.json?date=" + sdf.format(requested_date) + "&nohtml=1";
        menu_lunch.setText("Loading Menu");
        menu_dinner.setText("");
        //   date.setText(Html.fromHtml("<b>MENU</b>"));
        //   date.setGravity(Gravity.CENTER);
        //   date.setTextSize(35);
        sdf3 = new SimpleDateFormat("EEEE", Locale.CANADA);
        menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + sdf3.format(requested_date) + " (" + date1.format(requested_date) + ")</b>"));
        menu_title.setTextSize(40);
        menu_title.setGravity(Gravity.CENTER);
        next.setImageDrawable(getResources().getDrawable(R.drawable.today));
        prev.setImageDrawable(null);
        dateCounter = -1;

        UInoWifi(sdf_date.format(requested_date));

        if (isNetworkAvailable()) {
            try {
                getAsyncTask.cancel(true);
                getAsyncTask = new MenuDownload();
                getAsyncTask.execute();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            UInoWifi(sdf_date.format(requested_date));
        }
    }

    public class MenuDownload extends AsyncTask<Void, Void, List<String>> {

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return mClient.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mClient.close();
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            //if (null != mClient)
            try {
                mClient.close();
                UIupdate(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class JSONResponseHandler implements ResponseHandler<List<String>> {

        private static final String MENU_TAG = "menus";

        private static final String LAUNCH_SOUP_TAG = "lunch_soup";
        private static final String LAUNCH_SALAD_TAG = "lunch_salad";
        private static final String LAUNCH_ENTREE1_TAG = "lunch_entree1";
        private static final String LAUNCH_ENTREE2_TAG = "lunch_entree2";
        private static final String LAUNCH_DESERT_TAG = "lunch_dessert";
        private static final String LAUNCH_OTHER_TAG = "lunch_other";

        private static final String DINNER_SOUP_TAG = "dinner_soup";
        private static final String DINNER_SALAD_TAG = "dinner_salad";
        private static final String DINNER_ENTREE1_TAG = "dinner_entree1";
        private static final String DINNER_ENTREE2_TAG = "dinner_entree2";
        private static final String DINNER_POTATO_TAG = "dinner_potato";
        private static final String DINNER_VEG_TAG = "dinner_veg";
        private static final String DINNER_DESERT_TAG = "dinner_dessert";
        private static final String DINNER_OTHER_TAG = "dinner_other";

        String lunch_soup, lunch_salad, lunch_entree1, lunch_entree2, lunch_dessert, lunch_other;

        String dinner_soup, dinner_salad, dinner_entree1, dinner_entree2, dinner_potato, dinner_veg,
                dinner_dessert, dinner_other;

        @Override
        public List<String> handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            List<String> result = new ArrayList<>();

            String JSONResponse = new BasicResponseHandler()
                    .handleResponse(response);
            try {
                // Get top-level JSON Object - a Map
                JSONObject responseObject = (JSONObject) new JSONTokener(
                        JSONResponse).nextValue();

                // Get single data - a Map
                JSONObject menu_jobject = responseObject.getJSONObject(MENU_TAG);
                //Pulling menu

                if (menu_jobject.has(LAUNCH_SOUP_TAG)) {
                    result.add((String) menu_jobject.get(LAUNCH_SOUP_TAG));
                    lunch_soup = (String) menu_jobject.get(LAUNCH_SOUP_TAG);
                }
                if (menu_jobject.has(LAUNCH_SALAD_TAG)) {
                    result.add((String) menu_jobject.get(LAUNCH_SALAD_TAG));
                    lunch_salad = (String) menu_jobject.get(LAUNCH_SALAD_TAG);
                }
                if (menu_jobject.has(LAUNCH_ENTREE1_TAG)) {
                    result.add((String) menu_jobject.get(LAUNCH_ENTREE1_TAG) + "<br/>" + "<b>or</b>" + "<br/>" + (String) menu_jobject.get(LAUNCH_ENTREE2_TAG));
                    lunch_entree1 = (String) menu_jobject.get(LAUNCH_ENTREE1_TAG);
                    lunch_entree2 = (String) menu_jobject.get(LAUNCH_ENTREE2_TAG);
                }
                if (menu_jobject.has(LAUNCH_DESERT_TAG)) {
                    result.add((String) menu_jobject.get(LAUNCH_DESERT_TAG));
                    lunch_dessert = (String) menu_jobject.get(LAUNCH_DESERT_TAG);
                }
                if (menu_jobject.has(LAUNCH_OTHER_TAG)) {
                    result.add((String) menu_jobject.get(LAUNCH_OTHER_TAG));
                    lunch_other = (String) menu_jobject.get(LAUNCH_OTHER_TAG);
                }

                if (menu_jobject.has(DINNER_SOUP_TAG)) {
                    result.add((String) menu_jobject.get(DINNER_SOUP_TAG));
                    dinner_soup = (String) menu_jobject.get(DINNER_SOUP_TAG);
                }
                if (menu_jobject.has(DINNER_SALAD_TAG)) {
                    result.add((String) menu_jobject.get(DINNER_SALAD_TAG));
                    dinner_salad = (String) menu_jobject.get(DINNER_SALAD_TAG);
                }
                if (menu_jobject.has(DINNER_ENTREE1_TAG)) {
                    result.add((String) menu_jobject.get(DINNER_ENTREE1_TAG) + "<br/>" + "<b>or</b>" + "<br/>" + (String) menu_jobject.get(DINNER_ENTREE2_TAG));
                    dinner_entree1 = (String) menu_jobject.get(DINNER_ENTREE1_TAG);
                    dinner_entree2 = (String) menu_jobject.get(DINNER_ENTREE2_TAG);
                }
                if (menu_jobject.has(DINNER_POTATO_TAG)) {
                    result.add((String) menu_jobject.get(DINNER_POTATO_TAG));
                    dinner_potato = (String) menu_jobject.get(DINNER_POTATO_TAG);
                }
                if (menu_jobject.has(DINNER_VEG_TAG)) {
                    result.add((String) menu_jobject.get(DINNER_VEG_TAG));
                    dinner_veg = (String) menu_jobject.get(DINNER_VEG_TAG);
                }
                if (menu_jobject.has(DINNER_DESERT_TAG)) {
                    result.add((String) menu_jobject.get(DINNER_DESERT_TAG));
                    dinner_dessert = (String) menu_jobject.get(DINNER_DESERT_TAG);
                }
                if (menu_jobject.has(DINNER_OTHER_TAG)) {
                    result.add((String) menu_jobject.get(DINNER_OTHER_TAG));
                    dinner_other = (String) menu_jobject.get(DINNER_OTHER_TAG);
                }
                */

                /** Continue **/
                /*
                MenuObject obj = new MenuObject((String) menu_jobject.get("creatorID"), (String) menu_jobject.get("displayID"),
                        (String) menu_jobject.get("date"), lunch_soup, lunch_salad, lunch_entree1,
                        lunch_entree2, lunch_dessert, lunch_other, dinner_soup, dinner_salad,
                        dinner_entree1, dinner_entree2, dinner_potato, dinner_veg, dinner_dessert,
                        dinner_other, (String) menu_jobject.get("handle"), (String) menu_jobject.get("ID"));

                db.open();
                if (db.getAllItems().getCount() > itemsToStore) {
                    db.resetDB();
                }
                Cursor c = db.getItem(obj.dateM);
                if (!c.moveToFirst()) {
                    Log.e("menu1", "insert Menu");
                    db.insertItem(obj.creatorID, obj.displayID, obj.dateM, obj.lunchSoup, obj.lunchSalad, obj.lunchEntree1, obj.lunchEntree2,
                            obj.lunchDessert, obj.lunchOther, obj.dinnerSoup, obj.dinnerSalad, obj.dinnerEntree1, obj.dinnerEntree2,
                            obj.dinnerPotato, obj.dinnerVeg, obj.dinnerDessert, obj.dinnerOther, obj.handle, obj.menuId);
                } else {
                    Log.e("menu1", "update Menu" + db.updateItem(obj.creatorID, obj.displayID, obj.dateM, obj.lunchSoup, obj.lunchSalad, obj.lunchEntree1, obj.lunchEntree2,
                            obj.lunchDessert, obj.lunchOther, obj.dinnerSoup, obj.dinnerSalad, obj.dinnerEntree1, obj.dinnerEntree2,
                            obj.dinnerPotato, obj.dinnerVeg, obj.dinnerDessert, obj.dinnerOther, obj.handle, obj.menuId)
                    );
                    db.updateItem(obj.creatorID, obj.displayID, obj.dateM, obj.lunchSoup, obj.lunchSalad, obj.lunchEntree1, obj.lunchEntree2,
                            obj.lunchDessert, obj.lunchOther, obj.dinnerSoup, obj.dinnerSalad, obj.dinnerEntree1, obj.dinnerEntree2,
                            obj.dinnerPotato, obj.dinnerVeg, obj.dinnerDessert, obj.dinnerOther, obj.handle, obj.menuId);

                }
                //  dbAlb.close();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public void UInoWifi(String mDate) {
        List<String> result = new ArrayList<>();
        db.open();
        try {
            Cursor c = db.getItem(mDate);
            Log.e("Menu C", "" + c.getCount());
            if (c.moveToFirst()) {
                result.add(c.getString(c.getColumnIndex("lunchSoup")));
                result.add(c.getString(c.getColumnIndex("lunchSalad")));
                result.add(c.getString(c.getColumnIndex("lunchEntree1")) + "<br/>" + "<b>or</b>" + "<br/>" + c.getString(c.getColumnIndex("lunchEntree2")));
                result.add(c.getString(c.getColumnIndex("lunchDessert")));
                result.add(c.getString(c.getColumnIndex("lunchOther")));

                result.add(c.getString(c.getColumnIndex("dinnerSoup")));
                result.add(c.getString(c.getColumnIndex("dinnerSalad")));
                result.add(c.getString(c.getColumnIndex("dinnerEntree1")) + "<br/>" + "<b>or</b>" + "<br/>" + c.getString(c.getColumnIndex("dinnerEntree2")));
                result.add(c.getString(c.getColumnIndex("dinnerPotato")));
                result.add(c.getString(c.getColumnIndex("dinnerVeg")));
                result.add(c.getString(c.getColumnIndex("dinnerDessert")));
                result.add(c.getString(c.getColumnIndex("dinnerOther")));
            }
            //  dbAlb.close();
            UIupdate(result);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void UIupdate(List<String> result) {
        if (result == null || result.size() <= 5) {
            menu_lunch.setText(Html.fromHtml("<b><u>LOADING.....</u></b>"));
        } else {
            List<String> result_lunch = new ArrayList<>();
            // TODO port perry, has no other
            //  for (int i = 0; i < 4; i++) {
            for (int i = 0; i < 5; i++) {
                result_lunch.add(result.get(i));
            }
            menu_lunch.setText(Html.fromHtml("<b><u>LUNCH</u></b><br/><br/>"));
            for (int loopCount = 1; loopCount <= result_lunch.size(); loopCount++) {
                if (!(result_lunch.get(loopCount - 1).equals("") || result_lunch.get(loopCount - 1).equals("--")))
                    menu_lunch.append(Html.fromHtml(result_lunch.get(loopCount - 1) + "<br/>---<br/>"));
            }

            List<String> result_dinner = new ArrayList<>();
            // TODO port perry, has no other
            //   for (int i = 4; i < result.size(); i++) {
            for (int i = 5; i < result.size(); i++) {
                result_dinner.add(result.get(i));
            }

            menu_dinner.setText(Html.fromHtml("<b><u>DINNER</u></b><br/><br/>"));
            for (int loopCount = 1; loopCount <= result_dinner.size(); loopCount++) {
                if (!(result_dinner.get(loopCount - 1).equals("") || result_dinner.get(loopCount - 1).equals("--")))
                    menu_dinner.append(Html.fromHtml(result_dinner.get(loopCount - 1) + "<br/>---<br/>"));
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