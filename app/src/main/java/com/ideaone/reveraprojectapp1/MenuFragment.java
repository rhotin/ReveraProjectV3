package com.ideaone.reveraprojectapp1;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MenuFragment extends Fragment implements MenuDownload.Communicator {
    //String locationSelected;

    TextView menu_title, menu_lunch, menu_dinner;
    Date resultdate, requested_date, resultdate1;
    SimpleDateFormat sdf_date, sdf, sdf2, sdf3;
    static String URL;
    GregorianCalendar calendar;
    static int dateCounter = 0;
    ImageView calendarSide, next, prev;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener datePick;
    MenuDownload getAsyncTask;
    DBAdapterMenu db;

    ArrayList<MenuObject> menuObjects = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.menu_fragment, container, false);

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

        sdf_date = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        calendar = new GregorianCalendar();

        sdf = new SimpleDateFormat("MMM dd", Locale.CANADA);
        sdf2 = new SimpleDateFormat("EEE", Locale.CANADA);
        resultdate1 = calendar.getTime();
        sdf3 = new SimpleDateFormat("EEEE", Locale.CANADA);
        menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + "Today" + "</b>"));
        //   menu_title.setTextSize(40);
        menu_title.setGravity(Gravity.CENTER);

        prev = (ImageView) V.findViewById(R.id.menu_side_prev_day_button);
        next = (ImageView) V.findViewById(R.id.menu_side_next_day_button);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next.setImageDrawable(getResources().getDrawable(R.drawable.side_right_arrow));
                prev.setImageDrawable(getResources().getDrawable(R.drawable.side_left_arrow));
                menu_lunch.setText(R.string.loadingMenuText);
                menu_dinner.setText("");
                calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, ++dateCounter);
                requested_date = calendar.getTime();
                URL = "http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/menu.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
                resultdate1 = calendar.getTime();
                if (dateCounter - 1 == -1) {
                    menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + "Today" + "</b>"));
                } else {
                    sdf3 = new SimpleDateFormat("EEEE", Locale.CANADA);
                    if (Build.VERSION.SDK_INT >= 24) {
                        menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + sdf3.format(resultdate1) + " (" + sdf.format(resultdate1) + ")</b>", Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + sdf3.format(resultdate1) + " (" + sdf.format(resultdate1) + ")</b>"));
                    }
                }
                //    menu_title.setTextSize(40);
                menu_title.setGravity(Gravity.CENTER);

                UInoWifi(sdf_date.format(requested_date));

                if (isNetworkAvailable()) {
                    try {
                        getAsyncTask.cancel(true);
                        getAsyncTask = new MenuDownload(MenuFragment.this);
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
                menu_lunch.setText(R.string.loadingMenuText);
                menu_dinner.setText("");
                calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, --dateCounter);
                requested_date = calendar.getTime();
                URL = "http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/menu.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
                resultdate1 = calendar.getTime();

                if (dateCounter - 1 == -1) {
                    menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + "Today" + "</b>"));
                } else {
                    sdf3 = new SimpleDateFormat("EEEE", Locale.CANADA);
                    menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + sdf3.format(resultdate1) + " (" + sdf.format(resultdate1) + ")</b>"));
                }
                //   menu_title.setTextSize(40);
                menu_title.setGravity(Gravity.CENTER);

                UInoWifi(sdf_date.format(requested_date));

                if (isNetworkAvailable()) {
                    try {
                        getAsyncTask.cancel(true);
                        getAsyncTask = new MenuDownload(MenuFragment.this);
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
        calendar.add(Calendar.DATE, dateCounter);
        requested_date = calendar.getTime();
        URL = "http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/menu.json?date=" + sdf_date.format(requested_date) + "&nohtml=1";
        Log.e("MenuURL", "" + URL);

        if (isNetworkAvailable()) {
            UInoWifi(sdf_date.format(requested_date));
            getAsyncTask = new MenuDownload(this);
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
        URL = "http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/menu.json?date=" + sdf.format(requested_date) + "&nohtml=1";
        menu_lunch.setText(R.string.loadingMenuText);
        menu_dinner.setText("");
        //   date.setText(Html.fromHtml("<b>MENU</b>"));
        //   date.setGravity(Gravity.CENTER);
        //   date.setTextSize(35);
        sdf3 = new SimpleDateFormat("EEEE", Locale.CANADA);
        menu_title.setText(Html.fromHtml("<b>Menu</b>" + "<b>" + " for " + sdf3.format(requested_date) + " (" + date1.format(requested_date) + ")</b>"));
        //  menu_title.setTextSize(40);
        menu_title.setGravity(Gravity.CENTER);
        next.setImageDrawable(getResources().getDrawable(R.drawable.today));
        prev.setImageDrawable(null);
        dateCounter = -1;

        UInoWifi(sdf_date.format(requested_date));

        if (isNetworkAvailable()) {
            try {
                getAsyncTask.cancel(true);
                getAsyncTask = new MenuDownload(this);
                getAsyncTask.execute();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            UInoWifi(sdf_date.format(requested_date));
        }
    }

    public void UInoWifi(String mDate) {
        //ArrayList<String> result = new ArrayList<>();
        db.open();
        try {
            Cursor c = db.getItem(mDate);
            menuObjects.clear();
            Log.e("Menu C", "" + c.getCount());
            if (c.moveToFirst()) {
                /*result.add(c.getString(c.getColumnIndex("lunchSoup")));
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
                result.add(c.getString(c.getColumnIndex("dinnerOther")));*/

                MenuObject objResult = new MenuObject(c.getString(c.getColumnIndex("creatorID")),
                        c.getString(c.getColumnIndex("modified")),
                        c.getString(c.getColumnIndex("displayID")),
                        c.getString(c.getColumnIndex("date")),
                        c.getString(c.getColumnIndex("lunchSoup")),
                        c.getString(c.getColumnIndex("lunchSalad")),
                        c.getString(c.getColumnIndex("lunchEntree1")),
                        c.getString(c.getColumnIndex("lunchEntree2")),
                        c.getString(c.getColumnIndex("lunchDessert")),
                        c.getString(c.getColumnIndex("lunchOther")),
                        c.getString(c.getColumnIndex("dinnerSoup")),
                        c.getString(c.getColumnIndex("dinnerSalad")),
                        c.getString(c.getColumnIndex("dinnerEntree1")),
                        c.getString(c.getColumnIndex("dinnerEntree2")),
                        c.getString(c.getColumnIndex("dinnerPotato")),
                        c.getString(c.getColumnIndex("dinnerVeg")),
                        c.getString(c.getColumnIndex("dinnerDessert")),
                        c.getString(c.getColumnIndex("dinnerOther")),
                        c.getString(c.getColumnIndex("handle")),
                        c.getString(c.getColumnIndex("menuID")));

                menuObjects.add(objResult);
                updateUI(menuObjects);
            }

        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        // db.close();
    }

    @Override
    public void updateUI(ArrayList<MenuObject> result) {
        try {
            if (result == null) {
                menu_lunch.setText(Html.fromHtml("LOADING....."));
            } else {
                ArrayList<String> result_lunch = new ArrayList<>();
                // TODO port perry, has no other
                if (!(result.get(0).lunchSoup.equals("") || result.get(0).lunchSoup.equals("--")))
                    result_lunch.add(result.get(0).lunchSoup);
                if (!(result.get(0).lunchSalad.equals("") || result.get(0).lunchSalad.equals("--")))
                    result_lunch.add(result.get(0).lunchSalad);
                if (!(result.get(0).lunchEntree1.equals("") || result.get(0).lunchEntree1.equals("--")) &&
                        !(result.get(0).lunchEntree2.equals("") || result.get(0).lunchEntree2.equals("--")))
                    result_lunch.add(result.get(0).lunchEntree1 + "<br/>" + "<b>or</b>" + "<br/>" + result.get(0).lunchEntree2);
                if (!(result.get(0).lunchDessert.equals("") || result.get(0).lunchDessert.equals("--")))
                    result_lunch.add(result.get(0).lunchDessert);
                if (!(result.get(0).lunchOther.equals("") || result.get(0).lunchOther.equals("--")))
                    result_lunch.add(result.get(0).lunchOther);

                Log.e("hello", "kkk-" + result.size());

                menu_lunch.setText(Html.fromHtml("<b><u>LUNCH</u></b><br/><br/>"));
                for (int loopCount = 0; loopCount < result_lunch.size(); loopCount++) {
                    menu_lunch.append(Html.fromHtml(result_lunch.get(loopCount) + "<br/>---<br/>"));
                }

                List<String> result_dinner = new ArrayList<>();

                if (!result.get(0).dinnerSoup.equals(""))
                    result_dinner.add(result.get(0).dinnerSoup);
                if (!result.get(0).dinnerSalad.equals(""))
                    result_dinner.add(result.get(0).dinnerSalad);
                if (!result.get(0).dinnerEntree1.equals("") &&
                        !result.get(0).dinnerEntree2.equals(""))
                    result_dinner.add(result.get(0).dinnerEntree1 + "<br/>" + "<b>or</b>" + "<br/>" + result.get(0).dinnerEntree2);
                if (!result.get(0).dinnerPotato.equals(""))
                    result_dinner.add(result.get(0).dinnerPotato);
                if (!result.get(0).dinnerVeg.equals(""))
                    result_dinner.add(result.get(0).dinnerVeg);
                if (!result.get(0).dinnerDessert.equals(""))
                    result_dinner.add(result.get(0).dinnerDessert);
                if (!result.get(0).dinnerOther.equals(""))
                    result_dinner.add(result.get(0).dinnerOther);

                menu_dinner.setText(Html.fromHtml("<b><u>DINNER</u></b><br/><br/>"));
                for (int loopCount = 1; loopCount <= result_dinner.size(); loopCount++) {
                    if (!(result_dinner.get(loopCount - 1).equals("") || result_dinner.get(loopCount - 1).equals("--")))
                        menu_dinner.append(Html.fromHtml(result_dinner.get(loopCount - 1) + "<br/>---<br/>"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            menu_lunch.setText(Html.fromHtml("LOADING....."));
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
        try {
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
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        db.close();
        if (getAsyncTask != null)
            getAsyncTask.cancel(true);
    }
}