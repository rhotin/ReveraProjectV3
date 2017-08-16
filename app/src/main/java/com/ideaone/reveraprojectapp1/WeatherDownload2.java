package com.ideaone.reveraprojectapp1;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

class WeatherDownload2 extends AsyncTask<Void, Integer, Void> {

    private ArrayList<WeatherObject2> weatherArrayList = new ArrayList<>();
    Communicator context;

    WeatherDownload2(WeatherFragment2 c) {
        this.context = c;
    }

    @Override
    protected Void doInBackground(Void... params) {
        URL theUrl, theUrlCur;
        try {
            theUrl = new URL(WeatherFragment2.WEATHER_URL);
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String weather_json = reader.readLine();

            theUrlCur = new URL(WeatherFragment2.WEATHER_URL_CURRENT);
            BufferedReader readerC = new BufferedReader
                    (new InputStreamReader(theUrlCur.openConnection().getInputStream(), "UTF-8"));
            String weather_jsonC = readerC.readLine();

            JSONObject weather_object = new JSONObject(weather_json);
            JSONObject weather_object_cur = new JSONObject(weather_jsonC);

            double getLatitude = weather_object.getJSONObject("city").getJSONObject("coord").getDouble("lon");
            double getLongitude = weather_object.getJSONObject("city").getJSONObject("coord").getDouble("lat");
            String getLocation = weather_object.getJSONObject("city").getString("name");
            /**
             * CHANGE THIS TO PROPER LOCATION!!!
             */
            // String getLocation = "Toronto";

            JSONArray data_arr = weather_object.getJSONArray("list");

            int get1Time = data_arr.getJSONObject(0).getInt("dt"); //time
            String get1SummaryC = weather_object_cur.getJSONArray("weather").getJSONObject(0).getString("description");
            String get1IconC = weather_object_cur.getJSONArray("weather").getJSONObject(0).getString("icon");
            String get1Summary = data_arr.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");
            String get1Icon = data_arr.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon");
            double get1Temperature = data_arr.getJSONObject(0).getJSONObject("temp").getDouble("day");
            double get1TemperatureMin = data_arr.getJSONObject(0).getJSONObject("temp").getDouble("min");
            double get1TemperatureMax = data_arr.getJSONObject(0).getJSONObject("temp").getDouble("max");
            double get1TemperatureNight = data_arr.getJSONObject(0).getJSONObject("temp").getDouble("night");
            double get1TemperatureEve = data_arr.getJSONObject(0).getJSONObject("temp").getDouble("eve");
            double get1TemperatureMorn = data_arr.getJSONObject(0).getJSONObject("temp").getDouble("morn");
            int get2Time = data_arr.getJSONObject(1).getInt("dt");
            String get2Summary = data_arr.getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("description");
            String get2Icon = data_arr.getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("icon");
            double get2Temperature = data_arr.getJSONObject(1).getJSONObject("temp").getDouble("day");
            double get2TemperatureMin = data_arr.getJSONObject(1).getJSONObject("temp").getDouble("min");
            double get2TemperatureMax = data_arr.getJSONObject(1).getJSONObject("temp").getDouble("max");
            double get2TemperatureNight = data_arr.getJSONObject(1).getJSONObject("temp").getDouble("night");
            double get2TemperatureEve = data_arr.getJSONObject(1).getJSONObject("temp").getDouble("eve");
            double get2TemperatureMorn = data_arr.getJSONObject(1).getJSONObject("temp").getDouble("morn");
            int get3Time = data_arr.getJSONObject(2).getInt("dt");
            String get3Summary = data_arr.getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("description");
            String get3Icon = data_arr.getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("icon");
            double get3Temperature = data_arr.getJSONObject(2).getJSONObject("temp").getDouble("day");
            double get3TemperatureMin = data_arr.getJSONObject(2).getJSONObject("temp").getDouble("min");
            double get3TemperatureMax = data_arr.getJSONObject(2).getJSONObject("temp").getDouble("max");
            double get3TemperatureNight = data_arr.getJSONObject(2).getJSONObject("temp").getDouble("night");
            double get3TemperatureEve = data_arr.getJSONObject(2).getJSONObject("temp").getDouble("eve");
            double get3TemperatureMorn = data_arr.getJSONObject(2).getJSONObject("temp").getDouble("morn");
            int get4Time = data_arr.getJSONObject(3).getInt("dt");
            String get4Summary = data_arr.getJSONObject(3).getJSONArray("weather").getJSONObject(0).getString("description");
            String get4Icon = data_arr.getJSONObject(3).getJSONArray("weather").getJSONObject(0).getString("icon");
            double get4Temperature = data_arr.getJSONObject(3).getJSONObject("temp").getDouble("day");
            double get4TemperatureMin = data_arr.getJSONObject(3).getJSONObject("temp").getDouble("min");
            double get4TemperatureMax = data_arr.getJSONObject(3).getJSONObject("temp").getDouble("max");
            double get4TemperatureNight = data_arr.getJSONObject(3).getJSONObject("temp").getDouble("night");
            double get4TemperatureEve = data_arr.getJSONObject(3).getJSONObject("temp").getDouble("eve");
            double get4TemperatureMorn = data_arr.getJSONObject(3).getJSONObject("temp").getDouble("morn");

            WeatherObject2 obj = new WeatherObject2(getLatitude, getLongitude, getLocation,
                    get1Time, get1SummaryC, get1IconC, get1Summary, get1Icon, get1Temperature, get1TemperatureMin, get1TemperatureMax,
                    get1TemperatureNight, get1TemperatureEve, get1TemperatureMorn,
                    get2Time, get2Summary, get2Icon, get2Temperature, get2TemperatureMin, get2TemperatureMax,
                    get2TemperatureNight, get2TemperatureEve, get2TemperatureMorn,
                    get3Time, get3Summary, get3Icon, get3Temperature, get3TemperatureMin, get3TemperatureMax,
                    get3TemperatureNight, get3TemperatureEve, get3TemperatureMorn,
                    get4Time, get4Summary, get4Icon, get4Temperature, get4TemperatureMin, get4TemperatureMax,
                    get4TemperatureNight, get4TemperatureEve, get4TemperatureMorn);
            weatherArrayList.add(obj);
            //     publishProgress((int) (((i + 1.0) / totalDays) * 100.0));
            //   }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.updateUI(weatherArrayList);
    }

    interface Communicator {

        void updateUI(ArrayList<WeatherObject2> photosArrayList);
    }
}
