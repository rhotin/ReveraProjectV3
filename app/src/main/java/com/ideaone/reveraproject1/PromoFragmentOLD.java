/*package com.ideaone.reveraproject1;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PromoFragmentOLD extends Fragment {

    String locationSelected = HomeFragment.locationSelected.equalsIgnoreCase("") ?
            "leaside" : HomeFragment.locationSelected;

    static String URL;

    View V;
    LinearLayout thumbnail_container;
    ImageView up_button, down_button;
    int num_of_messages;
    List<String> urls_thumbnail = new ArrayList<String>();
    List<String> urls_full = new ArrayList<String>();
    ImageView Full_image_view;
    Bitmap full_image;
    ImageView[] tiles;

    TextView textViewNoData;
    Date resultdate, requested_date;
    HttpGetTask getUrls;

    SimpleDateFormat sdf_date;
    GregorianCalendar calendar;
    int dateCounter = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.promo_fragment_old, container, false);
        thumbnail_container = (LinearLayout) V.findViewById(R.id.thumbnail_scrollview_linearlayout);
        Full_image_view = (ImageView) V.findViewById(R.id.full_image);

        textViewNoData = (TextView) V.findViewById(R.id.textViewNoData);

        // seeting up buttons
        up_button = (ImageView) V.findViewById(R.id.msg_up_button);
        up_button.setImageDrawable(getResources().getDrawable(R.drawable.up_button));
        down_button = (ImageView) V.findViewById(R.id.msg_down_button);
        down_button.setImageDrawable(getResources().getDrawable(R.drawable.down_button));

        sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, dateCounter);
        requested_date = calendar.getTime();
         URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/promos.json?date="
                + sdf_date.format(requested_date) + "&nohtml=1";

        if (isNetworkAvailable()) {
            getUrls = new HttpGetTask();
            getUrls.execute();
            textViewNoData.setText("Loading...");
        } else {
            textViewNoData.setText("No Wifi, Please contact staff for more information");
        }
        return V;
    }

    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {
        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return mClient.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (null != mClient)
                mClient.close();
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    String first = result.get(i).substring(0, 33);
                    String imageID = result.get(i).substring(39, result.get(i).length());
                    String final_url = first + "/250/" + imageID;
                    urls_thumbnail.add(final_url);
                    urls_full.add(result.get(i));
                }
                new ImageToDownload().execute();
            } else {
                textViewNoData.setText("No Promos");
            }
        }
    }

    private class JSONResponseHandler implements ResponseHandler<List<String>> {
        private static final String PROMOS_MAIN = "promos";
        private static final String PHOTO_TAG = "photo";

        @Override
        public List<String> handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            List<String> result = new ArrayList<String>();
            String JSONResponse = new BasicResponseHandler()
                    .handleResponse(response);
            try {
                // Get top-level JSON Object - a Map 
                JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
                // Extract value of "messages" key -- a List 
                JSONArray messages = responseObject.getJSONArray(PROMOS_MAIN);
                num_of_messages = messages.length();

                // Iterate over list
                for (int idx = 0; idx < messages.length(); idx++) {

                    // Get single data - a Map
                    JSONObject messages_jobject = (JSONObject) messages.get(idx);
                    result.add((String) messages_jobject.get(PHOTO_TAG));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private class ImageToDownload extends AsyncTask<Void, Void, Boolean> {
        Bitmap[] images;

        @Override
        protected Boolean doInBackground(Void... param) {
            images = new Bitmap[urls_thumbnail.size()];
            num_of_messages = urls_thumbnail.size();
            for (int i = 0; i < num_of_messages; i++) {
                images[i] = downloadBitmap(urls_thumbnail.get(i));
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean df) {
            tiles = new ImageView[images.length];
            // get reference to LayoutInflater
            LayoutInflater inflater2 = null;
            try {
                inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            } catch (NullPointerException e) {
                onDetach();
            }
            thumbnail_container.setWeightSum(images.length);

            for (int i = 0; i < images.length; i++) {
                //Creating copy of imageview by inflating it
                ImageView image;
                if (inflater2 != null) {
                    image = (ImageView) inflater2.inflate(R.layout.thumbnail_layout, null);
                    image.setImageBitmap(images[i]);
                    tiles[i] = image;
                    tiles[i].setId(i);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.2f);
                    tiles[i].setLayoutParams(params);
                    tiles[i].setAlpha(125);
                    thumbnail_container.addView(tiles[i]);
                }
            }
            myHandler.sendEmptyMessage(0);
        }

        Handler myHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        loadFullImage();
                        break;
                    default:
                        break;
                }
            }
        };


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
    }

    private void loadFullImage() {
        final int[] gallery_number = new int[1];
        gallery_number[0] = 0;
        for (ImageView tile : tiles) {
            if (tile != null)
                tile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tiles[v.getId()].setAlpha(255);
                        for (int j = 0; j < tiles.length; j++) {
                            if (j != v.getId())
                                tiles[j].setAlpha(125);
                        }
                        //  Toast.makeText(getActivity().getApplicationContext(), "loading the full size image, please wait!", Toast.LENGTH_SHORT).show();
                        ImageToDownload_full task = new ImageToDownload_full();
                        task.execute(v.getId());
                        gallery_number[0] = v.getId();
                        try {
                            while (task.get() != true) {
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Full_image_view.setImageBitmap(full_image);

                    }
                });
        }
        if (tiles.length > 0) {
            if (tiles[0] != null) {
                tiles[0].performClick();
            }
            down_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gallery_number[0] >= tiles.length - 1) {
                        gallery_number[0] = tiles.length - 1;
                        Toast.makeText(getActivity().getApplicationContext(), "no more promos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tiles[++gallery_number[0]].performClick();
                    for (int j = 0; j < tiles.length; j++) {
                        if (j != gallery_number[0])
                            tiles[j].setAlpha(125);
                    }
                }
            });
            up_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gallery_number[0] <= 0) {
                        gallery_number[0] = 0;
                        Toast.makeText(getActivity().getApplicationContext(), "no more promos", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tiles[--gallery_number[0]].performClick();
                    for (int j = 0; j < tiles.length; j++) {
                        if (j != gallery_number[0])
                            tiles[j].setAlpha(125);
                    }
                }
            });
        } else {
            try {
                Toast.makeText(getActivity().getApplicationContext(), "No promos", Toast.LENGTH_LONG).show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private class ImageToDownload_full extends AsyncTask<Integer, Void, Boolean> {
        int ViewID;

        @Override
        protected Boolean doInBackground(Integer... param) {
            ViewID = param[0];
            full_image = downloadBitmap(urls_full.get(param[0]));
            finishedTask();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean df) {
        }

        private Bitmap downloadBitmap(String url) {
            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();
            Bitmap bitmap;

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
                        bitmap = BitmapFactory.decodeStream(inputStream);
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
            } catch (OutOfMemoryError e) {
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

    public boolean isNetworkAvailable() {
        getActivity().getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (full_image != null) {
            full_image.recycle();
        }
        thumbnail_container.removeAllViews();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getUrls != null)
            getUrls.cancel(true);
    }
}
*/