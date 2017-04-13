//package com.ideaone.reveraproject1;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.net.http.AndroidHttpClient;
//import android.os.AsyncTask;
//import android.text.Html;
//import android.util.Log;
//import android.view.Gravity;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONTokener;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//
///**
// * Created by hkarimi on 2015-11-11.
// */
//public class WeatherGetHourlyForecast {
//    private TextView date, condition, temperature;
//    private ImageView icon;
//    Drawable background_image;
//    Context contex;
//    int day_index;
//    GregorianCalendar calendar;
//    SimpleDateFormat sdf_date;
//    Date resultdate;
//    String icon_url;
//    Bitmap iconBitmap;
//    HttpGetTask getWeather;
//
//    public WeatherGetHourlyForecast(Context contex, TextView date, ImageView icon, TextView condition, TextView temperature, int day_index) {
//        this.contex = contex;
//        this.date = date;
//        this.icon = icon;
//        this.condition = condition;
//        this.temperature = temperature;
//        this.day_index = day_index;
//        getWeather = new HttpGetTask();
//        getWeather.execute();
//    }
//
//    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {
//
//        private final String URL = "http://api.wunderground.com/api/" + WeatherFragment.APIKEY + "/hourly/q/zmw:" + WeatherFragment.weatherLocation + ".json";
//
//        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");
//
//        @Override
//        protected List<String> doInBackground(Void... params) {
//            HttpGet request = new HttpGet(URL);
//            JSONResponseHandler responseHandler = new JSONResponseHandler();
//            try {
//                return mClient.execute(request, responseHandler);
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(final List<String> result) {
//            if (null != mClient)
//                mClient.close();
//
//            if (result != null) {
//                sdf_date = new SimpleDateFormat("EEE (MMM dd)");
//                calendar = new GregorianCalendar();
//                calendar.add(Calendar.DATE, day_index);
//                resultdate = calendar.getTime();
//
//                date.setText(result.get(0));
//                date.setTextSize(30);
//                date.setGravity(Gravity.CENTER);
//                condition.setText(result.get(1));
//                condition.setTextSize(30);
//                condition.setGravity(Gravity.CENTER);
//                temperature.setText(Html.fromHtml("<b>" + result.get(2) + "</b>" + " " + "<b>&deg</b>C"));
//                temperature.setTextSize(45);
//                temperature.setGravity(Gravity.CENTER);
//                icon_url = result.get(3);
//                new ICON_download().execute(icon_url);
//            }
//
//        }
//    }
//
//    private class JSONResponseHandler implements ResponseHandler<List<String>> {
//
//        private static final String HOURLY_TAG = "hourly_forecast";
//        private static final String FCTTIME_TAG = "FCTTIME";
//        private static final String WEEKDAY_TAG = "weekday_name_abbrev";
//        private static final String CIVIL_TAG = "civil";
//        private static final String AMPM_TAG = "ampm";
//        private static final String TEMP_TAG = "temp";
//        private static final String METRIC_TAG = "metric";
//        private static final String WEATHER_TAG = "condition";
//        private static final String ICON_URL_TAG = "icon_url";
//
//
//        @Override
//        public List<String> handleResponse(HttpResponse response)
//                throws ClientProtocolException, IOException {
//            List<String> result = new ArrayList<String>();
//            String JSONResponse = new BasicResponseHandler()
//                    .handleResponse(response);
//            try {
//
//                // Get top-level JSON Object - a Map
//                JSONObject responseObject = (JSONObject) new JSONTokener(
//                        JSONResponse).nextValue();
//
//                // Extract value of "messages" key -- a List
//                JSONArray hourly_forecast = responseObject.getJSONArray(HOURLY_TAG);
//                JSONObject hourlyForecast = hourly_forecast.getJSONObject(day_index);
//                JSONObject fcttime = hourlyForecast.getJSONObject(FCTTIME_TAG);
//                System.out.println(fcttime.get(AMPM_TAG));
//                String ampm = (String) fcttime.get(AMPM_TAG);
//
//                result.add((String) fcttime.get(WEEKDAY_TAG) + " (" + fcttime.get(CIVIL_TAG) + ")");
//
//
//                JSONObject temp = hourlyForecast.getJSONObject(TEMP_TAG);
//                String max = ((String) temp.get(METRIC_TAG));
//                String weather = ((String) hourlyForecast.get(WEATHER_TAG));
//                String Url = ((String) hourlyForecast.get(ICON_URL_TAG));
//
//                result.add(weather);
//                result.add(max);
//                result.add(Url);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//    }
//
//    private class ICON_download extends AsyncTask<String, Void, Boolean> {
//        int ViewID;
//
//        @Override
//        protected Boolean doInBackground(String... param) {
//            iconBitmap = downloadBitmap(param[0]);
//            finishedTask();
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean df) {
//            icon.setImageBitmap(iconBitmap);
//        }
//
//        private Bitmap downloadBitmap(String url) {
//            // initilize the default HTTP client object
//            final DefaultHttpClient client = new DefaultHttpClient();
//
//
//            //forming a HttoGet request
//            final HttpGet getRequest = new HttpGet(url);
//            try {
//
//                HttpResponse response = client.execute(getRequest);
//
//                //check 200 OK for success
//                final int statusCode = response.getStatusLine().getStatusCode();
//
//                if (statusCode != HttpStatus.SC_OK) {
//                    Log.w("ImageDownloader", "Error " + statusCode +
//                            " while retrieving bitmap from " + url);
//                    return null;
//
//                }
//
//                final HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    InputStream inputStream = null;
//                    try {
//                        // getting contents from the stream
//                        inputStream = entity.getContent();
//
//                        // decoding stream data back into image Bitmap that android understands
//                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//                        return bitmap;
//                    } finally {
//                        if (inputStream != null) {
//                            inputStream.close();
//                        }
//                        entity.consumeContent();
//                    }
//                }
//            } catch (Exception e) {
//                // You Could provide a more explicit error message for IOException
//                getRequest.abort();
//                Log.e("ImageDownloader", "Something went wrong while" +
//                        " retrieving bitmap from " + url + " " + e.toString());
//            }
//
//            return null;
//        }
//
//        public boolean finishedTask() {
//            if (getWeather != null)
//                getWeather.cancel(true);
//            return true;
//        }
//    }
//}