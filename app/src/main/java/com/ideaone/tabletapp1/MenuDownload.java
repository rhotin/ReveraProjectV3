package com.ideaone.tabletapp1;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

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


class MenuDownload extends AsyncTask<Void, Integer, Void> {
    private ArrayList<MenuObject> objectArrayList = new ArrayList<>();
    Communicator context;

    private Boolean flag = true;

    private int daysToSave = 7;

    DBAdapterMenu db;

    MenuDownload(MenuFragment c) {
        this.context = c;
        db = new DBAdapterMenu(c.getActivity().getApplicationContext());
    }

    @Override
    protected Void doInBackground(Void... params) {
        URL theUrl;
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date requested_date;
        /*
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Date requested_date;
        int dateCounter = 0;
        GregorianCalendar calendarDate;
        calendarDate = new GregorianCalendar();
        calendarDate.add(Calendar.DATE, dateCounter);
        requested_date = calendarDate.getTime();
        */

        int dateCounter = MenuFragment.dateCounter;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int am = c.get(Calendar.AM_PM);

        try {
            if (dateCounter == 0 && hour >= 5 && hour <= 6 && am == Calendar.PM && flag) { // and time is between 5 am
                flag = false;
                for (int i = 0; i < daysToSave; i++) {

                    GregorianCalendar calendarDate;
                    calendarDate = new GregorianCalendar();
                    calendarDate.add(Calendar.DATE, i);
                    requested_date = calendarDate.getTime();

                    Log.e("MENU DOWNLOAD", "TEST" + requested_date);
                    theUrl = new URL("http://" + MenuFragment.companySelected + "/displays/" + MenuFragment.locationSelected + "/menu.json?date=" + sdf_date.format(requested_date) + "&nohtml=1");
                    parseJSON(theUrl);
                }
            } else {
                theUrl = new URL(MenuFragment.URL);
                parseJSON(theUrl);
            }
            if (hour > 6) {
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
        try {
            //theUrl = new URL("http://revera.mxs-s.com/displays/" + HomeFragment.locationSelected + "/menu.json?date=" + sdf_date.format(requested_date) + "&nohtml=1");

            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String menu_json = reader.readLine();

            JSONObject menu_object = new JSONObject(menu_json);

            JSONObject data_arr = menu_object.getJSONObject("menus");
            //JSONArray data_arr = menu_object.getJSONArray("menus");

            //int totalMenuItems = data_arr.length();
            //for (int i = 0; i < totalMenuItems; i++) {
            String creatorID, modified, displayID, date, lunchSoup, lunchSalad, lunchEntree1, lunchEntree2,
                    lunchDessert, lunchOther, dinnerSoup, dinnerSalad, dinnerEntree1, dinnerEntree2,
                    dinnerPotato, dinnerVeg, dinnerDessert, dinnerOther, handle, menuID;

            creatorID = modified = displayID = date = lunchSoup = lunchSalad = lunchEntree1 =
                    lunchEntree2 = lunchDessert = lunchOther = dinnerSoup = dinnerSalad =
                            dinnerEntree1 = dinnerEntree2 = dinnerPotato = dinnerVeg = dinnerDessert =
                                    dinnerOther = handle = menuID = "";

            if (data_arr.has("creatorID")) {
                creatorID = data_arr.getString("creatorID");
            }
            if (data_arr.has("modified")) {
                modified = data_arr.getString("modified");
            }
            if (data_arr.has("displayID")) {
                displayID = data_arr.getString("displayID");
            }
            if (data_arr.has("date")) {
                date = data_arr.getString("date");
            }
            if (data_arr.has("lunch_soup")) {
                lunchSoup = data_arr.getString("lunch_soup");
            }
            if (data_arr.has("lunch_salad")) {
                lunchSalad = data_arr.getString("lunch_salad");
            }
            if (data_arr.has("lunch_entree1")) {
                lunchEntree1 = data_arr.getString("lunch_entree1");
            }
            if (data_arr.has("lunch_entree2")) {
                lunchEntree2 = data_arr.getString("lunch_entree2");
            }
            if (data_arr.has("lunch_dessert")) {
                lunchDessert = data_arr.getString("lunch_dessert");
            }
            if (data_arr.has("lunch_other")) {
                lunchOther = data_arr.getString("lunch_other");
            }
            if (data_arr.has("dinner_soup")) {
                dinnerSoup = data_arr.getString("dinner_soup");
            }
            if (data_arr.has("dinner_salad")) {
                dinnerSalad = data_arr.getString("dinner_salad");
            }
            if (data_arr.has("dinner_entree1")) {
                dinnerEntree1 = data_arr.getString("dinner_entree1");
            }
            if (data_arr.has("dinner_entree2")) {
                dinnerEntree2 = data_arr.getString("dinner_entree2");
            }
            if (data_arr.has("dinner_potato")) {
                dinnerPotato = data_arr.getString("dinner_potato");
            }
            if (data_arr.has("dinner_veg")) {
                dinnerVeg = data_arr.getString("dinner_veg");
            }
            if (data_arr.has("dinner_dessert")) {
                dinnerDessert = data_arr.getString("dinner_dessert");
            }
            if (data_arr.has("dinner_other")) {
                dinnerOther = data_arr.getString("dinner_other");
            }
            if (data_arr.has("handle")) {
                handle = data_arr.getString("handle");
            }
            if (data_arr.has("ID")) {
                menuID = data_arr.getString("ID");
            }

            MenuObject obj = new MenuObject(creatorID, modified, displayID,
                    date, lunchSoup, lunchSalad, lunchEntree1, lunchEntree2, lunchDessert, lunchOther,
                    dinnerSoup, dinnerSalad, dinnerEntree1, dinnerEntree2, dinnerPotato, dinnerVeg,
                    dinnerDessert, dinnerOther, handle, menuID);
            objectArrayList.add(obj);
            saveToDB(obj);

        } catch (MalformedURLException | UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToDB(MenuObject result) {
        db.open();
        int itemsToStore = 30;
        if (db.getAllItems().getCount() > itemsToStore) {
            db.resetDB();
        }
        Cursor c = db.getItem(result.dateM);
        if (!c.moveToFirst()) {
            Log.e("menu1", "insert Menu");
            db.insertItem(result.creatorID, result.modified, result.displayID, result.dateM, result.lunchSoup, result.lunchSalad, result.lunchEntree1, result.lunchEntree2,
                    result.lunchDessert, result.lunchOther, result.dinnerSoup, result.dinnerSalad, result.dinnerEntree1, result.dinnerEntree2,
                    result.dinnerPotato, result.dinnerVeg, result.dinnerDessert, result.dinnerOther, result.handle, result.menuId);
        } else {
            Log.e("menu1", "update Menu" + db.updateItem(result.creatorID, result.modified, result.displayID, result.dateM, result.lunchSoup, result.lunchSalad, result.lunchEntree1, result.lunchEntree2,
                    result.lunchDessert, result.lunchOther, result.dinnerSoup, result.dinnerSalad, result.dinnerEntree1, result.dinnerEntree2,
                    result.dinnerPotato, result.dinnerVeg, result.dinnerDessert, result.dinnerOther, result.handle, result.menuId)
            );
            db.updateItem(result.creatorID, result.modified, result.displayID, result.dateM, result.lunchSoup, result.lunchSalad, result.lunchEntree1, result.lunchEntree2,
                    result.lunchDessert, result.lunchOther, result.dinnerSoup, result.dinnerSalad, result.dinnerEntree1, result.dinnerEntree2,
                    result.dinnerPotato, result.dinnerVeg, result.dinnerDessert, result.dinnerOther, result.handle, result.menuId);
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
        void updateUI(ArrayList<MenuObject> menuArrayList);
    }
}