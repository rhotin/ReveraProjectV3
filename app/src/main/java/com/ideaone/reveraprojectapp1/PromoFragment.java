package com.ideaone.reveraprojectapp1;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromoFragment extends Fragment implements PromoDownload.Communicator {

    String locationSelected;

    public static String promoType = "Event";
    public static String promoType2 = "Additional Rec";
    int flipTime = 1000 * 60 * 60; //7000 / 1000 = 7 seconds
    int tempMinute = 0;
    int tempHour = 0;

    ArrayList<PromoObject> promosArrayList = new ArrayList<>();

  //  public static String ANNOUNCEMENT_URL = "";

    PromoDBAdapter db;
    VideoView vidView;
    ImageView vidHomeImage;

    PromoObject obj;

    float initialX;

    ViewFlipper mAnnouncementFlipper;
    PromoDownload downloadAnnouncement;

    ImageView left_arrow, right_arrow, backgroundImageView;

    public PromoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View V = inflater.inflate(R.layout.promo_fragment, container, false);

        mAnnouncementFlipper = (ViewFlipper) V.findViewById(R.id.viewFlipperAnnouncement);
        left_arrow = (ImageView) V.findViewById(R.id.left_arrow);
        right_arrow = (ImageView) V.findViewById(R.id.right_arrow);

        backgroundImageView = (ImageView) V.findViewById(R.id.BackgroundImageView);

        //     progressBar.setVisibility(View.VISIBLE);
        //messageText.setVisibility(View.GONE);

        db = new PromoDBAdapter(getActivity().getApplicationContext());

        // Set in/out flipping animations
        mAnnouncementFlipper.setInAnimation(getActivity().getApplicationContext(), android.R.anim.fade_in);
        mAnnouncementFlipper.setOutAnimation(getActivity().getApplicationContext(), android.R.anim.fade_out);
        //   mAnnouncementFlipper.setFlipInterval(flipTime);
        // mAnnouncementFlipper.setAutoStart(true);

        right_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     mAnnouncementFlipper.stopFlipping();
                mAnnouncementFlipper.showNext();
                //    mAnnouncementFlipper.startFlipping();
            }
        });

        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      mAnnouncementFlipper.stopFlipping();
                mAnnouncementFlipper.showPrevious();
                //      mAnnouncementFlipper.startFlipping();
            }
        });

        mAnnouncementFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (promosArrayList.size() <= mAnnouncementFlipper.getChildCount()) {
                    try {
                        if (mAnnouncementFlipper.getDisplayedChild() >= 0) {
                            if (mAnnouncementFlipper.getDisplayedChild() < promosArrayList.size() - 1)
                                obj = promosArrayList.get(mAnnouncementFlipper.getDisplayedChild() + 1);
                            else {
                                obj = promosArrayList.get(0);
                            }
                            Log.e("HOME Switch", "duration " + obj.length);
                            mAnnouncementFlipper.setFlipInterval(obj.length * 1000);
                        }
                        if (mAnnouncementFlipper.getDisplayedChild() >= 0 && mAnnouncementFlipper.getDisplayedChild() < promosArrayList.size()) {
                            Log.e("HOME PIC", "" + promosArrayList.size());
                            obj = promosArrayList.get(mAnnouncementFlipper.getDisplayedChild());
                            if (obj.backPhoto != null)
                                backgroundImageView.setImageBitmap(obj.backPhoto);
                            else
                                backgroundImageView.setImageBitmap(null);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mAnnouncementFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent touchEvent) {
                if (touchEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //      mAnnouncementFlipper.stopFlipping();
                    //     mAnnouncementFlipper.setAutoStart(false);
                    initialX = touchEvent.getX();
                    return true;
                }
                if (touchEvent.getAction() == MotionEvent.ACTION_UP) {
                    //      mAnnouncementFlipper.startFlipping();
                    float finalX = touchEvent.getX();
                    if (initialX > finalX) {
                        mAnnouncementFlipper.showNext();
                    } else if (initialX < finalX) {
                        mAnnouncementFlipper.showPrevious();
                    }
                }
                return false;
            }
        });

        Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);
        int hour = cal.get(Calendar.HOUR);

        try {
            db.open();
            Cursor c = db.getAllItems();
            if (c.moveToFirst()) {
                if (mAnnouncementFlipper.getChildCount() > 0) {
                    Log.e("Announce DELETE", " DELETE ALL VIEWs ");
                    mAnnouncementFlipper.removeAllViews();
                }
                getAllItems();
            }
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (isNetworkAvailable()) {
            Log.e("Announce PIC", " network ");
            if (hour != tempHour) {
                tempMinute = minute;
                tempHour = hour;
                db.open();
                db.resetDB();
                db.close();
            }
            //   messageText.setText("");
            downloadAnnouncement = new PromoDownload(this);
            downloadAnnouncement.execute();
            //   }
        } else {
            try {
                db.open();
                Cursor c = db.getAllItems();
                if (c.moveToFirst()) {

                    if (mAnnouncementFlipper.getChildCount() > 0) {
                        Log.e("Announce DELETE", " DELETE ALL VIEWs ");
                        mAnnouncementFlipper.removeAllViews();
                    }
                    getAllItems();
                }
                db.close();
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
        return V;
    }

    /*@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            db.open();
            Cursor c = db.getAllItems();
            if (c.moveToFirst()) {

                if (mAnnouncementFlipper.getChildCount() > 0) {
                    Log.e("Announce DELETE", " DELETE ALL VIEWs ");
                    mAnnouncementFlipper.removeAllViews();
                }
                getAllItems();
            }
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void updateUI(ArrayList<PromoObject> photosArrayList) {
        //photoListView.setVisibility(View.VISIBLE);
        //     progressBar.setVisibility(View.GONE);
        // messageText.setVisibility(View.VISIBLE);
        //AnnouncementsObject obj;
        if (isNetworkAvailable()) {
           /* db.open();
            db.resetDB();
            db.close();
            /*
            for (int i = 0; i < photosArrayList.size(); i++) {
                //  This will create dynamic image view and add them to ViewFlipper
                obj = photosArrayList.get(i);
                saveToAnnouncements(obj);
            }
            */

           /*
            for (PromoObject obj : photosArrayList) {
                saveToAnnouncements(obj);
            }
            */

            int viewIndex = mAnnouncementFlipper.getDisplayedChild();
            if (mAnnouncementFlipper.getChildCount() > 0) {
                Log.e("Announce DELETE", " DELETE ALL VIEWs ");
                mAnnouncementFlipper.removeAllViews();
            }
            getAllItems();
            mAnnouncementFlipper.setDisplayedChild(viewIndex);


        }

        //getAllItems();
        /*if (mAnnouncementFlipper.getChildCount() > 0) {
            mAnnouncementFlipper.removeAllViews();
        }

        for (int i = 0; i < photosArrayList.size(); i++) {
            //  This will create dynamic image view and add them to ViewFlipper
            obj = photosArrayList.get(i);
            setFlipperImage(obj);
            // noticeText.setText(Html.fromHtml(obj.htmlText));
        }*/
    }

    private void setFlipperImage(final PromoObject obj) {
        // Log.e("Announce PICT", "" + mAnnouncementFlipper.getDisplayedChild() + "-" + obj.background);
        // backgroundImageView.setImageBitmap(obj.backPhoto);
        Log.e("TESTING AddRec", "TEST" + obj.photo);
        if (obj.promoType.equalsIgnoreCase(promoType) || obj.promoType.equalsIgnoreCase(promoType2)) {
            switch (obj.type) {
                case "image":
                    ImageView image = new ImageView(getActivity().getApplicationContext());
                    image.setImageBitmap(obj.photo);
                    //image.setScaleType(ImageView.ScaleType.FIT_XY);
                    mAnnouncementFlipper.addView(image);
                    break;
                case "pdf":
                    ImageView imagePdf = new ImageView(getActivity().getApplicationContext());
                    imagePdf.setImageBitmap(obj.photo);
                    // imagePdf.setScaleType(ImageView.ScaleType.FIT_XY);
                    mAnnouncementFlipper.addView(imagePdf);
                    break;
                case "text":
                    final WebView text = new WebView(getActivity().getApplicationContext());
                    text.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            text.loadUrl("javascript:document.body.style.margin=\"20px 100px 20px 100px\"; void 0");
                        }
                    });
                    text.setWebChromeClient(new WebChromeClient() {
                    });
                    if (Build.VERSION.SDK_INT >= 19) {
                        text.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    } else {
                        text.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    }
                    text.clearCache(true);
                    text.clearHistory();
                    text.destroyDrawingCache();
                    text.getSettings().setDomStorageEnabled(true);
                    text.getSettings().setJavaScriptEnabled(true);
                    text.getSettings().setAppCacheEnabled(false);
                    text.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    text.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    text.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                    text.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return true;
                        }
                    });
                    text.setLongClickable(false);
                    text.setBackgroundColor(Color.TRANSPARENT);
                    text.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            text.loadDataWithBaseURL(null, obj.text, "text/html", "utf-8", null);
                        }
                    }, 300);
                    //text.loadDataWithBaseURL("http://static-a.condo.ideaone.tv/full/d7e01139c5a8ca92aeba3ea800739fbaaf9fe1ac.jpg", obj.htmlText, "text/html", "utf-8", null);
                    mAnnouncementFlipper.addView(text);
                    //    mHomeFlipper.setFlipInterval(obj.durationTime * 1000);
                    break;

                case "url":
                    final WebView textUrl = new WebView(getActivity().getApplicationContext());
                    textUrl.setWebViewClient(new WebViewClient());
                    textUrl.setWebChromeClient(new WebChromeClient() {
                    });
                    final String htmlUrl = obj.url;
                    textUrl.clearCache(true);
                    textUrl.clearHistory();
                    textUrl.destroyDrawingCache();
                    textUrl.getSettings().setDomStorageEnabled(true);
                    textUrl.getSettings().setJavaScriptEnabled(true);
                    textUrl.getSettings().setAppCacheEnabled(false);
                    textUrl.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    textUrl.getSettings().setPluginState(WebSettings.PluginState.ON);
                    textUrl.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return true;
                        }
                    });
                    textUrl.setLongClickable(false);
                    textUrl.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    if (Build.VERSION.SDK_INT >= 19) {
                        textUrl.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                        //textUrl.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    } else {
                        textUrl.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                    }
                    textUrl.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (htmlUrl.contains("http://") || htmlUrl.contains("https://")) {
                                textUrl.loadUrl(htmlUrl);
                            } else {
                                textUrl.loadUrl("http://" + htmlUrl);
                            }
                        }
                    }, 300);
                    mAnnouncementFlipper.addView(textUrl);
                    //    mHomeFlipper.setFlipInterval(obj.durationTime * 1000);
                    break;
                    /*
                case "url":
                    final String htmlUrl = obj.announceUrl;
                    createVideoView(htmlUrl);
                    break;
                    */
            }
        }
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
                setFlipperImage(obj);
            } while (c.moveToNext());
            PromoObject ob = promosArrayList.get(0);
            //backgroundImageView.setImageBitmap(ob.backPhoto);
        }
        db.close();
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
        if (downloadAnnouncement != null)
            downloadAnnouncement.cancel(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (downloadAnnouncement != null)
            downloadAnnouncement.cancel(true);
    }
}
