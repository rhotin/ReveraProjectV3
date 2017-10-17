package com.ideaone.tabletapp1;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment implements HomeDownload.Communicator {
    View V;

    public static String promoType = "Announcement";
    ArrayList<PromoObject> promosArrayList = new ArrayList<>();
    HomeDBAdapter db;
    int tempMonth = 1;
    HomeDownload downloadHome;

    public static String companySelected;
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
    //00000.162.71624 = Mississauga, ontario
    //00000.42.71697 = Oshawa, ontario
    //00000.160.71624 = etobecoke

    public static String weatherLocation = "ontario/toronto";

    /**
     * Weather API KEYS
     * 421f30eef552c298  = Leaside
     * 421f30eef552c298  = Revera
     */
    public static String APIKEY = "421f30eef552c298"; // leaside

    public static String URL = "http://api.wunderground.com/api/" + APIKEY + "/conditions/q/zmw:" + weatherLocation + ".json";

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
    ImageView mImageViewLogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        V = inflater.inflate(R.layout.home_fragment, container, false);

        final SharedPreferences prefs = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        locationSelected = prefs.getString("location", getString(R.string.RetirementLocation));
        companySelected = prefs.getString("company", getString(R.string.RetirementCompany));
        if (locationSelected.equals("leaside-14")) {
            locationSelected = "leaside";
        }

        switch (locationSelected) {
            case "beechwood_court":
                weatherLocation = "ontario/mississauga";
                break;
            case "beechwood_place":
                weatherLocation = "ontario/mississauga";
                break;
            case "bough_beeches":
                weatherLocation = "ontario/mississauga";
                break;
            case "bradgate":
                weatherLocation = "ontario/toronto";
                break;
            case "briarfield_gardens":
                weatherLocation = "ontario/kitchener";
                break;
            case "cedarcroft_place":
                weatherLocation = "ontario/Oshawa";
                break;
            case "demo":
                weatherLocation = "ontario/toronto";
                break;
            case "demo_2":
                weatherLocation = "ontario/toronto";
                break;
            case "demo4":
                weatherLocation = "ontario/toronto";
                break;
            case "donmills_apartments":
                weatherLocation = "ontario/toronto";
                break;
            case "donway_place":
                weatherLocation = "ontario/toronto";
                break;
            case "fergus_place":
                weatherLocation = "ontario/kitchener";
                break;
            case "forest_hill":
                weatherLocation = "ontario/toronto";
                break;
            case "glynnwood":
                weatherLocation = "ontario/thornhill";
                break;
            case "granite_landing":
                weatherLocation = "ontario/cambridge";
                break;
            case "highland_place":
                weatherLocation = "ontario/kitchener";
                break;
            case "kingsway":
                weatherLocation = "ontario/etobicoke";
                break;
            case "leaside":
                weatherLocation = "ontario/toronto";
                break;
            case "pinevilla":
                weatherLocation = "ontario/toronto";
                break;
            case "port_perry_villa":
                weatherLocation = "ontario/Port%20Perry";
                break;
            case "rayoak":
                weatherLocation = "ontario/North%20York";
                break;
            case "terrace":
                weatherLocation = "ontario/North%20York";
                break;
            case "annex":
                weatherLocation = "ontario/toronto";
                break;
            case "the_renoir":
                weatherLocation = "ontario/newmarket";
                break;
            case "william_place":
                weatherLocation = "ontario/lindsay";
                break;
            case "windermere":
                weatherLocation = "ontario/london";
                break;
            default:
                weatherLocation = "ontario/toronto";
                break;
        }

        // URL = "http://api.wunderground.com/api/" + APIKEY + "/conditions/q/zmw:" + weatherLocation + ".json";
        URL = "http://api.wunderground.com/api/" + APIKEY + "/conditions/q/" + weatherLocation + ".json";
        Log.e("Home Weather", "" + URL);

        msg1_view_authur = (TextView) V.findViewById(R.id.msgAuthur);
        msg1_view = (TextView) V.findViewById(R.id.msgText);

        mImageViewChoice = (ImageView) V.findViewById(R.id.imageViewChoice);
        mImageViewLogo = (ImageView) V.findViewById(R.id.imageViewLogo);

        db = new HomeDBAdapter(getActivity().getApplicationContext());
        getAllItems();

        if (isNetworkAvailable()) {
            httpGetTask = new HttpGetTask();
            httpGetTask.execute();
        } else {
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
            feels_like.setText(R.string.loading);
            weather_condition.setText("weather");
        }

        mImageViewChoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View promptView = layoutInflater.inflate(R.layout.dialog_password, null);

                    // ArrayList selectLocationList = new ArrayList<String>();
                    // DownloadLocations downloadTask = new DownloadLocations();
                    // try {
                    //     selectLocationList = downloadTask.execute().get();
                    // } catch (ExecutionException | InterruptedException e) {
                    //     e.printStackTrace();
                    // }

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Settings");
                    builder.setMessage("Password needed to change location");
                    builder.setView(promptView);
                    final EditText input = (EditText) promptView.findViewById(R.id.userInput);
                    builder.setCancelable(false);
                    //  final ArrayList<String> finalSelectLocationList = selectLocationList;
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (input.getText().toString().equals("Idea12345")) {

                                Intent intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                                startActivity(intent);
                                dialog.dismiss();

                                /*
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
                                        Intent intent = new Intent(getActivity().getApplicationContext(), SettingsActivity.class);
                                        startActivity(intent);
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
                                */

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
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        if (isNetworkAvailable()) {
            Log.e("Announce PIC", " network ");
            if (month != tempMonth) {
                tempMonth = month;
                db.open();
                db.resetDB();
                db.close();
            }
            //   messageText.setText("");
            downloadHome = new HomeDownload(this);
            downloadHome.execute();
            //   }
        }else{
            getAllItems();
        }

        return V;
    }

    @Override
    public void updateUI(ArrayList<PromoObject> photosArrayList) {
        getAllItems();
    }

    public void getAllItems() {
        promosArrayList.clear();
        String creatorID;
        String created;
        String name;
        String zone;
        String displayID;
        int length;
        int priority;
        String dateStart;
        String dateEnd;
        String web;
        String display;
        String calendar;
        String bulletin;
        String kiosk;
        String type;
        String promoType;
        String modified;
        int showMonday;
        int showTuesday;
        int showWednesday;
        int showThursday;
        int showFriday;
        int showSaturday;
        int showSunday;
        String url;
        String text;
        byte[] Image;
        byte[] ImageBack;
        Bitmap photo = null;
        Bitmap backPhoto = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inSampleSize = calculateInSampleSize(options, 300, 300);
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        BitmapFactory.Options optionsBack = new BitmapFactory.Options();
        optionsBack.inScaled = true;
        optionsBack.inSampleSize = calculateInSampleSize(options, 200, 200);
        optionsBack.inPreferredConfig = Bitmap.Config.RGB_565;

        db.open();
        Cursor c = db.getAllItems();
        if (c.moveToFirst()) {
            do {
                creatorID = c.getString(1);
                created = c.getString(2);
                name = c.getString(3);
                zone = c.getString(4);
                displayID = c.getString(5);
                length = c.getInt(6);
                priority = c.getInt(7);
                dateStart = c.getString(8);
                dateEnd = c.getString(9);
                web = c.getString(10);
                display = c.getString(11);
                calendar = c.getString(12);
                bulletin = c.getString(13);
                kiosk = c.getString(14);
                type = c.getString(15);
                promoType = c.getString(16);
                modified = c.getString(17);
                showMonday = c.getInt(18);
                showTuesday = c.getInt(19);
                showWednesday = c.getInt(20);
                showThursday = c.getInt(21);
                showFriday = c.getInt(22);
                showSaturday = c.getInt(23);
                showSunday = c.getInt(24);
                url = c.getString(25);
                text = c.getString(26);
                Image = c.getBlob(27);
                ImageBack = c.getBlob(28);

                if (Image != null) {
                    try {
                        photo = BitmapFactory.decodeByteArray(Image, 0, Image.length, options);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                }

                if (ImageBack != null) {
                    try {
                        backPhoto = BitmapFactory.decodeByteArray(ImageBack, 0, ImageBack.length, optionsBack);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                }

                /*creatorID, created, name,
                                    zone, displayID, length, priority, dateStart, dateEnd, web,
                                    display, calendar, messages, type, promoType, modified, showMonday,
                                    showTuesday, showWednesday, showThursday, showFriday, showSaturday,
                                    showSunday, url, text, bmp
                                    */
                PromoObject obj = new PromoObject(creatorID, created, name, zone,
                        displayID, length, priority, dateStart, dateEnd, web, display, calendar, bulletin,
                        kiosk, type, promoType, modified, showMonday, showTuesday, showWednesday,
                        showThursday, showFriday, showSaturday, showSunday, url, text, photo, backPhoto);
                promosArrayList.add(obj);
                photo = null;
                setLogoImage(obj);
            } while (c.moveToNext());
            PromoObject ob = promosArrayList.get(0);
            //backgroundImageView.setImageBitmap(ob.backPhoto);
        }
        db.close();
    }

    private void setLogoImage(final PromoObject obj) {
        mImageViewLogo.setImageBitmap(obj.photo);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {
        /* ******** This is for Quote of the day (don't change)  ******** */
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
            mClient.close();
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

    @Override
    public void onPause() {
        super.onPause();
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
            if (mClient != null)
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