/*package com.ideaone.reveraproject1;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OldNewsFragment extends Fragment {

    private View V;
    private TextView news_title, news_snippet, news_link;
    private ImageView news_image;
    private ImageView next, prev;
    private LinearLayout news_main_ll;
    private WebView webView;
    private long yourmilliseconds;
    private Date resultdate;
    private SimpleDateFormat sdf_clock;
    private int num_of_news_entries, current_news_entry;
    private Bitmap thumbnail_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.old_news_fragment, container, false);
     //   news_side = (TextView) V.findViewById(R.id.news_side);
     //   news_side.setText(Html.fromHtml("<b>Daily News</b>"));
     //   news_side.setGravity(Gravity.CENTER);
       // news_side.setTextSize(35);

        news_title = (TextView) V.findViewById(R.id.news_title);
        news_snippet = (TextView) V.findViewById(R.id.news_snippet);
        news_title.setTextSize(48);
        news_title.setGravity(Gravity.CENTER);
        news_snippet.setTextSize(40);
        news_snippet.setGravity(Gravity.LEFT);
        news_link = (TextView) V.findViewById(R.id.news_link);

        news_image = (ImageView) V.findViewById(R.id.news_image);
        next = (ImageView) V.findViewById(R.id.news_side_next_news_button);
        next.setImageDrawable(getResources().getDrawable(R.drawable.side_right_arrow));
        prev = (ImageView) V.findViewById(R.id.news_side_prev_news_button);
        prev.setImageDrawable(getResources().getDrawable(R.drawable.side_left_arrow));
        news_main_ll = (LinearLayout) V.findViewById(R.id.news_main_view);
        new HttpGetTask().execute();
        return V;
    }

    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

        private final String URL = "http://ajax.googleapis.com/ajax/services/feed/load?v=1.0&num=16&q=http%3A%2F%2Fnews.google.ca%2Fnews%3Foutput%3Drss";

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
        protected void onPostExecute(final List<String> result) {
            if (null != mClient)
                mClient.close();
            if (result != null) {
                final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                news_title.setText(result.get(0));
                news_snippet.setText(Html.fromHtml(result.get(2)));
                Pattern p = Pattern.compile("src=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(result.get(1));
                if (m.find()) {
                    String url = ("http:" + m.group(1));
                    new ImageToDownload_full().execute(url);
                }

                news_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newsPopUp(result.get(0), result.get(3));
                    }
                });
                current_news_entry = 0;

                if (current_news_entry >= 0 && current_news_entry <= num_of_news_entries) {
                    if (current_news_entry <= num_of_news_entries - 2) {
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final int temp = ++current_news_entry;
                                if (current_news_entry >= num_of_news_entries) {
                                    current_news_entry = num_of_news_entries - 1;
                                    Toast.makeText(getActivity().getApplicationContext(), "no more news", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                news_title.setText(result.get(temp * 4));
                                news_snippet.setText(Html.fromHtml(result.get(temp * 4 + 2)));
                                Pattern p = Pattern.compile("src=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
                                Matcher m = p.matcher(result.get(temp * 4 + 1));
                                if (m.find()) {
                                    String url = ("http:" + m.group(1));
                                    new ImageToDownload_full().execute(url);
                                }
                                news_link.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        newsPopUp(result.get(temp * 4), result.get(temp * 4 + 3));
                                    }
                                });
                            }
                        });
                    } else {
                        next.setOnClickListener(null);
                    }

                    if (current_news_entry >= 0) {
                        prev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final int temp = --current_news_entry;
                                if (current_news_entry < 0) {
                                    current_news_entry = 0;
                                    Toast.makeText(getActivity().getApplicationContext(), "please press next button to see the next news", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                System.out.println(current_news_entry);
                                news_title.setText(result.get(temp * 4));
                                news_snippet.setText(Html.fromHtml(result.get(temp * 4 + 2)));
                                Pattern p = Pattern.compile("src=\"(.*?)\"", Pattern.CASE_INSENSITIVE);
                                Matcher m = p.matcher(result.get(temp * 4 + 1));
                                if (m.find()) {
                                    String url = ("http:" + m.group(1));
                                    new ImageToDownload_full().execute(url);
                                }
                                news_link.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        newsPopUp(result.get(temp * 4), result.get(temp * 4 + 3));
                                    }
                                });
                            }
                        });
                    } else {
                        prev.setOnClickListener(null);
                    }
                }

            } else{
                news_title.setText("No Wifi, Sorry for the inconvenience. Try again later.");
            }
        }
    }

    private class JSONResponseHandler implements ResponseHandler<List<String>> {

        private static final String RESPONSEDATA_TAG = "responseData";
        private static final String FEED_TAG = "feed";
        private static final String ENTRIES_TAG = "entries";
        private static final String LINK_TAG = "link";
        private static final String IMAGE_LINK_TAG = "";
        private static final String TITLE_TAG = "title";
        private static final String SNIPPET_TAG = "contentSnippet";
        private static final String CONTENT_TAG = "content";


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
                JSONObject responseData = responseObject
                        .getJSONObject(RESPONSEDATA_TAG);
                JSONObject feed = responseData.getJSONObject(FEED_TAG);
                JSONArray entries = feed.getJSONArray(ENTRIES_TAG);
                num_of_news_entries = entries.length();

                for (int i = 0; i < entries.length(); i++) {
                    JSONObject news = entries.getJSONObject(i);
                    result.add((String) news.get(TITLE_TAG));
                    result.add((String) news.get(CONTENT_TAG));
                    result.add("<br/>" + news.get(SNIPPET_TAG));
                    result.add((String) news.get(LINK_TAG));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }


    private class ImageToDownload_full extends AsyncTask<String, Void, Boolean> {
        int ViewID;

        @Override
        protected Boolean doInBackground(String... param) {
            thumbnail_image = downloadBitmap(param[0]);
            finishedTask();
            return true;

        }

        @Override
        protected void onPostExecute(Boolean df) {
            news_image.setImageBitmap(thumbnail_image);
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

    public void newsPopUp(String title, String url) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(title);

        WebView wv = new WebView(getActivity());

        WebSettings settings = wv.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);

        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

}
*/