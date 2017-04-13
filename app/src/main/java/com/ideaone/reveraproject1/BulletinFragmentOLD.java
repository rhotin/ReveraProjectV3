/*package com.ideaone.reveraproject1;


import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BulletinFragmentOLD extends Fragment {

    String locationSelected = HomeFragment.locationSelected;

    int itemsToStore = 30;

    private final String URL = "http://revera.mxs-s.com/displays/" + locationSelected + "/promos.json?date=2015-10-13&nohtml=1";

    TextView msg1_view;
    TextView msg1_view_title;
    ImageView up_button, down_button;
    private static int num_of_messages = 0;
    LinearLayout msg_tab;
    View V;
    int loopCount = 0;
    TextView clock;
    long yourmilliseconds;
    Date resultdate;
    SimpleDateFormat sdf_clock;
    HttpGetTask httpGetTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.bulletin_fragment_old, container, false);
        msg1_view = (TextView) V.findViewById(R.id.msg1);
        msg1_view_title = (TextView) V.findViewById(R.id.msg1_title);

        msg_tab = (LinearLayout) V.findViewById(R.id.msg_tab);

        // seeting up buttons
        up_button = (ImageView) V.findViewById(R.id.msg_up_button);
        up_button.setImageDrawable(getResources().getDrawable(R.drawable.up_button));
        down_button = (ImageView) V.findViewById(R.id.msg_down_button);
        down_button.setImageDrawable(getResources().getDrawable(R.drawable.down_button));

        if(isNetworkAvailable()) {
            httpGetTask = new HttpGetTask();
            httpGetTask.execute();
        }else{
            msg1_view.setText("No Wifi, Please contact staff for more information");
        }
        return V;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (httpGetTask != null)
            httpGetTask.cancel(true);
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
        protected void onPostExecute(final List<String> result) {
            if (null != mClient)
                mClient.close();
            if (result != null) {
                final int[] msg_number = new int[1];
                msg_number[0] = 0;

                if (num_of_messages != 0) {
                    msg1_view_title.setText("");
                    msg1_view.setText("Select a message");

                    final TextView[] textViewArray = new TextView[num_of_messages];
                    msg_tab.setWeightSum(num_of_messages);
                    LayoutParams lparams = new LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
                    final int count = 3;
                    for (loopCount = 0; loopCount < num_of_messages; loopCount++) {
                        textViewArray[loopCount] = new TextView(getActivity());
                        textViewArray[loopCount].setLayoutParams(lparams);
                        textViewArray[loopCount].setGravity(Gravity.CENTER);
                        textViewArray[loopCount].setText(Html.fromHtml(result.get(loopCount * count)));
                        textViewArray[loopCount].setTextSize(25);
                        textViewArray[loopCount].setBackground(getResources().getDrawable(R.drawable.message_unselected));
                        textViewArray[loopCount].setId(loopCount);
                        textViewArray[loopCount].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                v.setBackground(getResources().getDrawable(R.drawable.message_selected));
                                msg1_view_title.setText(Html.fromHtml("<b><u>" + result.get(v.getId() * count) + "</u></b>"));
                                msg1_view.setText(Html.fromHtml("<b>" + result.get(v.getId() * count + 1) + "</b>"));
                                msg_number[0] = v.getId();
                                for (int j = 0; j < num_of_messages; j++) {
                                    if (j != v.getId())
                                        textViewArray[j].setBackground(getResources().getDrawable(R.drawable.message_unselected));
                                }
                            }
                        });

                        //textViewArray[loopCount].setFocusableInTouchMode(true);
                        msg_tab.addView(textViewArray[loopCount]);
                    }
                    if (textViewArray[0] != null) {
                        textViewArray[0].performClick();
                    }
                    down_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (msg_number[0] >= num_of_messages - 1) {
                                msg_number[0] = num_of_messages - 1;
                                Toast.makeText(getActivity().getApplicationContext(), "no more messages", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            textViewArray[++msg_number[0]].performClick();
                            for (int j = 0; j < num_of_messages; j++) {
                                if (j != msg_number[0])
                                    textViewArray[j].setBackground(getResources().getDrawable(R.drawable.message_unselected));
                            }
                        }
                    });

                    up_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (msg_number[0] <= 0) {
                                msg_number[0] = 0;
                                Toast.makeText(getActivity().getApplicationContext(), "no more messages", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            textViewArray[--msg_number[0]].performClick();
                            for (int j = 0; j < num_of_messages; j++) {
                                if (j != msg_number[0])
                                    textViewArray[j].setBackground(getResources().getDrawable(R.drawable.message_unselected));
                            }
                        }
                    });
                } else {
                    msg1_view.setText("No Messages");
                }
            } else {
                msg1_view.setText("No Messages");
            }
        }
    }

    private class JSONResponseHandler implements ResponseHandler<List<String>> {

        private static final String MESSAGE_TAG_MAIN = "promos";
        private static final String MESSAGE_TAG = "text";
        private static final String NAME_TAG = "name";
        private static final String DATE_TAG = "created";

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
                JSONArray messages = responseObject
                        .getJSONArray(MESSAGE_TAG_MAIN);
                num_of_messages = 0;

                // Iterate over list
                for (int idx = 0; idx < messages.length(); idx++) {

                    // Get single data - a Map
                    JSONObject messages_jobject = (JSONObject) messages.get(idx);

                    if (messages_jobject.has(MESSAGE_TAG)) {
                        result.add((String) messages_jobject.get(NAME_TAG));
                        result.add((String) messages_jobject.get(MESSAGE_TAG));
                        result.add((String) messages_jobject.get(DATE_TAG));
                        num_of_messages++;
                    }
                }
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

*/