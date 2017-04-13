import android.view.View;

/*package com.ideaone.aimproject;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class GalleryFragment2 extends Fragment {
    View V ;
    LinearLayout thumbnail_container,single_album_thmbnails_container ;
    ImageView up_button,down_button ;
    int num_of_messages ;
    List<String> urls_thumbnail =new ArrayList<String>();
    List<String> urls_single_album_thumbnail =new ArrayList<String>();
    List<String> urls_full =new ArrayList<String>();
    ImageView Full_image_view ;
    Bitmap full_image ;
    ImageView[] tiles,upper_menu_tiles ;

    int num_of_albums,albumID ;

    TextView clock ;
    long yourmilliseconds ;
    Date resultdate ;
    SimpleDateFormat sdf_clock ;
    Bitmap[] images,single_album_thumbnails ;

    getAlbumsThumbnail getUrls ;
    LoadThumbnailImagesForSideMenu getthumbnailForSideMenu ;
    getSingleAlbumThumbnail getSingleAlbumUrls ;
    LoadThumbnailImagesForUpperMenu getthumbnailForUpperMenu ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.gallery_fragment2, container, false);
        thumbnail_container = (LinearLayout) V.findViewById(R.id.thumbnail_scrollview_linearlayout) ;
        single_album_thmbnails_container = (LinearLayout) V.findViewById(R.id.thumbnail_main_scrollview_linearlayout) ;
        Full_image_view = (ImageView) V.findViewById(R.id.full_image) ;

        // setting time
        clock = (TextView) V.findViewById(R.id.system_clock) ;
        final android.os.Handler handler = new android.os.Handler();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        new updateClock().execute();
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000); //it executes this every 1000ms

        getUrls= new getAlbumsThumbnail() ;
        getUrls.execute();

        // seeting up buttons
        up_button = (ImageView) V.findViewById(R.id.msg_up_button) ;
        up_button.setImageDrawable(getResources().getDrawable(R.drawable.up_button));
        down_button = (ImageView) V.findViewById(R.id.msg_down_button) ;
        down_button.setImageDrawable(getResources().getDrawable(R.drawable.down_button));

        return V ;
    }

    private class updateClock extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params){
            yourmilliseconds = System.currentTimeMillis();
            sdf_clock = new SimpleDateFormat("EEE MMM dd\n K:mm a");
            resultdate = new Date(yourmilliseconds);
            return true ;
        }

        @Override
        protected void onPostExecute (Boolean result){
            clock.setText(sdf_clock.format(resultdate));
        }
    }

    ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////
    ////////// loading album thumbnails ///////////////
    ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////
    private class getAlbumsThumbnail extends AsyncTask<Void, Void, List<String>> {

        private final String URL = "http://revera.mxs-s.com/albums.json?dID=532c6331c0af979c16000d21" ;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params) {
            HttpGet request = new HttpGet(URL);
            GetAlbumsThumbnails responseHandler = new GetAlbumsThumbnails();
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
            for (int i=0;i<result.size();i++) {
                urls_thumbnail.add("http://static-a.reveraconnect.com/250/" + result.get(i) + ".jpg") ;
            }
            getthumbnailForSideMenu = new LoadThumbnailImagesForSideMenu() ;
            getthumbnailForSideMenu.execute() ;
        }
    }

    private class GetAlbumsThumbnails implements ResponseHandler<List<String>> {

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
                JSONArray data = responseObject.getJSONArray(DATA_MAIN);
                num_of_albums = data.length() ;
                //System.out.println(num_of_albums);
                //JSONArray albums = data.getJSONArray(ALBUM_TAG);
                for (int i=0 ; i<num_of_albums;i++) {
                    JSONArray photos = data.getJSONObject(i).getJSONArray(PHOTO_TAG);
                    JSONObject thumbnail = (JSONObject) photos.get(0);
                    result.add((String) thumbnail.get(HANDLE_TAG)) ;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }


    private class LoadThumbnailImagesForSideMenu extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... param) {
            images = new Bitmap[urls_thumbnail.size()] ;
            num_of_messages = urls_thumbnail.size() ;
            for (int i=0; i<num_of_messages ; i++){
                images[i] = downloadBitmap(urls_thumbnail.get(i)) ;
            }
            return true ;

        }

        @Override
        protected void onPostExecute(Boolean df) {
            tiles = new ImageView[images.length];
            // get reference to LayoutInflater
            LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            thumbnail_container.setWeightSum(images.length);

            for(int i = 0; i<images.length; i++) {
                //Creating copy of imageview by inflating it
                ImageView image = (ImageView) inflater2.inflate(R.layout.thumbnail_layout, null);
                image.setImageBitmap(images[i]);
                image.setBackground(getResources().getDrawable(R.drawable.message_unselected));
                tiles[i] = image;
                tiles[i].setId(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,0.2f);
                tiles[i].setLayoutParams(params);
                thumbnail_container.addView(tiles[i]);
                tiles[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        albumID = v.getId() ;
                        single_album_thmbnails_container.removeAllViews();
                        getSingleAlbumUrls = new getSingleAlbumThumbnail();
                        getSingleAlbumUrls.execute(v.getId()) ;
                    }
                });
            }

            myHandler.sendEmptyMessage(0);

        }

        Handler myHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        getthumbnailForSideMenu.cancel(true) ;
                        getUrls.cancel(true) ;
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






    ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////
    ////////// loading each albums ///////////////
    ///////////////////////////////////////////////////
    ///////////////////////////////////////////////////
    private class getSingleAlbumThumbnail extends AsyncTask<Integer, Void, List<String>> {

        private final String URL = "http://revera.mxs-s.com/albums.json?dID=532c6331c0af979c16000d21" ;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Integer... params) {
            albumID = params[0] ;
            HttpGet request = new HttpGet(URL);
            GetSingleAlbumThumbnails responseHandler = new GetSingleAlbumThumbnails();
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
            for (int i=0;i<result.size();i++) {
                System.out.println("http://static-a.reveraconnect.com/250/" + result.get(i) + ".jpg");
                urls_single_album_thumbnail.add("http://static-a.reveraconnect.com/250/" + result.get(i) + ".jpg") ;
                urls_full.add("http://static-a.reveraconnect.com/full/" + result.get(i)+ ".jpg") ;
            }
            getthumbnailForUpperMenu = new LoadThumbnailImagesForUpperMenu() ;
            getthumbnailForUpperMenu.execute() ;
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
                JSONArray data = responseObject
                        .getJSONArray(DATA_MAIN);
                //JSONArray albums = data.getJSONArray(ALBUM_TAG);
                JSONArray photos = data.getJSONObject(albumID).getJSONArray(PHOTO_TAG);



                // Iterate over list
                for (int idx = 0; idx < photos.length(); idx++) {
                    // Get single data - a Map
                    JSONObject messages_jobject = (JSONObject) photos.get(idx);
                    result.add((String) messages_jobject.get(HANDLE_TAG)) ;
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
            System.out.println(urls_single_album_thumbnail.size());
            single_album_thumbnails = new Bitmap[urls_single_album_thumbnail.size()] ;
            num_of_messages = urls_single_album_thumbnail.size() ;
            for (int i=0; i<num_of_messages ; i++){
                single_album_thumbnails[i] = downloadBitmap(urls_single_album_thumbnail.get(i)) ;
            }
            return true ;

        }

        @Override
        protected void onPostExecute(Boolean df) {
            upper_menu_tiles = new ImageView[urls_single_album_thumbnail.size()];
            // get reference to LayoutInflater
            LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            single_album_thmbnails_container.setWeightSum(urls_single_album_thumbnail.size());

            for(int i = 0; i<urls_single_album_thumbnail.size(); i++) {
                //Creating copy of imageview by inflating it
                ImageView image = (ImageView) inflater2.inflate(R.layout.thumbnail_layout, null);
                image.setImageBitmap(single_album_thumbnails[i]);
                image.setBackground(getResources().getDrawable(R.drawable.message_unselected));
                upper_menu_tiles[i] = image;
                upper_menu_tiles[i].setId(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,0.1f);
                upper_menu_tiles[i].setLayoutParams(params);
                single_album_thmbnails_container.addView(upper_menu_tiles[i]);
            }
            myHandler.sendEmptyMessage(0);

        }

        Handler myHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        getthumbnailForUpperMenu.cancel(true) ;
                        getSingleAlbumUrls.cancel(true) ;
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


    private void loadFullImage (){
        for (int i=0 ; i<tiles.length ; i++) {
            tiles[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "loading the full size image, please wait!", Toast.LENGTH_SHORT).show();
                    ImageToDownload_full task = new ImageToDownload_full() ;
                    task.execute(v.getId());
                    try{
                        while (task.get() != true){
                        }
                    } catch (ExecutionException e){
                        e.printStackTrace();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    full_image.compress(Bitmap.CompressFormat.JPEG,70,bao) ;
                    Full_image_view.setImageBitmap(full_image);
                }
            });
        }
        tiles[0].performClick();

    }


    private class ImageToDownload_full extends AsyncTask<Integer, Void, Boolean> {
        int ViewID ;

        @Override
        protected Boolean doInBackground(Integer... param) {
            ViewID = param[0] ;
            full_image = downloadBitmap(urls_full.get(param[0])) ;
            finishedTask() ;
            return true ;

        }

        @Override
        protected void onPostExecute(Boolean df) {
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

        public boolean finishedTask(){
            return true ;
        }
    }

    /*@Override
    public void onStop(){
        super.onStop();
        if (full_image != null) {
            full_image.recycle();
        }
        thumbnail_container.removeAllViews();
        for (int i=0;i<images.length;i++){
            images[i].recycle();
        }
        System.gc();
    }////////////////////////


    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getUrls.cancel(true);
        getthumbnailForSideMenu.cancel(true);
        getSingleAlbumUrls.cancel(true);
        getthumbnailForUpperMenu.cancel(true);
    }

}
*/