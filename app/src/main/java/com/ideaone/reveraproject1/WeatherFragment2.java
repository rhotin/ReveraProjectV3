package com.ideaone.reveraproject1;


import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment2 extends Fragment implements WeatherDownload2.Communicator {

    //https://api.forecast.io/forecast/95a96c110c829dce3eee6d9b3be63228/43.8267,-79?units=ca

    String locationSelected = HomeFragment.locationSelected;

    public static ArrayList<WeatherObject2> weatherArrayList = new ArrayList<>();

    public static String WEATHER_URL = "";
    public static String WEATHER_URL_CURRENT = "";

    WeatherDBAdapter2 db;

    //   ProgressBar progressBar;
    //  public static TextView messageText;
    //  ViewFlipper mAnnouncementFlipper;
    WeatherDownload2 downloadWeather;

    int tempMinute = 0;
    int tempHour = 0;

    LinearLayout dayWeather24;
    LinearLayout dayWeather3;

    TextView tempTextView, tempSummaryTextView, tempMaxText, tempMinText, textViewWeather;
    ImageView weatherImageView;
    // ImageView left_arrow, right_arrow;

    TextView timeMornText;
    TextView timeEveText;
    TextView timeNightText;
    TextView tempMornText;
    TextView tempEveText;
    TextView tempNightText;

    TextView timeTextV1, summaryTextV1, tempMaxTextV1, tempMinTextV1;
    ImageView iconImage1;
    TextView timeTextV2, summaryTextV2, tempMaxTextV2, tempMinTextV2;
    ImageView iconImage2;
    TextView timeTextV3, summaryTextV3, tempMaxTextV3, tempMinTextV3;
    ImageView iconImage3;

    ImageView daily_weather, hourly_weather;

    int tilesTitleTextSize = 60;
    int tilesSummaryTextSize = 40;
    int tilesHighTextSize = 40;
    int tilesLowTextSize = 40;

    int tiles24TitleTextSize = 70;
    int tiles24HighTextSize = 50;

    /**
     * Toronto lat =  43.7
     *         lon = -79.42
     *
     * Waterloo lat = 43.43
     *          lon = -80.52
     * */

    String location = "toronto";
    double lat = 43.43;
    double lon = -80.52;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_weather2, container, false);

        db = new WeatherDBAdapter2(getActivity().getApplicationContext());

        try {
           // String locality;
            Geocoder gc = new Geocoder(getActivity().getBaseContext());
            List<Address> list = gc.getFromLocationName(location, 1);
            Address add = list.get(0);
         //   locality = add.getLocality();
         //   Toast.makeText(getActivity().getBaseContext(), locality, Toast.LENGTH_LONG).show();
            lat = add.getLatitude();
            lon = add.getLongitude();
        }catch (IOException e){
            e.printStackTrace();
        }


        WEATHER_URL_CURRENT = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&APPID=de21930f5f309825b403e20c8672b202&units=metric";
        WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon + "&APPID=de21930f5f309825b403e20c8672b202&units=metric&cnt=4";

        dayWeather3 = (LinearLayout) V.findViewById(R.id.dayWeather3);
        dayWeather24 = (LinearLayout) V.findViewById(R.id.dayWeather24);

        daily_weather = (ImageView) V.findViewById(R.id.weather_daily);
        hourly_weather = (ImageView) V.findViewById(R.id.weather_hourly);

        textViewWeather = (TextView) V.findViewById(R.id.textViewWeather);
        tempTextView = (TextView) V.findViewById(R.id.tempTextView);
        tempSummaryTextView = (TextView) V.findViewById(R.id.summaryTextView);
        tempMaxText = (TextView) V.findViewById(R.id.maxTextView);
        tempMinText = (TextView) V.findViewById(R.id.minTextView);


        daily_weather.setImageDrawable(getResources().getDrawable(R.drawable.side5dayselect));
        hourly_weather.setImageDrawable(getResources().getDrawable(R.drawable.side24hr));

        weatherImageView = (ImageView) V.findViewById(R.id.weatherImageView);

        textViewWeather.setTextSize(85 / getResources().getDisplayMetrics().scaledDensity);
        tempTextView.setTextSize(100 / getResources().getDisplayMetrics().scaledDensity);
        tempSummaryTextView.setTextSize(50 / getResources().getDisplayMetrics().scaledDensity);
        tempMaxText.setTextSize(60 / getResources().getDisplayMetrics().scaledDensity);
        tempMinText.setTextSize(60 / getResources().getDisplayMetrics().scaledDensity);

        //Morn
        timeMornText = (TextView) V.findViewById(R.id.timeTextMorn);
        timeMornText.setTextSize(tiles24TitleTextSize / getResources().getDisplayMetrics().scaledDensity);
        tempMornText = (TextView) V.findViewById(R.id.tempMornText);
        tempMornText.setTextSize(tiles24HighTextSize / getResources().getDisplayMetrics().scaledDensity);

        //Eve
        timeEveText = (TextView) V.findViewById(R.id.timeTextEve);
        Log.e("DPIDPIDPPPIDPIDPDI", "" + getResources().getDisplayMetrics().scaledDensity);
        timeEveText.setTextSize(tiles24TitleTextSize / getResources().getDisplayMetrics().scaledDensity);
        tempEveText = (TextView) V.findViewById(R.id.tempEveText);
        tempEveText.setTextSize(tiles24HighTextSize / getResources().getDisplayMetrics().scaledDensity);

        //Night
        timeNightText = (TextView) V.findViewById(R.id.timeTextNight);
        timeNightText.setTextSize(tiles24TitleTextSize / getResources().getDisplayMetrics().scaledDensity);
        tempNightText = (TextView) V.findViewById(R.id.tempNightText);
        tempNightText.setTextSize(tiles24HighTextSize / getResources().getDisplayMetrics().scaledDensity);

        /*
        tiles
         */
        timeTextV1 = (TextView) V.findViewById(R.id.timeTextV1);
        summaryTextV1 = (TextView) V.findViewById(R.id.summaryTextV1);
        tempMaxTextV1 = (TextView) V.findViewById(R.id.tempMaxTextV1);
        tempMinTextV1 = (TextView) V.findViewById(R.id.tempMinTextV1);
        iconImage1 = (ImageView) V.findViewById(R.id.iconImage1);

        timeTextV1.setTextSize(tilesTitleTextSize / getResources().getDisplayMetrics().scaledDensity);
        summaryTextV1.setTextSize(tilesSummaryTextSize / getResources().getDisplayMetrics().scaledDensity);
        tempMaxTextV1.setTextSize(tilesHighTextSize / getResources().getDisplayMetrics().scaledDensity);
        tempMinTextV1.setTextSize(tilesLowTextSize / getResources().getDisplayMetrics().scaledDensity);
