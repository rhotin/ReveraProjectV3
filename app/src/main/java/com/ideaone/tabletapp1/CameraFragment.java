package com.ideaone.tabletapp1;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraFragment extends Fragment {

    String companySelected;
    String locationSelected;

    String URL;
    private static final int CAMERA_REQUEST = 1888;
    private View V;
    private Bitmap bitmap;
    Button upload;
    Button save;
    Button takeAnother;
    Uri mImageUri;
    String album_handle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.camera_fragment, container, false);

        final SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        locationSelected = prefs.getString("location", getString(R.string.RetirementLocation));
        companySelected = prefs.getString("company", getString(R.string.RetirementCompany));
        if (locationSelected.equals("leaside-14")) {
            locationSelected = "leaside";
        }

        URL = "http://" + companySelected + "/displays/" + locationSelected + "/albums.json";

        new getAlbumsThumbnail().execute();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Intent imgIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File photo;
        try {
            // place where to store camera taken picture
            photo = new File(Environment.getExternalStorageDirectory(),
                    "my-photo" + timeStamp + ".jpg");
            //photo.delete();
            mImageUri = Uri.fromFile(photo);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
        }

        upload = (Button) V.findViewById(R.id.upload_button);
        save = (Button) V.findViewById(R.id.save_button);
        takeAnother = (Button) V.findViewById(R.id.take_another_button);
        imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(imgIntent, CAMERA_REQUEST);

        takeAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save.setText("SAVE");
                upload.setText("UPLOAD");
                save.setBackgroundResource(android.R.drawable.btn_default);
                upload.setBackgroundResource(android.R.drawable.btn_default);
                takeAnother.setBackgroundResource(android.R.drawable.btn_default);
                Intent imgIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(imgIntent, CAMERA_REQUEST);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save.setText("Saved ✓");
                save.setBackgroundColor(getResources().getColor(R.color.side_menu_color));

            }
        });
        return V;
    }


    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public Bitmap grabImage(ImageView imageView) {

        getActivity().getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = getActivity().getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            imageView.setBackground(null);
            imageView.setImageBitmap(bitmap);
            return bitmap;
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "Failed to load", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        }

    }


    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            System.out.println("image captured");
            //Bundle extras = data.getExtras();
            //bitmap = (Bitmap) extras.get("data");
            ImageView preview = (ImageView) V.findViewById(R.id.preview);
            //preview.setImageBitmap(bitmap);
            bitmap = grabImage(preview);

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity().getApplicationContext(), "Uploading image to server",Toast.LENGTH_LONG).show();
                    new ImageUploader().execute();
                }
            });
        }
    }

    private class ImageUploader extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String result = "";

            // Client-side HTTP transport library
            HttpClient httpClient = new DefaultHttpClient();

            // using POST method
            HttpPost httpPostRequest = new HttpPost("http://" + companySelected + "/albums/" + album_handle + "/537e5b67c0af973c7900002c/add.json");
            Log.e("LOG", album_handle + "");
            try {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();
                ByteArrayBody bab = new ByteArrayBody(data, "Hirad.jpg");


                MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
                multiPartEntityBuilder.addPart("media", bab);
                //multiPartEntityBuilder.addPart("media", new StringBody("Hirad.jpg"));
                //multiPartEntityBuilder.addPart("web", new StringBody("yes"));
                //multiPartEntityBuilder.addPart("handle", new StringBody("live"));
                multiPartEntityBuilder.addPart("testkey", new StringBody("f2Kjf34mnf21jf2Kfwk32-aPl1sfWqa1"));
                httpPostRequest.setEntity(multiPartEntityBuilder.build());

                // Execute POST request to the given URL
                HttpResponse httpResponse = null;
                httpResponse = httpClient.execute(httpPostRequest);

                // receive response as inputStream
                InputStream inputStream = null;
                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
                return result;
            } catch (Exception e) {
                return null;
            }

            // return result;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Toast.makeText(getActivity().getApplicationContext(), "Uploading image to server", Toast.LENGTH_LONG).show();
            upload.setText("Uploading");
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                upload.setText("Uploaded ✓");
                upload.setBackgroundColor(getResources().getColor(R.color.side_menu_color));
                Toast.makeText(getActivity().getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                upload.setOnClickListener(null);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class getAlbumsThumbnail extends AsyncTask<Void, Void, List<String>> {

        //private final String URL = "http://revera.mxs-s.com/albums.json?dID=532c6331c0af979c16000d21";

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
            if (result != null)
                try {
                    album_handle = result.get(0);
                } catch (IndexOutOfBoundsException e) {

                }
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
                JSONObject last_album = data.getJSONObject(data.length() - 1);
                for (int i = 0; i < data.length(); i++) {
                    last_album = data.getJSONObject(i);
                    if (last_album.getString("title").equals("Recent Photos")) {
                        result.add(last_album.getString(HANDLE_TAG));
                    }
                }
                if (result.isEmpty()) {
                    last_album = data.getJSONObject(0);
                    result.add(last_album.getString(HANDLE_TAG));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

}