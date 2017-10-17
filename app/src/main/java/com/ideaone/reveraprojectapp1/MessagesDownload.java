package com.ideaone.reveraprojectapp1;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessagesDownload extends AsyncTask<Void, Integer, Void> {
    ArrayList<PromoObject> objectArrayList = new ArrayList<>();
    Communicator context;

    MessagesDBAdapter db;

    MessagesDownload(MessagesFragment c) {
        this.context = c;
        db = new MessagesDBAdapter(c.getActivity().getApplicationContext());
    }

    @Override
    protected Void doInBackground(Void... params) {
        URL theUrl;
        try {
            theUrl = new URL("http://" + HomeFragment.companySelected + "/displays/" + HomeFragment.locationSelected + "/promos.json");
            //theUrl = new URL("http://revera.mxs-s.com/displays/" + HomeFragment.locationSelected + "/promos.json?location=kiosk");
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
            String announcement_json = reader.readLine();

            JSONObject announcement_object = new JSONObject(announcement_json);

            JSONArray data_arr = announcement_object.getJSONArray("promos");
            //JSONArray data_arr = announcement_object.getJSONArray("promos");

            int totalImages = data_arr.length();
            for (int i = 0; i < totalImages; i++) {
                String creatorID = "";
                String created = "";
                String name = "";
                String zone = "";
                String displayID = "";
                int length = 0;
                int priority = 0;
                String dateStart = "";
                String dateEnd = "";
                String web = "";
                String display = "";
                String calendar = "";
                String bulletin = "";
                String kiosk = "";
                String type = "image";
                String promoType = "Event";
                String modified = "";
                int showMonday = 0;
                int showTuesday = 0;
                int showWednesday = 0;
                int showThursday = 0;
                int showFriday = 0;
                int showSaturday = 0;
                int showSunday = 0;
                String text = "";
                String url = "";
                Bitmap bmp = null;
                Bitmap backgroundBmp = null;
                if (data_arr.getJSONObject(i).has("creatorID")) {
                    creatorID = data_arr.getJSONObject(i).getString("creatorID");
                }
                if (data_arr.getJSONObject(i).has("created")) {
                    created = data_arr.getJSONObject(i).getString("created");
                }
                if (data_arr.getJSONObject(i).has("name")) {
                    name = data_arr.getJSONObject(i).getString("name");
                }
                if (data_arr.getJSONObject(i).has("zone")) {
                    zone = data_arr.getJSONObject(i).getString("zone");
                }
                if (data_arr.getJSONObject(i).has("displayID")) {
                    displayID = data_arr.getJSONObject(i).getString("displayID");
                }
                if (data_arr.getJSONObject(i).has("length")) {
                    length = data_arr.getJSONObject(i).getInt("length");
                }
                if (data_arr.getJSONObject(i).has("priority")) {
                    priority = data_arr.getJSONObject(i).getInt("priority");
                }
                if (data_arr.getJSONObject(i).has("dateStart")) {
                    dateStart = data_arr.getJSONObject(i).getString("dateStart");
                }
                if (data_arr.getJSONObject(i).has("dateEnd")) {
                    dateEnd = data_arr.getJSONObject(i).getString("dateEnd");
                }
                if (data_arr.getJSONObject(i).has("web")) {
                    web = data_arr.getJSONObject(i).getString("web");
                }
                if (data_arr.getJSONObject(i).has("display")) {
                    display = data_arr.getJSONObject(i).getString("display");
                }
                if (data_arr.getJSONObject(i).has("calendar")) {
                    calendar = data_arr.getJSONObject(i).getString("calendar");
                }
                if (data_arr.getJSONObject(i).has("messages")) {
                    bulletin = data_arr.getJSONObject(i).getString("messages");
                }
                if (data_arr.getJSONObject(i).has("kiosk")) {
                    kiosk = data_arr.getJSONObject(i).getString("kiosk");
                }
                if (data_arr.getJSONObject(i).has("type")) {
                    type = data_arr.getJSONObject(i).getString("type");
                }
                if (data_arr.getJSONObject(i).has("promoType")) {
                    promoType = data_arr.getJSONObject(i).getString("promoType");
                }
                if (data_arr.getJSONObject(i).has("modified")) {
                    modified = data_arr.getJSONObject(i).getString("modified");
                }
                if (data_arr.getJSONObject(i).has("showMonday")) {
                    if (!data_arr.getJSONObject(i).get("showMonday").equals(""))
                        showMonday = data_arr.getJSONObject(i).getInt("showMonday");
                }
                if (data_arr.getJSONObject(i).has("showTuesday")) {
                    if (!data_arr.getJSONObject(i).get("showTuesday").equals(""))
                        showTuesday = data_arr.getJSONObject(i).getInt("showTuesday");
                }
                if (data_arr.getJSONObject(i).has("showWednesday")) {
                    if (!data_arr.getJSONObject(i).get("showWednesday").equals(""))
                        showWednesday = data_arr.getJSONObject(i).getInt("showWednesday");
                }
                if (data_arr.getJSONObject(i).has("showThursday")) {
                    if (!data_arr.getJSONObject(i).get("showThursday").equals(""))
                        showThursday = data_arr.getJSONObject(i).getInt("showThursday");
                }
                if (data_arr.getJSONObject(i).has("showFriday")) {
                    if (!data_arr.getJSONObject(i).get("showFriday").equals(""))
                        showFriday = data_arr.getJSONObject(i).getInt("showFriday");
                }
                if (data_arr.getJSONObject(i).has("showSaturday")) {
                    if (!data_arr.getJSONObject(i).get("showSaturday").equals(""))
                        showSaturday = data_arr.getJSONObject(i).getInt("showSaturday");
                }
                if (data_arr.getJSONObject(i).has("showSunday")) {
                    if (!data_arr.getJSONObject(i).get("showSunday").equals(""))
                        showSunday = data_arr.getJSONObject(i).getInt("showSunday");
                }

                if (type.equalsIgnoreCase("image") || type.equalsIgnoreCase("pdf")) {
                    String image_url = data_arr.getJSONObject(i).getString("photo");
                    URL downloadURL = new URL(image_url);
                    HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
                    InputStream inputStreamI = conn.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inJustDecodeBounds = true;
                    //BitmapFactory.decodeStream(inputStream, null, options);
                    options.inSampleSize = calculateInSampleSize(options, 300, 300);
                    options.inScaled = false;
                    //options.inJustDecodeBounds = false;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    try {
                        bmp = BitmapFactory.decodeStream(inputStreamI, null, options);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                } else if (type.equalsIgnoreCase("text")) {
                    String image_url = data_arr.getJSONObject(i).getString("background");
                    if (!image_url.equalsIgnoreCase("null")) {
                        URL downloadURL = new URL(image_url);
                        HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
                        InputStream inputStreamT = conn.getInputStream();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        //options.inJustDecodeBounds = true;
                        //BitmapFactory.decodeStream(inputStream, null, options);
                        options.inScaled = false;
                        options.inSampleSize = calculateInSampleSize(options, 200, 200);
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        //options.inJustDecodeBounds = false;
                        try {
                            backgroundBmp = BitmapFactory.decodeStream(inputStreamT, null, options);
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                        }
                    }

                    text = data_arr.getJSONObject(i).getString("text");
                } else if (type.equalsIgnoreCase("url")) {
                    url = data_arr.getJSONObject(i).getString("url");
                }

                if ((promoType.equalsIgnoreCase(MessagesFragment.promoType) || promoType.equalsIgnoreCase(MessagesFragment.promoType2))
                        && kiosk.equalsIgnoreCase("yes")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                    Date nowT = new Date(System.currentTimeMillis());
                    String now = sdf.format(nowT);
                    try {
                        if (sdf.parse(dateStart).compareTo(sdf.parse(now)) <= 0 &&
                                sdf.parse(dateEnd).compareTo(sdf.parse(now)) >= 0) {
                            PromoObject obj = new PromoObject(creatorID, created, name,
                                    zone, displayID, length, priority, dateStart, dateEnd, web,
                                    display, calendar, bulletin, kiosk, type, promoType, modified, showMonday,
                                    showTuesday, showWednesday, showThursday, showFriday, showSaturday,
                                    showSunday, url, text, bmp, backgroundBmp);
                            objectArrayList.add(obj);
                            saveToDB(obj);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //publishProgress((int) (((i + 1.0) / totalImages) * 100.0));
            }
        } catch (MalformedURLException | UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveToDB(PromoObject obj) {
        byte[] imgStream = new byte[0];
        if (obj.photo != null) {
            try {
                Bitmap photo = obj.photo;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                imgStream = stream.toByteArray();
                //stream.reset();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        byte[] imgStreamBack = new byte[0];
        if (obj.backPhoto != null) {
            try {
                Bitmap bmpBack = obj.backPhoto;
                ByteArrayOutputStream streamBack = new ByteArrayOutputStream();
                bmpBack.compress(Bitmap.CompressFormat.JPEG, 90, streamBack);
                imgStreamBack = streamBack.toByteArray();
                //streamBack.reset();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        try {
            db.open();
            Cursor c = db.getItem(obj.creatorID, obj.created, obj.name, obj.zone, obj.displayID, obj.length, obj.priority,
                    obj.dateStart, obj.dateEnd, obj.web, obj.display, obj.calendar, obj.bulletin, obj.kiosk, obj.type,
                    obj.promoType, obj.modified, obj.showMonday, obj.showTuesday, obj.showWednesday, obj.showThursday,
                    obj.showFriday, obj.showSaturday, obj.showSunday, obj.url, obj.text, imgStream, imgStreamBack);
            if (!c.moveToFirst()) {
                db.insertItem(obj.creatorID, obj.created, obj.name, obj.zone, obj.displayID, obj.length, obj.priority,
                        obj.dateStart, obj.dateEnd, obj.web, obj.display, obj.calendar, obj.bulletin, obj.kiosk, obj.type,
                        obj.promoType, obj.modified, obj.showMonday, obj.showTuesday, obj.showWednesday, obj.showThursday,
                        obj.showFriday, obj.showSaturday, obj.showSunday, obj.url, obj.text, imgStream, imgStreamBack);
            }
            db.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
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

    //  @Override
    //  protected void onProgressUpdate(Integer... values) {
    //      super.onProgressUpdate(values);
    //      context.updateProgressTo(values[0]);
    //  }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.updateUI(objectArrayList);
    }

    interface Communicator {
        //   void updateProgressTo(int progress);

        void updateUI(ArrayList<PromoObject> photosArrayList);
    }
}