// 2
        timeTextV2 = (TextView) V.findViewById(R.id.timeTextV2);
        summaryTextV2 = (TextView) V.findViewById(R.id.summaryTextV2);
        tempMaxTextV2 = (TextView) V.findViewById(R.id.tempMaxTextV2);
        tempMinTextV2 = (TextView) V.findViewById(R.id.tempMinTextV2);
        iconImage2 = (ImageView) V.findViewById(R.id.iconImage2);

        timeTextV2.setTextSize(tilesTitleTextSize / getResources().getDisplayMetrics().scaledDensity);
        summaryTextV2.setTextSize(tilesSummaryTextSize / getResources().getDisplayMetrics().scaledDensity);
        tempMaxTextV2.setTextSize(tilesHighTextSize / getResources().getDisplayMetrics().scaledDensity);
        tempMinTextV2.setTextSize(tilesLowTextSize / getResources().getDisplayMetrics().scaledDensity);
        //3
        timeTextV3 = (TextView) V.findViewById(R.id.timeTextV3);
        summaryTextV3 = (TextView) V.findViewById(R.id.summaryTextV3);
        tempMaxTextV3 = (TextView) V.findViewById(R.id.tempMaxTextV3);
        tempMinTextV3 = (TextView) V.findViewById(R.id.tempMinTextV3);
        iconImage3 = (ImageView) V.findViewById(R.id.iconImage3);

        timeTextV3.setTextSize(tilesTitleTextSize / getResources().getDisplayMetrics().scaledDensity);
        summaryTextV3.setTextSize(tilesSummaryTextSize / getResources().getDisplayMetrics().scaledDensity);
        tempMaxTextV3.setTextSize(tilesHighTextSize / getResources().getDisplayMetrics().scaledDensity);
        tempMinTextV3.setTextSize(tilesLowTextSize / getResources().getDisplayMetrics().scaledDensity);


        Log.e("density", "" + getResources().getDisplayMetrics().scaledDensity);

       /* db.open();
        Cursor cur = db.getAllItems();
        if (cur.moveToFirst()) {
            getAllWeatherDays();
        }
        db.close();*/

        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);

        if (isNetworkAvailable()) {
            if (hour != tempHour || minute <= (tempMinute - 9) || minute > (tempMinute + 9)) {
                tempMinute = minute;
                tempHour = hour;
                downloadWeather = new WeatherDownload2(this);
                downloadWeather.execute();
            }
        }
        db.open();
        Cursor cur = db.getAllItems();
        if (cur.moveToFirst()) {
            getAllWeatherDays();
        }
        db.close();

        daily_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily_weather.setImageDrawable(getResources().getDrawable(R.drawable.side5dayselect));
                hourly_weather.setImageDrawable(getResources().getDrawable(R.drawable.side24hr));
                dayWeather3.setVisibility(View.VISIBLE);
                dayWeather24.setVisibility(View.GONE);
            }
        });
        hourly_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily_weather.setImageDrawable(getResources().getDrawable(R.drawable.side5day));
                hourly_weather.setImageDrawable(getResources().getDrawable(R.drawable.side24hrselect));
                dayWeather3.setVisibility(View.GONE);
                dayWeather24.setVisibility(View.VISIBLE);
            }
        });

        return V;
    }

    @Override
    public void updateUI(ArrayList<WeatherObject2> photosArrayList) {
        //photoListView.setVisibility(View.VISIBLE);
        //    progressBar.setVisibility(View.GONE);
        //    messageText.setVisibility(View.VISIBLE);

        //if (isNetworkAvailable()) {
        try {
            if (!photosArrayList.isEmpty()) {
                WeatherObject2 obj1;
                obj1 = photosArrayList.get(0);
                if (!obj1.m1Summary.isEmpty()) {
                    db.open();
                    db.resetDB();
                    db.close();
                    for (int i = 0; i < photosArrayList.size(); i++) {
                        //  This will create dynamic image view and add them to ViewFlipper
                        WeatherObject2 obj = photosArrayList.get(i);
                        //  setFlipperImage(obj);
                        saveToWeather(obj);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        //}
        getAllWeatherDays();
    }

    private void setFlipperImage(WeatherObject2 temp) {
        tempTextView.setText(new StringBuilder().append((int) temp.m1Temperature).append("\u00B0c").toString());
        tempSummaryTextView.setText(temp.m1SummaryC.substring(0, 1).toUpperCase() + temp.m1SummaryC.substring(1));
        tempMinText.setText(new StringBuilder().append((int) temp.m1TemperatureMin).append("\u00B0c / ").toString());
        tempMaxText.setText(new StringBuilder().append((int) temp.m1TemperatureMax).append("\u00B0c").toString());

        //    TopLogoFragment.weather.setText(new StringBuilder().append((int) temp.m1Temperature).append("\u00B0c").toString());
        /*
            tiles
         */

        //morn
        tempMornText.setText(new StringBuilder().append((int) temp.m1TemperatureMorn).append("\u00B0c").toString());
        //eve
        tempEveText.setText(new StringBuilder().append((int) temp.m1TemperatureEve).append("\u00B0c").toString());
        //night
        tempNightText.setText(new StringBuilder().append((int) temp.m1TemperatureNight).append("\u00B0c").toString());

        /*
        //4
        long unixSeconds4 = temp.m4Time;
        Date date4 = new Date(unixSeconds4 * 1000L); // *1000 is to convert seconds to milliseconds
        String formattedDate4 = sdf.format(date4);
        timeTextV4.setText(formattedDate4);
        summaryTextV4.setText(temp.m4Icon);
        tempMaxTextV4.setText(new StringBuilder().append((int) temp.m4TemperatureMax).append("\u00B0c").toString());
        tempMinTextV4.setText(new StringBuilder().append((int) temp.m4TemperatureMin).append("\u00B0c").toString());
        windSpeedTextV4.setText(new StringBuilder().append(temp.m4WindSpeed).append(" km/h").toString());
        //5
        long unixSeconds5 = temp.m5Time;
        Date date5 = new Date(unixSeconds5 * 1000L); // *1000 is to convert seconds to milliseconds
        String formattedDate5 = sdf.format(date5);
        timeTextV5.setText(formattedDate5);
        summaryTextV5.setText(temp.m5Icon);
        tempMaxTextV5.setText(new StringBuilder().append((int) temp.m5TemperatureMax).append("\u00B0c").toString());
        tempMinTextV5.setText(new StringBuilder().append((int) temp.m5TemperatureMin).append("\u00B0c").toString());
        windSpeedTextV5.setText(new StringBuilder().append(temp.m5WindSpeed).append(" km/h").toString());
        //6
        long unixSeconds6 = temp.m6Time;
        Date date6 = new Date(unixSeconds6 * 1000L); // *1000 is to convert seconds to milliseconds
        String formattedDate6 = sdf.format(date6);
        timeTextV6.setText(formattedDate6);
        summaryTextV6.setText(temp.m6Icon);
        tempMaxTextV6.setText(new StringBuilder().append((int) temp.m6TemperatureMax).append("\u00B0c").toString());
        tempMinTextV6.setText(new StringBuilder().append((int) temp.m6TemperatureMin).append("\u00B0c").toString());
        windSpeedTextV6.setText(new StringBuilder().append(temp.m6WindSpeed).append(" km/h").toString());
*/
// 1
        long unixSeconds = temp.m1Time;
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CANADA); // the format of your date
        String formattedDate = sdf.format(date);
        timeTextV1.setText(formattedDate);
        summaryTextV1.setText(temp.m1Summary);
        tempMaxTextV1.setText(new StringBuilder().append((int) temp.m1TemperatureMax).append("\u00B0c").toString());
        tempMinTextV1.setText(new StringBuilder().append((int) temp.m1TemperatureMin).append("\u00B0c").toString());
        //    windSpeedTextV1.setText(new StringBuilder().append(temp.m1WindSpeed).append(" km/h").toString());
// 2
        long unixSeconds2 = temp.m2Time;
        Date date2 = new Date(unixSeconds2 * 1000L); // *1000 is to convert seconds to milliseconds
        String formattedDate2 = sdf.format(date2);
        timeTextV2.setText(formattedDate2);
        summaryTextV2.setText(temp.m2Summary);
        tempMaxTextV2.setText(new StringBuilder().append((int) temp.m2TemperatureMax).append("\u00B0c").toString());
        tempMinTextV2.setText(new StringBuilder().append((int) temp.m2TemperatureMin).append("\u00B0c").toString());
        //    windSpeedTextV2.setText(new StringBuilder().append(temp.m2WindSpeed).append(" km/h").toString());
        //3
        long unixSeconds3 = temp.m3Time;
        Date date3 = new Date(unixSeconds3 * 1000L); // *1000 is to convert seconds to milliseconds
        String formattedDate3 = sdf.format(date3);
        timeTextV3.setText(formattedDate3);
        summaryTextV3.setText(temp.m3Summary);
        tempMaxTextV3.setText(new StringBuilder().append((int) temp.m3TemperatureMax).append("\u00B0c").toString());
        tempMinTextV3.setText(new StringBuilder().append((int) temp.m3TemperatureMin).append("\u00B0c").toString());
        //    windSpeedTextV3.setText(new StringBuilder().append(temp.m3WindSpeed).append(" km/h").toString());

        setImage(temp.m1IconC, temp.m1Icon, temp.m2Icon, temp.m3Icon);
    }

    public void setImage(String t1IconC, String t1Icon, String t2Icon, String t3Icon) {

        if (t1IconC.equalsIgnoreCase("01d")) {
            weatherImageView.setImageResource(R.drawable.sunny);
            //       TopLogoFragment.weatherIcon.setImageResource(R.drawable.sunny);
        } else if (t1IconC.equalsIgnoreCase("01n")) {
            weatherImageView.setImageResource(R.drawable.clear_night);
            //       TopLogoFragment.weatherIcon.setImageResource(R.drawable.clear_night);
        } else if (t1IconC.equalsIgnoreCase("02d") || t1IconC.equalsIgnoreCase("02n")) {
            weatherImageView.setImageResource(R.drawable.partly_cloudy);
            //       TopLogoFragment.weatherIcon.setImageResource(R.drawable.partly_cloudy);
        } else if (t1IconC.equalsIgnoreCase("03d") || t1IconC.equalsIgnoreCase("04d") ||
                t1IconC.equalsIgnoreCase("03n") || t1IconC.equalsIgnoreCase("04n")) {
            weatherImageView.setImageResource(R.drawable.cloudy);
            //       TopLogoFragment.weatherIcon.setImageResource(R.drawable.cloudy);
        } else if (t1IconC.equalsIgnoreCase("09d") || t1IconC.equalsIgnoreCase("10d") ||
                t1IconC.equalsIgnoreCase("09n") || t1IconC.equalsIgnoreCase("10n")) {
            weatherImageView.setImageResource(R.drawable.rainy);
            //       TopLogoFragment.weatherIcon.setImageResource(R.drawable.rainy);
        } else if (t1IconC.equalsIgnoreCase("11d") || t1IconC.equalsIgnoreCase("11n")) {
            weatherImageView.setImageResource(R.drawable.thunder);
            //      TopLogoFragment.weatherIcon.setImageResource(R.drawable.thunder);
        } else if (t1IconC.equalsIgnoreCase("13d") || t1IconC.equalsIgnoreCase("13n")) {
            weatherImageView.setImageResource(R.drawable.snowy);
            //     TopLogoFragment.weatherIcon.setImageResource(R.drawable.snowy);
        } else if (t1IconC.equalsIgnoreCase("50d") || t1IconC.equalsIgnoreCase("50n")) {
            weatherImageView.setImageResource(R.drawable.foggy);
            //    TopLogoFragment.weatherIcon.setImageResource(R.drawable.foggy);
        } else {
            weatherImageView.setImageResource(R.drawable.weather);
            //      TopLogoFragment.weatherIcon.setImageResource(R.drawable.weather);
        }

        if (t1Icon.equalsIgnoreCase("01d") || t1Icon.equalsIgnoreCase("01n")) {
            iconImage1.setImageResource(R.drawable.sunny);
        } else if (t1Icon.equalsIgnoreCase("02d") || t1Icon.equalsIgnoreCase("02n")) {
            iconImage1.setImageResource(R.drawable.partly_cloudy);
        } else if (t1Icon.equalsIgnoreCase("03d") || t1Icon.equalsIgnoreCase("04d") ||
                t1Icon.equalsIgnoreCase("03n") || t1Icon.equalsIgnoreCase("04n")) {
            iconImage1.setImageResource(R.drawable.cloudy);
        } else if (t1Icon.equalsIgnoreCase("09d") || t1Icon.equalsIgnoreCase("10d") ||
                t1Icon.equalsIgnoreCase("09n") || t1Icon.equalsIgnoreCase("10n")) {
            iconImage1.setImageResource(R.drawable.rainy);
        } else if (t1Icon.equalsIgnoreCase("11d") || t1Icon.equalsIgnoreCase("11n")) {
            iconImage1.setImageResource(R.drawable.thunder);
        } else if (t1Icon.equalsIgnoreCase("13d") || t1Icon.equalsIgnoreCase("13n")) {
            iconImage1.setImageResource(R.drawable.snowy);
        } else if (t1Icon.equalsIgnoreCase("50d") || t1Icon.equalsIgnoreCase("50n")) {
            iconImage1.setImageResource(R.drawable.foggy);
        } else {
            iconImage1.setImageResource(R.drawable.weather);
        }

        if (t2Icon.equalsIgnoreCase("01d") || t2Icon.equalsIgnoreCase("01n")) {
            iconImage2.setImageResource(R.drawable.sunny);
        } else if (t2Icon.equalsIgnoreCase("02d") || t2Icon.equalsIgnoreCase("02n")) {
            iconImage2.setImageResource(R.drawable.partly_cloudy);
        } else if (t2Icon.equalsIgnoreCase("03d") || t2Icon.equalsIgnoreCase("04d") ||
                t2Icon.equalsIgnoreCase("03n") || t2Icon.equalsIgnoreCase("04n")) {
            iconImage2.setImageResource(R.drawable.cloudy);
        } else if (t2Icon.equalsIgnoreCase("09d") || t2Icon.equalsIgnoreCase("10d") ||
                t2Icon.equalsIgnoreCase("09n") || t2Icon.equalsIgnoreCase("10n")) {
            iconImage2.setImageResource(R.drawable.rainy);
        } else if (t2Icon.equalsIgnoreCase("11d") || t2Icon.equalsIgnoreCase("11n")) {
            iconImage2.setImageResource(R.drawable.thunder);
        } else if (t2Icon.equalsIgnoreCase("13d") || t2Icon.equalsIgnoreCase("13n")) {
            iconImage2.setImageResource(R.drawable.snowy);
        } else if (t2Icon.equalsIgnoreCase("50d") || t2Icon.equalsIgnoreCase("50n")) {
            iconImage2.setImageResource(R.drawable.foggy);
        } else {
            iconImage2.setImageResource(R.drawable.weather);
        }

        if (t3Icon.equalsIgnoreCase("01d") || t3Icon.equalsIgnoreCase("01n")) {
            iconImage3.setImageResource(R.drawable.sunny);
        } else if (t3Icon.equalsIgnoreCase("02d") || t3Icon.equalsIgnoreCase("02n")) {
            iconImage3.setImageResource(R.drawable.partly_cloudy);
        } else if (t3Icon.equalsIgnoreCase("03d") || t3Icon.equalsIgnoreCase("04d") ||
                t3Icon.equalsIgnoreCase("03n") || t3Icon.equalsIgnoreCase("04n")) {
            iconImage3.setImageResource(R.drawable.cloudy);
        } else if (t3Icon.equalsIgnoreCase("09d") || t3Icon.equalsIgnoreCase("10d") ||
                t3Icon.equalsIgnoreCase("09n") || t3Icon.equalsIgnoreCase("10n")) {
            iconImage3.setImageResource(R.drawable.rainy);
        } else if (t3Icon.equalsIgnoreCase("11d") || t3Icon.equalsIgnoreCase("11n")) {
            iconImage3.setImageResource(R.drawable.thunder);
        } else if (t3Icon.equalsIgnoreCase("13d") || t3Icon.equalsIgnoreCase("13n")) {
            iconImage3.setImageResource(R.drawable.snowy);
        } else if (t3Icon.equalsIgnoreCase("50d") || t3Icon.equalsIgnoreCase("50n")) {
            iconImage3.setImageResource(R.drawable.foggy);
        } else {
            iconImage3.setImageResource(R.drawable.weather);
        }
    }

    public void saveToWeather(WeatherObject2 obj) {

        Log.e("item option", "Nothidsafdsafsadfasng!" + obj.location);
        if (!obj.location.isEmpty()) {
            db.open();
            Cursor c = db.getItem(obj.latitude, obj.longitude, obj.m1Temperature);
            if (c.moveToFirst()) {
                //db.deleteItem(imgStream, obj.timeCreated, obj.durationTime, obj.zone, obj.displayAnnounce,
                //       obj.displayHome, obj.displayOther, obj.startDate, obj.endDate);
                //Log.e("item option", "DELETED!");
                Log.e("item option", "Nothing!");
            } else {
                db.insertItem(obj.latitude, obj.longitude, obj.location,
                        obj.m1Time, obj.m1SummaryC, obj.m1IconC, obj.m1Summary, obj.m1Icon, obj.m1Temperature, obj.m1TemperatureMin,
                        obj.m1TemperatureMax, obj.m1TemperatureNight, obj.m1TemperatureEve, obj.m1TemperatureMorn,
                        obj.m2Time, obj.m2Summary, obj.m2Icon, obj.m2Temperature, obj.m2TemperatureMin,
                        obj.m2TemperatureMax, obj.m2TemperatureNight, obj.m2TemperatureEve, obj.m2TemperatureMorn,
                        obj.m3Time, obj.m3Summary, obj.m3Icon, obj.m3Temperature, obj.m3TemperatureMin,
                        obj.m3TemperatureMax, obj.m3TemperatureNight, obj.m3TemperatureEve, obj.m3TemperatureMorn,
                        obj.m4Time, obj.m4Summary, obj.m4Icon, obj.m4Temperature, obj.m4TemperatureMin,
                        obj.m4TemperatureMax, obj.m4TemperatureNight, obj.m4TemperatureEve, obj.m4TemperatureMorn);
                Log.e("item option", obj.location + "-ADDED!");
            }
            db.close();
        }
    }

    public void getAllWeatherDays() {
        db.open();
        Cursor c = db.getAllItems();
        if (c.moveToFirst()) {
            weatherArrayList.clear();
            do {
                double mLat = c.getDouble(1);
                double mLong = c.getDouble(2);
                String mLocation = c.getString(3);
                int m1Time = c.getInt(4);
                String m1SummaryC = c.getString(5);
                String m1IconC = c.getString(6);
                String m1Summary = c.getString(7);
                String m1Icon = c.getString(8);
                double m1Temp = c.getDouble(9);
                double m1TempMin = c.getDouble(10);
                double m1TempMax = c.getDouble(11);
                double m1TempNight = c.getDouble(12);
                double m1TempEve = c.getDouble(13);
                double m1TempMorn = c.getDouble(14);
                int m2Time = c.getInt(15);
                String m2Summary = c.getString(16);
                String m2Icon = c.getString(17);
                double m2Temp = c.getDouble(18);
                double m2TempMin = c.getDouble(19);
                double m2TempMax = c.getDouble(20);
                double m2TempNight = c.getDouble(21);
                double m2TempEve = c.getDouble(22);
                double m2TempMorn = c.getDouble(23);
                int m3Time = c.getInt(24);
                String m3Summary = c.getString(25);
                String m3Icon = c.getString(26);
                double m3Temp = c.getDouble(27);
                double m3TempMin = c.getDouble(28);
                double m3TempMax = c.getDouble(29);
                double m3TempNight = c.getDouble(30);
                double m3TempEve = c.getDouble(31);
                double m3TempMorn = c.getDouble(32);
                int m4Time = c.getInt(33);
                String m4Summary = c.getString(34);
                String m4Icon = c.getString(35);
                double m4Temp = c.getDouble(36);
                double m4TempMin = c.getDouble(37);
                double m4TempMax = c.getDouble(38);
                double m4TempNight = c.getDouble(39);
                double m4TempEve = c.getDouble(40);
                double m4TempMorn = c.getDouble(41);

                WeatherObject2 obj = new WeatherObject2(mLat, mLong, mLocation, m1Time, m1SummaryC,
                        m1IconC, m1Summary, m1Icon, m1Temp, m1TempMin, m1TempMax, m1TempNight, m1TempEve, m1TempMorn,
                        m2Time, m2Summary, m2Icon, m2Temp, m2TempMin, m2TempMax, m2TempNight, m2TempEve, m2TempMorn,
                        m3Time, m3Summary, m3Icon, m3Temp, m3TempMin, m3TempMax, m3TempNight, m3TempEve, m3TempMorn,
                        m4Time, m4Summary, m4Icon, m4Temp, m4TempMin, m4TempMax, m4TempNight, m4TempEve, m4TempMorn);
                weatherArrayList.add(obj);
                setFlipperImage(obj);
            } while (c.moveToNext());

            //updateUI(weatherArrayList);
        }
        db.close();
    }


    public boolean isNetworkAvailable() {
        getActivity().getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        //NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        //return activeNetworkInfo != null;
        if (connectivityManager.getActiveNetworkInfo() != null) {
            if (connectivityManager.getActiveNetworkInfo().isAvailable()
                    && connectivityManager.getActiveNetworkInfo().isConnected())
                return true;
        }
        tempHour = 0;
        return false;
    }

    /*public boolean isNetworkAvailable() {
        getActivity().getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    */

    @Override
    public void onDetach() {
        super.onDetach();
        if (downloadWeather != null)
            downloadWeather.cancel(true);
    }
}
