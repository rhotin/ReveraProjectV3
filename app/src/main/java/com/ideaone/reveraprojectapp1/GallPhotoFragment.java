package com.ideaone.reveraprojectapp1;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GallPhotoFragment extends Fragment implements GallDownloadAlbums.CommunicatorA, GallDownloadPhotos.Communicator, GallDownloadPhotoFull.CommunicatorF {
    String locationSelected;

    static String AlURL;

    ListView albumListView;
    ProgressBar progressBarA;
    TextView messageTextA;

    //public String AlURL = "http://revera.mxs-s.com/displays/" + locationSelected + "/albums.json?album=";
    public static String newRURL = "";

    ListView photoListView;
    ProgressBar progressBar;
    TextView messageText;

    ImageView photoView;

    static String photoCreatorID = "";
    static String photoCreated = "";
    static String photoClass = "";
    static String photoType = "";
    static String photoExt = "";
    static String photoName = "";
    static String photoID = "";
    static String photoThumb = "";
    static String photoFull = "";
    GallDownloadAlbums downloadAlbum;
    GallDownloadPhotoFull downloadPhotosFull;
    GallDownloadPhotos downloadPhotos;

    DBAdapterAlbums dbAlb;
    ArrayList<GallAlbumObject> albumsArrayListMem = new ArrayList<>();

    DBAdapterPhotos dbPhoto;
    ArrayList<GallPhotoObject> photosArrayListMem = new ArrayList<>();

    public GallPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.gallery_fragment, container, false);

        //AlbumObject obj = getActivity().getIntent().getParcelableExtra("theObject");
        //newRURL = RURL + obj.albumID;

        final SharedPreferences prefs = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        locationSelected = prefs.getString("location", getString(R.string.ReveraLocation));

        if (locationSelected.equals("leaside-14")) {
            locationSelected = "leaside";
        }

        dbAlb = new DBAdapterAlbums(getActivity().getBaseContext());
        dbPhoto = new DBAdapterPhotos(getActivity().getBaseContext());

        AlURL = "http://revera.mxs-s.com/displays/" + locationSelected + "/albums.json?album=";

        albumListView = (ListView) V.findViewById(R.id.listViewA);
        progressBarA = (ProgressBar) V.findViewById(R.id.progressBarA);
        messageTextA = (TextView) V.findViewById(R.id.textViewA);

        photoListView = (ListView) V.findViewById(R.id.listView);
        progressBar = (ProgressBar) V.findViewById(R.id.progressBar);
        messageText = (TextView) V.findViewById(R.id.textView);

        photoView = (ImageView) V.findViewById(R.id.userImage);

        photoListView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        messageText.setVisibility(View.VISIBLE);

        albumListView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        messageText.setVisibility(View.VISIBLE);

        return V;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            asyncTaskRelease();
            if (isNetworkAvailable()) {
                getAllAlbums();
                dbAlb.open();
                dbAlb.resetDB();
                dbAlb.close();
                downloadAlbum = new GallDownloadAlbums(this);
                downloadAlbum.execute();
            } else {
                getAllAlbums();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProgressTo(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void updateAUI(final ArrayList<GallAlbumObject> albumsArrayList) {
        try {
           /* dbAlb.open();
            dbAlb.resetDB();
            dbAlb.close();
            for (GallAlbumObject albumObj : albumsArrayList) {
                Log.e("helllooo ALBUM", "" + albumObj.albumName);
                saveToAlbums(albumObj);
            }
            */
            populateAlbumList(albumsArrayList);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void getAllAlbums() {
        albumsArrayListMem.clear();
        String creatorID;
        String created;
        String title;
        String displayID;
        String web;
        String display;
        String modified;
        String handle;
        String ID;

        dbAlb.open();
        Cursor c = dbAlb.getAllItems();
        Log.e("objtt5 ALBUM", "" + c);
        if (c.moveToLast()) {
            do {
                creatorID = c.getString(1);
                created = c.getString(2);
                title = c.getString(3);
                displayID = c.getString(4);
                web = c.getString(5);
                display = c.getString(6);
                modified = c.getString(7);
                handle = c.getString(8);
                ID = c.getString(9);

                GallAlbumObject obj = new GallAlbumObject(creatorID, created, title, displayID, web,
                        display, modified, handle, ID);
                albumsArrayListMem.add(obj);
                Log.e("obt1 ALBUM", "" + obj.albumName);
            } while (c.moveToPrevious());
            GallAlbumObject obj = albumsArrayListMem.get(0);
            Log.e("obt1 allbb", "" + albumsArrayListMem.size() + "---" + obj.albumName);
            //updateAUI(albumsArrayListMem);
            populateAlbumList(albumsArrayListMem);
        }
        dbAlb.close();
    }

    public void populateAlbumList(final ArrayList<GallAlbumObject> albumsArrayList) {
        if (albumsArrayList.size() > 0) {
            albumListView.setVisibility(View.VISIBLE);
            progressBarA.setVisibility(View.GONE);
            messageTextA.setVisibility(View.GONE);

            GallAlbumObject obj = albumsArrayList.get(0);
            newRURL = AlURL + obj.albumID;
            Log.e("RURL", "" + newRURL);
            Log.e("RURL2", "" + obj.albumName);

            GallAlbumAdapter adapterA = new GallAlbumAdapter(getActivity().getApplicationContext(), albumsArrayList);
            albumListView.setAdapter(adapterA);

            if (isNetworkAvailable()) {
                asyncTaskRelease();
                getAllPhotos();
                dbPhoto.open();
                dbPhoto.resetDB();
                dbPhoto.close();
                downloadPhotos = new GallDownloadPhotos(this);
                downloadPhotos.execute();
            } else {
                getAllPhotos();
            }

            albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    GallAlbumObject obj = albumsArrayList.get(position);

                    newRURL = AlURL + obj.albumID;
                    asyncTaskRelease();
                    if (isNetworkAvailable()) {
                        // getAllPhotos();
                        downloadPhotos = new GallDownloadPhotos(GallPhotoFragment.this);
                        downloadPhotos.execute();
                    } else {
                        getAllPhotos();
                    }
                /*Intent intent = new Intent(PhotoFragment.this, PhotoActivity.class);
                intent.putExtra("theObject", objectToPass);
                startActivity(intent);
                */
                }
            });
        }
    }

    @Override
    public void updateFUI(ArrayList<GallPhotoObject> photosArrayList) {
        photoView.setImageBitmap(photosArrayList.get(0).fullImg);
    }

    @Override
    public void updateUI(final ArrayList<GallPhotoObject> photosArrayList) {
        try {
            // Log.e("heyooo Photo", "" + albumsArrayListMem.get(0).albumID + "+++" + photosArrayList.get(0).creatorID);
            /*
            if (Objects.equals(albumsArrayListMem.get(0).albumID, photosArrayList.get(0).creatorID)) {
                dbPhoto.open();
                dbPhoto.resetDB();
                dbPhoto.close();
                for (GallPhotoObject photoObj : photosArrayList) {
                    Log.e("helllooo Photot", "" + photoObj.photoName);
                    saveToPhotos(photoObj);
                }
            }
            */
            populatePhotoList(photosArrayList);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void getAllPhotos() {
        photosArrayListMem.clear();
        String creatorID;
        String created;
        String mClass;
        String type;
        String ext;
        String picName;
        String id;
        byte[] image250;
        byte[] imageFull;
        Bitmap thumbnail = null;
        Bitmap fullImage = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inSampleSize = calculateInSampleSize(options, 300, 300);
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        dbPhoto.open();
        Cursor c = dbPhoto.getAllItems();
        Log.e("objtt5 ALBUM", "" + c);
        if (c.moveToLast()) {
            do {
                creatorID = c.getString(1);
                created = c.getString(2);
                mClass = c.getString(3);
                type = c.getString(4);
                ext = c.getString(5);
                picName = c.getString(6);
                id = c.getString(7);
                image250 = c.getBlob(8);
                //  imageFull = c.getBlob(9);

                if (image250 != null) {
                    try {
                        thumbnail = BitmapFactory.decodeByteArray(image250, 0, image250.length, options);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                }
            /*    if (imageFull != null) {
                    try {
                        fullImage = BitmapFactory.decodeByteArray(imageFull, 0, imageFull.length, options);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                }
                */

                GallPhotoObject obj = new GallPhotoObject(creatorID, created, mClass, type, ext,
                        picName, id, thumbnail, null);
                photosArrayListMem.add(obj);
                Log.e("obt1 photo", "" + obj.photoName);
            } while (c.moveToPrevious());
            GallPhotoObject obj = photosArrayListMem.get(0);
            Log.e("obt1 photobb", "" + photosArrayListMem.size() + "---" + obj.photoName);
        }
        dbPhoto.close();

        //updateAUI(albumsArrayListMem);
        populatePhotoList(photosArrayListMem);
    }

    public void populatePhotoList(final ArrayList<GallPhotoObject> photosArrayList) {
        if (photosArrayList.size() > 0) {
            photoListView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            messageText.setVisibility(View.GONE);
            if (!photosArrayList.isEmpty()) {
                try {
                    photoName = photosArrayList.get(0).photoName;
                    photoExt = photosArrayList.get(0).photoExt;

                    GallPhotoAdapter adapter = new GallPhotoAdapter(getActivity().getApplicationContext(), photosArrayList);
                    photoListView.setAdapter(adapter);

                    photoView.setImageBitmap(photosArrayList.get(0).thumbnail);
                    asyncTaskRelease();
                    if (isNetworkAvailable()) {
                        downloadPhotosFull = new GallDownloadPhotoFull(this);
                        downloadPhotosFull.execute();
                    }

                    photoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            GallPhotoObject objectToPass = photosArrayList.get(position);

                            photoCreatorID = objectToPass.creatorID;
                            photoCreated = objectToPass.created;
                            photoClass = objectToPass.mClass;
                            photoType = objectToPass.type;
                            photoExt = objectToPass.photoExt;
                            photoName = objectToPass.photoName;
                            photoID = objectToPass.ID;
                            photoView.setImageBitmap(objectToPass.thumbnail);
                            asyncTaskRelease();
                            if (isNetworkAvailable()) {
                                downloadPhotosFull = new GallDownloadPhotoFull(GallPhotoFragment.this);
                                downloadPhotosFull.execute();
                            }

                /*Intent intent = new Intent(PhotoActivity.this, DetailActivity.class);
                intent.putExtra("thePObject", objectToPass);
                startActivity(intent);
                */
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

    public boolean isNetworkAvailable() {
        getActivity().getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
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

    public void asyncTaskRelease() {
        if (downloadAlbum != null)
            downloadAlbum.cancel(true);
        if (downloadPhotosFull != null)
            downloadPhotosFull.cancel(true);
        if (downloadPhotos != null)
            downloadPhotos.cancel(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        asyncTaskRelease();
    }
}
