package com.ideaone.tabletapp1;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class RecreationFragment extends Fragment implements RecreationDownload.Communicator {

    int itemsToStore = 30;

    static String URLLegend;

    TextView recreation_title, tv_rec;
    //  TextView date;
    long yourmilliseconds;
    Date resultdate, requested_date, resultdate1;
    SimpleDateFormat sdf_clock, sdf_date, sdf, sdf2, sdf3, titleDate;
    static String URL;
    GregorianCalendar calendar;
    static int dateCounter = 0;
    ImageView calendarSide, next, prev;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener datePick;
    RecreationDownload getAsyncTask;
    DBAdapterRecreation db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.recreation_fragment, container, false);

        //if (locationSelected.equals("leaside-14")) {
        //    locationSelected = "leaside";
        // }

        titleDate = new SimpleDateFormat("EEE (MMM dd)");
        URLLegend = "http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/legends.json";

        Log.e("URLLegend", URLLegend);

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
                URL = "http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/recreation.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
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
                        getAsyncTask = new RecreationDownload(RecreationFragment.this);
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
                URL = "http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/recreation.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
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
                        getAsyncTask = new RecreationDownload(RecreationFragment.this);
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

        dateCounter = 0;
        sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, dateCounter);
        requested_date = calendar.getTime();
        URL = "http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/recreation.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";

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

        Log.e("RecreationURL", "" + URL);
        if (isNetworkAvailable()) {
            Log.e("recreation1", "insert rec");
            UInoWifi(sdf_date.format(requested_date));
            getAsyncTask = new RecreationDownload(RecreationFragment.this);
            getAsyncTask.execute();
        } else {
            UInoWifi(sdf_date.format(requested_date));
        }

        return V;
    }

    private void getRecreationForChosenDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat date1 = new SimpleDateFormat("MMM dd");
        SimpleDateFormat day1 = new SimpleDateFormat("EEEE");
        URL = "http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/recreation.json?date=" + sdf.format(calendar.getTime()) + "&nohtml=1";
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
                getAsyncTask = new RecreationDownload(RecreationFragment.this);
                getAsyncTask.execute();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            UInoWifi(sdf_date.format(requested_date));
        }
        hideNavBar();
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

    @Override
    public void updateUI(ArrayList<RecreationObject> result) {
        Log.e("recreation1", "insert rec");

        if (result == null) {
            tv_rec.setText(Html.fromHtml("<b>LOADING......</b>"));
        } else {
            sdf_date = new SimpleDateFormat("yyyy-MM-dd");
            calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, dateCounter);
            requested_date = calendar.getTime();
            UInoWifi(sdf_date.format(requested_date));
        }
    }

    private void UIupdate(List<String> result) {
        if (result == null || result.size() == 0) {
            tv_rec.setText(Html.fromHtml("<b>LOADING.....</b>"));
        } else {
            int resultItems = 4;
            tv_rec.setText("");
            for (int loopCount = 0; loopCount < result.size() / resultItems; loopCount++) {
                if (result.get((loopCount) * resultItems + 1).length() != 0) {
                    if (!result.get((loopCount) * resultItems + 2).equals("")) {
                        SpannableString s = new SpannableString(Html.fromHtml("<B>" + result.get((loopCount) * resultItems) + "</B>" + getString(R.string.tab) + getString(R.string.tab) + result.get((loopCount) * resultItems + 1) + " - " + result.get((loopCount) * resultItems + 2) + "<br>"));
                        s.setSpan(new android.text.style.LeadingMarginSpan.Standard(0, 145), 0, s.length(), 0);
                        tv_rec.append(s);
                        //tv_rec.append(Html.fromHtml("<p><B>" + result.get((loopCount) * resultItems) + "</B>" + getString(R.string.tab) + getString(R.string.tab) + result.get((loopCount) * resultItems + 1) + " - " + result.get((loopCount) * resultItems + 2) + "<br></p>"));
                    } else {
                        SpannableString s = new SpannableString(Html.fromHtml("<B>" + result.get((loopCount) * resultItems) + "</B>" + getString(R.string.tab) + getString(R.string.tab) + result.get((loopCount) * resultItems + 1) + "<br>"));
                        s.setSpan(new android.text.style.LeadingMarginSpan.Standard(0, 145), 0, s.length(), 0);
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