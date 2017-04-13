package com.ideaone.reveraproject1;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements NewsDownload.Communicator {

    int flipTime = 1000 * 20;
    public static String NEWS_URL = "http://newsapi.org/v1/articles?source=cnn&sortBy=top&apiKey=4790b144de9446388f817e184dfc584b";
    NewsDownload downloadNews;

    TextView newsTitle;
    ViewFlipper newsFlipper;
    ImageView next, prev;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_news, container, false);

        newsFlipper = (ViewFlipper) V.findViewById(R.id.newsFlipper);
        newsTitle = (TextView) V.findViewById(R.id.newsTitle);
        next = (ImageView) V.findViewById(R.id.news_side_next_news_button);
        prev = (ImageView) V.findViewById(R.id.news_side_prev_news_button);

        newsFlipper.setInAnimation(getActivity().getApplicationContext(), android.R.anim.fade_in);
        newsFlipper.setOutAnimation(getActivity().getApplicationContext(), android.R.anim.fade_out);
        //  newsFlipper.setFlipInterval(flipTime);
        //  newsFlipper.setAutoStart(true);

        if (newsFlipper.getChildCount() > 0) {
            Log.e("Announce DELETE", " DELETE ALL VIEWs ");
            newsFlipper.removeAllViews();
        }
        if (isNetworkAvailable()) {
            downloadNews = new NewsDownload(this);
            downloadNews.execute();
        } else {
            newsTitle.setText(R.string.NoWifiNewsText);
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsFlipper.showNext();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsFlipper.showPrevious();
            }
        });
        return V;
    }

    public void createNewsView(final NewsObject obj) {
        LinearLayout parent = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout top = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout bottom = new LinearLayout(getActivity().getApplicationContext());

        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setGravity(Gravity.CENTER);

        top.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER);

        bottom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2));
        bottom.setOrientation(LinearLayout.HORIZONTAL);
        bottom.setGravity(Gravity.CENTER);

//children of top linearlayout

        FrameLayout imageFrameLayout = new FrameLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2);
        imageFrameLayout.setLayoutParams(lp1);

        LinearLayout textFrameLayout = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2);
        textFrameLayout.setLayoutParams(lp2);

        lp1.setMargins(20, 0, 0, 0);
        lp2.setMargins(20, 0, 20, 5);

        textFrameLayout.setOrientation(LinearLayout.VERTICAL);
        textFrameLayout.setGravity(Gravity.CENTER);

        ImageView iv = new ImageView(getActivity().getApplicationContext());
        TextView tv1Title = new TextView(getActivity().getApplicationContext());
        TextView tvReadMore = new TextView(getActivity().getApplicationContext());

        iv.setImageBitmap(obj.image);

        // iv.setScaleType(ImageView.ScaleType.FIT_XY);
        tv1Title.setText(obj.title);
        tv1Title.setTextSize(50);
        tv1Title.setTextColor(Color.parseColor("#000000"));

        tvReadMore.setText("Read more ..");
        tvReadMore.setTextSize(30);
        tvReadMore.setTextColor(Color.parseColor("#33ADFF"));

        imageFrameLayout.addView(iv);
        textFrameLayout.addView(tv1Title);
        textFrameLayout.addView(tvReadMore);

        top.addView(imageFrameLayout);
        top.addView(textFrameLayout);

        // bottom view

        LinearLayout descFrameLayout = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2);
        descFrameLayout.setLayoutParams(lp3);

        lp3.setMargins(50, 20, 50, 20);

        descFrameLayout.setOrientation(LinearLayout.VERTICAL);
        descFrameLayout.setGravity(Gravity.CENTER);

        TextView tv2Desc = new TextView(getActivity().getApplicationContext());

        tv2Desc.setText(obj.description);
        tv2Desc.setTextSize(40);
        tv2Desc.setTextColor(Color.parseColor("#000000"));

        descFrameLayout.addView(tv2Desc);

        bottom.setBackgroundResource(R.drawable.border_up);
        bottom.addView(descFrameLayout);

        // parent view
        parent.addView(top);
        parent.addView(bottom);

        newsFlipper.addView(parent);

        tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsPopUp(obj.title, obj.url);
            }
        });
    }

    //  newsPopUp("Hello","http://www.cnn.com/2017/01/10/politics/trump-cabinet-confirmation-hearings-live/index.html");

    Handler handler;
    Runnable runnable;

    public void newsPopUp(String title, String url) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(title);

        final WebView wv = new WebView(getActivity());

        WebSettings settings = wv.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        wv.clearCache(true);
        wv.clearHistory();

        wv.loadUrl(url);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        wv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        wv.setLongClickable(false);

        wv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("TOUCH!!!!!!!!", "TT");
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, 1000 * 60 * 2); // 2 min
                        break;
                }
                return false;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = alert.create();

        WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.START;
        wmlp.x = 10;   //x position
        wmlp.y = 10;   //y position
        alertDialog.getWindow().setAttributes(wmlp);

        alertDialog.show();
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 650);
        //alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // Hide after some seconds
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }
        };
        handler.postDelayed(runnable, 1000 * 30); // 30 sec

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });
    }

    public boolean isNetworkAvailable() {
        getActivity().getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
        //return activeNetworkInfo.isConnected();
    }

    @Override
    public void updateUI(ArrayList<NewsObject> photosArrayList) {
        if (isNetworkAvailable() && !photosArrayList.isEmpty()) {
            newsTitle.setText("");
            NewsObject obj1 = photosArrayList.get(0);
            if (!obj1.title.isEmpty()) {
                for (NewsObject obj : photosArrayList) {
                    createNewsView(obj);
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (downloadNews != null)
            downloadNews.cancel(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (downloadNews != null)
            downloadNews.cancel(true);
    }
}