//package com.ideaone.reveraproject1;
//
///**
// * Created by hkarimi on 2015-11-11.
// */
//
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Typeface;
//import android.graphics.drawable.Drawable;
//import android.net.http.AndroidHttpClient;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Html;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
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
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONTokener;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class WeatherDailyFragment extends Fragment {
//
//    private View V;
//    private ImageView background, weather_icon, icon_day1, icon_day2, icon_day3, daily_weather, hourly_weather;
//    private TextView location, weather_condition, temperature, feels_like;
//    private TextView day1, day2, day3, cond1, cond2, cond3, temp1, temp2, temp3;
//    private LinearLayout first, second, third;
//    Drawable background_image;
//    private Bitmap icon;
//    Typeface font;
//    HttpGetTask httpGetTask;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        V = inflater.inflate(R.layout.weather_layout, container, false);
//        daily_weather = (ImageView) V.findViewById(R.id.weather_daily);
//        hourly_weather = (ImageView) V.findViewById(R.id.weather_hourly);
//        daily_weather.setImageDrawable(getResources().getDrawable(R.drawable.side5day));
//        hourly_weather.setImageDrawable(getResources().getDrawable(R.drawable.side24hrselect));
//        daily_weather.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WeatherFragment fragment2 = new WeatherFragment();
//                FragmentManager fragmentManager = getFragmentManager();
//                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.remove(fragment);
//                fragmentTransaction.replace(R.id.fragment_holder, fragment2);
//                fragmentTransaction.commit();
//            }
//        });
//
//        TextView current_weather_text = (TextView) V.findViewById(R.id.current_condition);
//        current_weather_text.setTypeface(font);
//        weather_icon = (ImageView) V.findViewById(R.id.weather_icon);
//
//        weather_condition = (TextView) V.findViewById(R.id.weather_condition);
//        temperature = (TextView) V.findViewById(R.id.temperature);
//        feels_like = (TextView) V.findViewById(R.id.feels_like);
//
//        first = (LinearLayout) V.findViewById(R.id.day1);
//        second = (LinearLayout) V.findViewById(R.id.day2);
//        third = (LinearLayout) V.findViewById(R.id.day3);
//
//
//        icon_day1 = (ImageView) V.findViewById(R.id.icon_day1);
//        icon_day2 = (ImageView) V.findViewById(R.id.icon_day2);
//        icon_day3 = (ImageView) V.findViewById(R.id.icon_day3);
//        day1 = (TextView) V.findViewById(R.id.date_day1);
//        day2 = (TextView) V.findViewById(R.id.date_day2);
//        day3 = (TextView) V.findViewById(R.id.date_day3);
//        day1.setBackgroundColor(getResources().getColor(R.color.day1));
//        day2.setBackgroundColor(getResources().getColor(R.color.day2));
//        day3.setBackgroundColor(getResources().getColor(R.color.day3));
//        cond1 = (TextView) V.findViewById(R.id.condition_day1);
//        cond2 = (TextView) V.findViewById(R.id.condition_day2);
//        cond3 = (TextView) V.findViewById(R.id.condition_day3);
//        temp1 = (TextView) V.findViewById(R.id.temperature_day1);
//        temp2 = (TextView) V.findViewById(R.id.temperature_day2);
//        temp3 = (TextView) V.findViewById(R.id.temperature_day3);
//
//        httpGetTask = new HttpGetTask();
//        httpGetTask.execute();
//        WeatherGetHourlyForecast nextday1 = new WeatherGetHourlyForecast(getActivity().getApplicationContext(), day1, icon_day1, cond1, temp1, 5);
//        WeatherGetHourlyForecast nextday2 = new WeatherGetHourlyForecast(getActivity().getApplicationContext(), day2, icon_day2, cond2, temp2, 12);
//        WeatherGetHourlyForecast nextday3 = new WeatherGetHourlyForecast(getActivity().getApplicationContext(), day3, icon_day3, cond3, temp3, 24);
//
//        return V;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        if (httpGetTask != null)
//            httpGetTask.cancel(true);
//    }
//
//    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {
//        //http://api.wunderground.com/api/8f0aaafc6aafd1c4/conditions/q/canada/vancouver.json
//        //     private final String URL = "http://api.wunderground.com/api/8f0aaafc6aafd1c4/conditions/q/canada/toronto.json";
//        private final String URL = "http://api.wunderground.com/api/" + WeatherFragment.APIKEY + "/conditions/q/zmw:" + WeatherFragment.weatherLocation + ".json";
//
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
//            try {
//                if (null != mClient)
//                    mClient.close();
//                new ICON_download().execute(result.get(4));
//                weather_condition.setText(" " + result.get(1) + "   ");
//                temperature.setText(Html.fromHtml("<b>" + result.get(2) + "</b>" + " " + "<b>&deg</b>C"));
//                temperature.setTextSize(150);
//                feels_like.setText(Html.fromHtml("Feels like " + "<b>" + result.get(3) + "</b>" + " " + "<b>&deg</b>C"));
//                feels_like.setTextSize(35);
//                weather_condition.setTypeface(font);
//                temperature.setTypeface(font);
//                feels_like.setTypeface(font);
//                temperature.setGravity(Gravity.BOTTOM);
//                feels_like.setGravity(Gravity.TOP);
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }
//
//    private class JSONResponseHandler implements ResponseHandler<List<String>> {
//
//        private static final String CURRENT_OBS_TAG = "current_observation";
//        private static final String LOCATION_TAG = "display_location";
//        private static final String CITY_TAG = "city";
//        private static final String STATE_TAG = "state";
//        private static final String TEMP_TAG = "temp_c";
//        private static final String WEATHER_TAG = "weather";
//        private static final String FEELS_LIKE_TAG = "feelslike_c";
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
//                JSONObject current = responseObject
//                        .getJSONObject(CURRENT_OBS_TAG);
//                JSONObject locationJSON = current.getJSONObject(LOCATION_TAG);
//                String city = (String) locationJSON.get(CITY_TAG);
//                String state = (String) locationJSON.get(STATE_TAG);
//                String temperature1 = Integer.toString(((Number) current.get(TEMP_TAG)).intValue());
//                String weather = ((String) current.get(WEATHER_TAG));
//                String feelsLike = ((String) current.get(FEELS_LIKE_TAG));
//                String Url = ((String) current.get(ICON_URL_TAG));
//
//
//                result.add(city + "," + state);
//                result.add(weather);
//                result.add(temperature1);
//                result.add(feelsLike);
//                result.add(Url);
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (IndexOutOfBoundsException e) {
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
//            icon = downloadBitmap(param[0]);
//            finishedTask();
//            return true;
//
//        }
//
//        @Override
//        protected void onPostExecute(Boolean df) {
//            weather_icon.setImageBitmap(icon);
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
//            return true;
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//}