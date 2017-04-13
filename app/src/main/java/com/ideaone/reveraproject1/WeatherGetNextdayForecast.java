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
//public class WeatherGetNextdayForecast {
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
//
//    HttpGetTask getWeather;
//
//    public WeatherGetNextdayForecast(Context contex, TextView date, ImageView icon, TextView condition, TextView temperature, int day_index) {
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
//        public static final String URL = "http://api.wunderground.com/api/"+WeatherFragment.APIKEY+"/forecast/q/zmw:" + WeatherFragment.weatherLocation + ".json";
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
//            if (!result.isEmpty()) {
//                sdf_date = new SimpleDateFormat("EEEE");
//                calendar = new GregorianCalendar();
//                calendar.add(Calendar.DATE, day_index);
//                resultdate = calendar.getTime();
//                date.setText(sdf_date.format(resultdate));
//                date.setTextSize(30);
//                date.setGravity(Gravity.CENTER);
//                condition.setText(result.get(0));
//                condition.setTextSize(27);
//                condition.setGravity(Gravity.CENTER);
//                temperature.setText(Html.fromHtml("<b>" + result.get(1) + "</b>" + " " + "<b>&deg</b>C"));
//                temperature.setTextSize(45);
//                temperature.setGravity(Gravity.CENTER);
//                icon_url = result.get(2);
//                new ICON_download().execute(icon_url);
//            }
//        }
//    }
//
//    private class JSONResponseHandler implements ResponseHandler<List<String>> {
//
//        private static final String FORCATS_TAG = "forecast";
//        private static final String SIMPLEFORMAT_TAG = "simpleforecast";
//        private static final String FORCASTDATE_TAG = "forecastday";
//        private static final String HIGH_TAG = "high";
//        private static final String LOW_TAG = "low";
//        private static final String CELICIUS_TAG = "celsius";
//        private static final String CONDITION_TAG = "conditions";
//        private static final String ICON_TAG = "icon_url";
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
//                JSONObject forcast = responseObject.getJSONObject(FORCATS_TAG);
//                JSONObject simpleformat = forcast.getJSONObject(SIMPLEFORMAT_TAG);
//                JSONArray forcastDateArray = simpleformat.getJSONArray(FORCASTDATE_TAG);
//                JSONObject day = (JSONObject) forcastDateArray.get(day_index);
//                JSONObject high = day.getJSONObject(HIGH_TAG);
//                JSONObject low = day.getJSONObject(LOW_TAG);
//                String min = (String) low.get(CELICIUS_TAG);
//                String max = (String) high.get(CELICIUS_TAG);
//                String weather = (String) day.get(CONDITION_TAG);
//                String Url = (String) day.get(ICON_TAG);
//
//                result.add(weather);
//                result.add(max);
//                result.add(Url);
//
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
//
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