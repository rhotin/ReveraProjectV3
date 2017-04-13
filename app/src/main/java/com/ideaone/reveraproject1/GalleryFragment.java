/*package com.ideaone.reveraproject1;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GalleryFragment extends Fragment {

    String locationSelected = HomeFragment.locationSelected;

    private final String URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/albums.json?album=";

    View V;
    LinearLayout thumbnail_container, single_album_thmbnails_container;
    ImageView up_button, down_button;
    int num_of_messages;
    List<String> urls_thumbnail = new ArrayList<String>();
    List<String> urls_single_album_thumbnail = new ArrayList<String>();
    List<String> urls_full = new ArrayList<String>();
    ImageView Full_image_view;
    Bitmap full_image;
    ImageView[] tiles, upper_menu_tiles;

    int num_of_albums, albumID;

    TextView clock;
    long yourmilliseconds;
    Date resultdate;
    SimpleDateFormat sdf_clock;
    Bitmap[] images, single_album_thumbnails;

    ArrayList<String> allAlbumId = new ArrayList<>();

    GetAlbumsNames getUrls;
    getSingleAlbumThumbnail getSingleAlbumUrls;
    LoadThumbnailImagesForUpperMenu getthumbnailForUpperMenu;

    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.gallery_fragment_old, container, false);
        thumbnail_container = (LinearLayout) V.findViewById(R.id.thumbnail_scrollview_linearlayout);
        single_album_thmbnails_container = (LinearLayout) V.findViewById(R.id.thumbnail_main_scrollview_linearlayout);
        Full_image_view = (ImageView) V.findViewById(R.id.full_image);

        if (isNetworkAvailable()) {
            Toast.makeText(getActivity().getApplicationContext(), "loading the full size image, please wait!", Toast.LENGTH_SHORT).show();
            progress = new ProgressDialog(container.getContext());
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...\nMay take longer than usual...");
            progress.show();
            getUrls = new GetAlbumsNames();
            getUrls.execute();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(container.getContext()).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("No WiFi, Please contact staff for more information");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            hideNavBar();
                        }
                    });
            alertDialog.show();
        }

        // seeting up buttons
        up_button = (ImageView) V.findViewById(R.id.msg_up_button);
        up_button.setImageDrawable(getResources().getDrawable(R.drawable.up_button));
        down_button = (ImageView) V.findViewById(R.id.msg_down_button);
        down_button.setImageDrawable(getResources().getDrawable(R.drawable.down_button));

        return V;
    }

    ///////////////////////////////////////////////////
    ////////// loading album names ///////////////
    ///////////////////////////////////////////////////
    private class GetAlbumsNames extends AsyncTask<Void, Void, List<String>> {

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                HttpGet request = new HttpGet(URL);
                GetAlbumsThumbnails responseHandler = new GetAlbumsThumbnails();

                return mClient.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<String> result) {
            if (null != mClient)
                mClient.close();
            if (result != null) {
                final int[] gallery_number = new int[1];
                gallery_number[0] = 0;
                final TextView[] textViewArray = new TextView[result.size()];
                thumbnail_container.setWeightSum(result.size());
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                for (int loopCount = result.size() - 1; loopCount >= 0; loopCount--) {
                    textViewArray[loopCount] = new TextView(getActivity());
                    textViewArray[loopCount].setLayoutParams(lparams);
                    textViewArray[loopCount].setGravity(Gravity.CENTER);
                    textViewArray[loopCount].setText(result.get(loopCount));
                    textViewArray[loopCount].setTextSize(20);
                    textViewArray[loopCount].setBackground(getResources().getDrawable(R.drawable.message_unselected));
                    textViewArray[loopCount].setId(loopCount);
                    thumbnail_container.addView(textViewArray[loopCount]);
                    textViewArray[loopCount].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            single_album_thmbnails_container.removeAllViews();
                            getSingleAlbumUrls = new getSingleAlbumThumbnail();
                            albumID = v.getId();
                            getSingleAlbumUrls.execute(v.getId());
                            gallery_number[0] = v.getId();
                            for (int j = 0; j < result.size(); j++) {
                                textViewArray[j].setBackground(getResources().getDrawable(R.drawable.message_unselected));
                            }
                            textViewArray[v.getId()].setBackground(getResources().getDrawable(R.drawable.message_selected));
                        }
                    });
                }
                if (result.size() - 1 >= 0)
                    textViewArray[result.size() - 1].performClick();

                down_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unloudTasks();
                        if (gallery_number[0] <= 0) {
                            gallery_number[0] = 0;
                            Toast.makeText(getActivity().getApplicationContext(), "no more gallery", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        textViewArray[--gallery_number[0]].performClick();
                        for (int j = 0; j < num_of_albums; j++) {
                            if (j != gallery_number[0])
                                textViewArray[j].setBackground(getResources().getDrawable(R.drawable.message_unselected));
                        }
                    }
                });

                up_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        unloudTasks();
                        if (gallery_number[0] >= num_of_albums - 1) {
                            gallery_number[0] = num_of_albums - 1;
                            Toast.makeText(getActivity().getApplicationContext(), "no more gallery", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        textViewArray[++gallery_number[0]].performClick();
                        for (int j = 0; j < num_of_albums; j++) {
                            if (j != gallery_number[0])
                                textViewArray[j].setBackground(getResources().getDrawable(R.drawable.message_unselected));
                        }
                    }
                });
            }
        }
    }

    private class GetAlbumsThumbnails implements ResponseHandler<List<String>> {
        private static final String DATA_MAIN = "data";
        private static final String ALBUM_TAG = "title";
        private static final String ALBUM_ID = "ID";

        @Override
        public List<String> handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            List<String> result = new ArrayList<String>();
            String JSONResponse = new BasicResponseHandler()
                    .handleResponse(response);
            try {
                // Get top-level JSON Object - a Map
                JSONObject responseObject = (JSONObject) new JSONTokener(
                        JSONResponse).nextValue();

                // Extract value of "messages" key -- a List
                JSONArray data = responseObject.getJSONArray(DATA_MAIN);
                num_of_albums = data.length();
                for (int i = num_of_albums - 1; i >= 0; i--) {
                    result.add(data.getJSONObject(i).getString(ALBUM_TAG));
                    allAlbumId.add(data.getJSONObject(i).getString(ALBUM_ID));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    ///////////////////////////////////////////////////
    ////////// loading each albums ///////////////
    ///////////////////////////////////////////////////
    private class getSingleAlbumThumbnail extends AsyncTask<Integer, Void, List<String>> {

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Integer... params) {
            albumID = params[0];
            getUrls.cancel(true);

            try {
                HttpGet request = new HttpGet(URL + allAlbumId.get(albumID));
                GetSingleAlbumThumbnails responseHandler = new GetSingleAlbumThumbnails();

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
            if (mClient != null)
                mClient.close();
            if (result != null) {
                urls_single_album_thumbnail = null;
                urls_full = null;
                urls_single_album_thumbnail = new ArrayList<String>();
                urls_full = new ArrayList<String>();
                for (int i = 0; i < result.size(); i++) {
                    urls_single_album_thumbnail.add("http://static-a.reveraconnect.com/250/" + result.get(i) + ".jpg");
                    urls_full.add("http://static-a.reveraconnect.com/full/" + result.get(i) + ".jpg");
                }
                getthumbnailForUpperMenu = new LoadThumbnailImagesForUpperMenu();
                getthumbnailForUpperMenu.execute();
            }
        }
    }

    private class GetSingleAlbumThumbnails implements ResponseHandler<List<String>> {

        private static final String DATA_MAIN = "data";
        private static final String ALBUM_TAG = "handle";
        private static final String PHOTO_TAG = "photos";
        private static final String HANDLE_TAG = "handle";


        @Override
        public List<String> handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            List<String> result = new ArrayList<String>();
            String JSONResponse = new BasicResponseHandler()
                    .handleResponse(response);
            try {
                // Get top-level JSON Object - a Map
                JSONObject responseObject = (JSONObject) new JSONTokener(
                        JSONResponse).nextValue();

                // Extract value of "messages" key -- a List
                //JSONArray data = responseObject
                //        .getJSONArray(DATA_MAIN);
                //JSONArray albums = data.getJSONArray(ALBUM_TAG);
                JSONArray photos = responseObject.getJSONArray(PHOTO_TAG);

                // Iterate over list
                for (int idx = 0; idx < photos.length(); idx++) {
                    // Get single data - a Map
                    JSONObject messages_jobject = (JSONObject) photos.get(idx);
                    result.add((String) messages_jobject.get(HANDLE_TAG));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private class LoadThumbnailImagesForUpperMenu extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... param) {
            single_album_thumbnails = new Bitmap[urls_single_album_thumbnail.size()];
            num_of_messages = urls_single_album_thumbnail.size();
            for (int i = 0; i < num_of_messages; i++) {
                try {
                    single_album_thumbnails[i] = downloadBitmap(urls_single_album_thumbnail.get(i));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean df) {
            single_album_thmbnails_container.removeAllViews();
            upper_menu_tiles = null;
            try {
                upper_menu_tiles = new ImageView[urls_single_album_thumbnail.size()];
                // get reference to LayoutInflater

                LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                single_album_thmbnails_container.setWeightSum(urls_single_album_thumbnail.size());

                for (int i = 0; i < urls_single_album_thumbnail.size(); i++) {
                    //Creating copy of imageview by inflating it
                    ImageView image = (ImageView) inflater2.inflate(R.layout.thumbnail_layout, null);
                    image.setImageBitmap(single_album_thumbnails[i]);
                    image.setAlpha(125);
                    upper_menu_tiles[i] = image;
                    upper_menu_tiles[i].setId(i);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.1f);
                    upper_menu_tiles[i].setLayoutParams(params);
                    single_album_thmbnails_container.addView(upper_menu_tiles[i]);
                }
            } catch (Exception e) {
                Log.e("Exception", "" + e);
            }
            myHandler.sendEmptyMessage(0);

        }

        Handler myHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        getthumbnailForUpperMenu.cancel(true);
                        getSingleAlbumUrls.cancel(true);
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
        try {
            Full_image_view.setId(0);
            for (int i = 0; i < upper_menu_tiles.length; i++) {
                upper_menu_tiles[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upper_menu_tiles[v.getId()].setAlpha(255);
                        if (v.getId() > 0) {
                            upper_menu_tiles[Full_image_view.getId()].setAlpha(125);
                        }

                        ImageToDownload_full task = new ImageToDownload_full();
                        task.execute(v.getId());
                        try {
                            while (task.get() != true) {
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (full_image != null) {
                            ByteArrayOutputStream bao = new ByteArrayOutputStream();
                            full_image.compress(Bitmap.CompressFormat.JPEG, 15, bao);
                            Full_image_view.setImageBitmap(full_image);
                            Full_image_view.setId(v.getId());
                        }
                    }
                });
            }
            upper_menu_tiles[0].performClick();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ImageToDownload_full extends AsyncTask<Integer, Void, Boolean> {
        int ViewID;

        @Override
        protected Boolean doInBackground(Integer... param) {
            ViewID = param[0];
            full_image = downloadBitmap(urls_full.get(param[0]));
            //finishedTask();
            return true;

        }

        @Override
        protected void onPostExecute(Boolean df) {
            finishedTask();
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
                        try {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            entity.consumeContent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + " " + e.toString());
            } catch (OutOfMemoryError e) {
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + " " + e.toString());
            }
            return null;
        }

        public boolean finishedTask() {
            progress.dismiss();
            hideNavBar();
            return true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (full_image != null) {
            full_image.recycle();
        }
        thumbnail_container.removeAllViews();
        if (images != null)
            for (Bitmap image : images) {
                image.recycle();
            }
        System.gc();
    }

    public void hideNavBar() {
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
    }

    public boolean isNetworkAvailable() {
        getActivity().getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unloudTasks();
    }

    public void unloudTasks() {
        if (getUrls != null)
            getUrls.cancel(true);
        if (getSingleAlbumUrls != null)
            getSingleAlbumUrls.cancel(true);
        if (getthumbnailForUpperMenu != null)
            getthumbnailForUpperMenu.cancel(true);
    }
}
*/