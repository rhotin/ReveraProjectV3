package com.ideaone.reveraproject1;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class HomeFragment extends Fragment {
    View V;

    public static String locationSelected;


    //00000.1.71368 = waterloo, ontario / kitchener
    //00000.76.71622 = london, ontario
    //00000.11.71639 = richmond hill, ontario
    //00000.1.71265 = toronto, ontario
    //00000.1.71892 = Vancouver, BC
    //00000.6.71775 = Langley, BC
    //00000.12.71639 = vaughan, ontario
    //00000.3.71629 = Lindsay, Ontario - william place
    //00000.28.WCYOO = Port Perry, Ontario
    //00000.74.71368 = strasburg, Ontario or Cambridge Ontatio

    public static final String weatherLocation = "00000.1.71368";

    /**
     * Weather API KEYS
     * 421f30eef552c298  = Leaside
     * 421f30eef552c298  = Revera
     */
    public static final String APIKEY = "421f30eef552c298"; // leaside

    public static final String URL = "http://api.wunderground.com/api/" + APIKEY + "/conditions/q/zmw:" + weatherLocation + ".json";

   // TextView clock;
   // long yourmilliseconds;
   // Date resultdate;
   // SimpleDateFormat sdf_clock;

    HttpGetTask httpGetTask;
    TextView msg1_view;
    TextView msg1_view_authur;
    MsgObjectHome mObj = new MsgObjectHome("Connection Issue", "Loading...");

    //weather
    Bitmap icon;
    ImageView weather_icon;
    TextView weather_condition, temperature, feels_like;
    HttpGetTaskWeather httpGetTaskWeather;

    ImageView mImageViewChoice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        V = inflater.inflate(R.layout.home_fragment, container, false);

        locationSelected = getString(R.string.ReveraLocation);

        final SharedPreferences prefs = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        locationSelected = prefs.getString("location", getString(R.string.ReveraLocation));

        msg1_view_authur = (TextView) V.findViewById(R.id.msgAuthur);
        msg1_view = (TextView) V.findViewById(R.id.msgText);

        mImageViewChoice = (ImageView) V.findViewById(R.id.imageViewChoice);

        if (isNetworkAvailable()) {
            httpGetTask = new HttpGetTask();
            httpGetTask.execute();
        } else{
            msg1_view.setText(mObj.msgText);
            msg1_view_authur.setText("..." + mObj.msgAuthor);
        }

        weather_icon = (ImageView) V.findViewById(R.id.weatherIcon);
        weather_condition = (TextView) V.findViewById(R.id.weatherCondText);
        temperature = (TextView) V.findViewById(R.id.tempText);
        feels_like = (TextView) V.findViewById(R.id.feelText);

        if (isNetworkAvailable()) {
            feels_like.setText("Getting Weather");
            weather_condition.setText("");
            httpGetTaskWeather = new HttpGetTaskWeather();
            httpGetTaskWeather.execute();
        } else {
            feels_like.setText("Loading");
            weather_condition.setText("weather");
        }

        mImageViewChoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View promptView = layoutInflater.inflate(R.layout.dialog_password, null);

                    ArrayList<String> selectLocationList = new ArrayList<String>();
                    DownloadLocations downloadTask = new DownloadLocations();
                    try {
                        selectLocationList = downloadTask.execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Settings");
                    builder.setMessage("Password needed to change location");
                    builder.setView(promptView);
                    final EditText input = (EditText) promptView.findViewById(R.id.userInput);
                    builder.setCancelable(false);
                    final ArrayList<String> finalSelectLocationList = selectLocationList;
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (input.getText().toString().equals("Idea12345")) {
                                final String[] selectLocation = new String[finalSelectLocationList.size()];
                                for (int i = 0; i < finalSelectLocationList.size(); i++) {
                                    selectLocation[i] = finalSelectLocationList.get(i);
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Select Location");
                                builder.setItems(selectLocation, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putString("location", selectLocation[which]);
                                        editor.commit();
                                        locationSelected = selectLocation[which];
                                        Toast.makeText(getActivity(), selectLocation[which] + " Selected",
                                                Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("cancel",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("Wrong Password");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                return false;
            }
        });

        return V;
    }

    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {
        private final String URL = "http://revera.mxs-s.com/displays/demo/promos.json?nohtml=1";

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params) {
            URL theUrl;
            try {
                theUrl = new URL(URL);
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
                String instagram_json = reader.readLine();

                JSONObject instagram_object = new JSONObject(instagram_json);
                JSONArray data_arr = instagram_object.getJSONArray("promos");

                int totalMsgs = data_arr.length();
                for (int i = 0; i < totalMsgs; i++) {
                    if (data_arr.getJSONObject(i).getString("name").equals("TodaysQuote") &&
                            data_arr.getJSONObject(i).getString("displayID").equals("537cd48cc0af978a0b000007")) {
                        String msgText = data_arr.getJSONObject(i).getString("text");
                        String[] parts = msgText.split("!!!"); // escape .

                        mObj.msgText = parts[0];
                        mObj.msgAuthor = parts[1];
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            //super.onPostExecute(strings);
            msg1_view.setText(Html.fromHtml(mObj.msgText));
            msg1_view_authur.setText(Html.fromHtml("..." + mObj.msgAuthor));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (httpGetTask != null)
            httpGetTask.cancel(true);
        if (httpGetTaskWeather != null)
            httpGetTaskWeather.cancel(true);
    }



    //weather
    private class ICON_download extends AsyncTask<String, Void, Boolean> {
        //int ViewID;

        @Override
        protected Boolean doInBackground(String... param) {
            icon = downloadBitmap(param[0]);
            finishedTask();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean df) {
            weather_icon.setImageBitmap(icon);
        }

        private Bitmap downloadBitmap(String url) {
            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();


            //forming a HttoGet request
            final HttpGet getRequest = new HttpGet(url);
            try {
                HttpResponse response = client.execute(getRequest);

                //check 200 OK for success
                final int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return null;
                }

                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        // getting contents from the stream
                        inputStream = entity.getContent();

                        // decoding stream data back into image Bitmap that android understands
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + " " + e.toString());
            }

            return null;
        }

        public boolean finishedTask() {
            return true;
        }
    }

    private class HttpGetTaskWeather extends AsyncTask<Void, Void, List<String>> {

        //private final String URL = WeatherFragment.URL;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return mClient.execute(request, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<String> result) {
            if (null != mClient)
                mClient.close();
            if (result != null && result.size() > 3) {
                try {
                    new ICON_download().execute(result.get(4));
                    weather_condition.setText(" " + result.get(1) + "   ");
                    temperature.setText(Html.fromHtml("<b>" + result.get(2) + "</b>" + " " + "<b>&deg</b>C"));
                    feels_like.setText(Html.fromHtml("Feels like " + "<b>" + result.get(3) + "</b>" + " " + "<b>&deg</b>C"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class JSONResponseHandler implements ResponseHandler<List<String>> {

        private static final String CURRENT_OBS_TAG = "current_observation";
        private static final String LOCATION_TAG = "display_location";
        private static final String CITY_TAG = "city";
        private static final String STATE_TAG = "state";
        private static final String TEMP_TAG = "temp_c";
        private static final String WEATHER_TAG = "weather";
        private static final String FEELS_LIKE_TAG = "feelslike_c";
        private static final String ICON_URL_TAG = "icon_url";

        @Override
        public List<String> handleResponse(HttpResponse response)
                throws IOException {
            List<String> result = new ArrayList<String>();
            String JSONResponse = new BasicResponseHandler()
                    .handleResponse(response);
            try {

                // Get top-level JSON Object - a Map
                JSONObject responseObject = (JSONObject) new JSONTokener(
                        JSONResponse).nextValue();

                // Extract value of "messages" key -- a List
                JSONObject current = responseObject
                        .getJSONObject(CURRENT_OBS_TAG);
                JSONObject locationJSON = current.getJSONObject(LOCATION_TAG);
                String city = (String) locationJSON.get(CITY_TAG);
                String state = (String) locationJSON.get(STATE_TAG);
                String temperature1 = Integer.toString(((Number) current.get(TEMP_TAG)).intValue());
                String weather = ((String) current.get(WEATHER_TAG));
                String feelsLike = ((String) current.get(FEELS_LIKE_TAG));
                String Url = ((String) current.get(ICON_URL_TAG));

                result.add(city + "," + state);
                result.add(weather);
                result.add(temperature1);
                result.add(feelsLike);
                result.add(Url);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public boolean isNetworkAvailable() {
        getActivity().getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}