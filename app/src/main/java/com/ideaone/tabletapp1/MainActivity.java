package com.ideaone.tabletapp1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity implements Thread.UncaughtExceptionHandler {
    public static final int REQUEST_CAMERA = 1;
    public static final int PERMISSION_REQUEST_CODE = 2;
    private Drawable storeBackground;
    private int actionId = 100;
    private boolean isLogoSelected = false;
    FragmentManager fm;
    // set 1
    HomeFragment homeFragment;
    WeatherFragment2 weatherFragment;
    NewsFragment newsFragment;
    MenuFragment menuFragment;
    PromoFragment promoFragment;
    RecreationFragment recreationFragment;
    MessagesFragment messages;
    //GalleryFragment galleryFragment;
    GallPhotoFragment galleryFragment;

    // set 2
    ReadFragment readFragment;
    MusicFragment musicFragment;
    VideoFragment videoFragment;
    CalendarFragment calendarFragment;
    SocialFragment socialFragment;
    GamesFragment gamesFragment;
    HelpFragment helpFragment;
    InfoFragment infoFragment;

    // first set
    ImageView logoView;
    ImageView weatherBtn;
    ImageView newsBtn;
    ImageView menuBtn;
    ImageView recreationBtn;
    ImageView promoBtn;
    ImageView bulletinBtn;
    ImageView galleryBtn;
    ImageView cameraBtn;
    // second set
    ImageView readView;
    ImageView musicView;
    ImageView videoView;
    ImageView calendarBtn;
    ImageView socialView;
    ImageView gamesView;
    ImageView helpView;
    ImageView infoView;

    int tempMinute = 0;
    int tempHour = -1;

    final int timeoutValue = 60; // 30 * 1000 = 30000 = 30 seconds
    boolean finishFlag = false;

    Boolean showPhase2, cameraS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        hideNavBar();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        showPhase2 = prefs.getBoolean("phaseS", false);
        cameraS = prefs.getBoolean("cameraS", false);

        logoView = (ImageView) findViewById(R.id.logo);
        weatherBtn = (ImageView) findViewById(R.id.weather);
        newsBtn = (ImageView) findViewById(R.id.news);
        menuBtn = (ImageView) findViewById(R.id.menu);
        recreationBtn = (ImageView) findViewById(R.id.recreation);
        promoBtn = (ImageView) findViewById(R.id.promo);
        bulletinBtn = (ImageView) findViewById(R.id.bulletin);
        galleryBtn = (ImageView) findViewById(R.id.gallery);
        cameraBtn = (ImageView) findViewById(R.id.camera);

        readView = (ImageView) findViewById(R.id.read);
        musicView = (ImageView) findViewById(R.id.music);
        videoView = (ImageView) findViewById(R.id.movies);
        calendarBtn = (ImageView) findViewById(R.id.calendar);
        socialView = (ImageView) findViewById(R.id.chat);
        gamesView = (ImageView) findViewById(R.id.games);
        helpView = (ImageView) findViewById(R.id.nurse);
        infoView = (ImageView) findViewById(R.id.info);

        if (cameraS) {
            cameraBtn.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.camera));
        } else {
            cameraBtn.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.exit));
        }

        fm = getFragmentManager();
        // set 1
        homeFragment = new HomeFragment();
        weatherFragment = new WeatherFragment2();
        newsFragment = new NewsFragment();
        menuFragment = new MenuFragment();
        promoFragment = new PromoFragment();
        recreationFragment = new RecreationFragment();
        //galleryFragment = new GalleryFragment();
        galleryFragment = new GallPhotoFragment();
        messages = new MessagesFragment();

        // set 2
        readFragment = new ReadFragment();
        musicFragment = new MusicFragment();
        videoFragment = new VideoFragment();
        calendarFragment = new CalendarFragment();
        socialFragment = new SocialFragment();
        gamesFragment = new GamesFragment();
        helpFragment = new HelpFragment();
        infoFragment = new InfoFragment();


        if (isNetworkAvailable()) {
            fragmentReplace(fm, homeFragment);
        } else {
            fragmentReplace(fm, homeFragment);
            Toast.makeText(getApplicationContext(), "check your internet connection", Toast.LENGTH_LONG).show();
        }

        // Check permission for CAMERA
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
            // else {
            //// permission has been granted, continue as usual

            //  Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //  startActivityForResult(captureIntent, 0);
            // }
        }

        setAppIdleTimeout();

        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.logo_green));

                //   if (!isLogoSelected) {
                //       logoView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.logo_green));
                //      findViewById(R.id.mainActionSet).setVisibility(View.GONE);
                //      findViewById(R.id.secondActionSet).setVisibility(View.VISIBLE);
                //   } else {
                //       logoView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.logo_grey));
                //      findViewById(R.id.mainActionSet).setVisibility(View.VISIBLE);
                //      findViewById(R.id.secondActionSet).setVisibility(View.GONE);
                //    }
                //   isLogoSelected = !isLogoSelected;
                fragmentReplace(fm, homeFragment);
            }
        });

        if (showPhase2) {
            logoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!isLogoSelected) {
                        logoView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.logo_green));
                        findViewById(R.id.mainActionSet).setVisibility(View.GONE);
                        findViewById(R.id.secondActionSet).setVisibility(View.VISIBLE);
                    } else {
                        logoView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.logo_grey));
                        findViewById(R.id.mainActionSet).setVisibility(View.VISIBLE);
                        findViewById(R.id.secondActionSet).setVisibility(View.GONE);
                    }
                    hideNavBar();
                    isLogoSelected = !isLogoSelected;
                    return false;
                }
            });
        }

        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.weather_select));
                fragmentReplace(fm, weatherFragment);
            }
        });


        newsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.news_select));
                fragmentReplace(fm, newsFragment);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.menu_select));
                fragmentReplace(fm, menuFragment);
            }
        });

        recreationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.recreation_select));
                fragmentReplace(fm, recreationFragment);
            }
        });
        promoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.promo_select));
                fragmentReplace(fm, promoFragment);
            }
        });

        bulletinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.bulletin_select));
                fragmentReplace(fm, messages);
            }
        });
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.gallery_select));
                fragmentReplace(fm, galleryFragment);
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraS) {
                    //******** with Camera ********//
                    storeAndRestoreState(v);
                    v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.camera_select));
                    fragmentReplace(fm, new CameraFragment());
                } else {
                    //******** no Camera ********//
                    finishFlag = true;
                    MainActivity.this.finish();
                }
            }
        });

        /***set 2*/

        readView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.read_select));
                fragmentReplace(fm, readFragment);
            }
        });
        musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.music_select));
                fragmentReplace(fm, musicFragment);
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.movies_select));
                fragmentReplace(fm, videoFragment);
            }
        });
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.calendar_select));
                fragmentReplace(fm, calendarFragment);
            }
        });
        socialView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.chat_select));
                fragmentReplace(fm, socialFragment);
            }
        });
        gamesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.games_select));
                fragmentReplace(fm, gamesFragment);
            }
        });
        helpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.nurse_select));
                fragmentReplace(fm, helpFragment);
            }
        });
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeAndRestoreState(v);
                v.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.info_select));
                fragmentReplace(fm, infoFragment);
            }
        });

    }

    //Timer for checking if idle
    private Handler handler;
    private Runnable runnable;
    private Runnable runnableFinishApp;

    //call in onCreate
    private void setAppIdleTimeout() {
        handler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Calendar cal = Calendar.getInstance();
                        //   int minute = cal.get(Calendar.MINUTE);
                        int hour = cal.get(Calendar.HOUR);

                        // if (hour != tempHour) {
                        //  tempMinute = minute;
                        //     tempHour = hour;
                        //   galleryBtn.performClick();
                        recreationBtn.performClick();
                        bulletinBtn.performClick();
                        promoBtn.performClick();
                        weatherBtn.performClick();
                        // }

                        menuBtn.performClick();
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, timeoutValue * 2 * 1000);
                    }
                });
            }
        };

        runnableFinishApp = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onDestroy();
                        handler.removeCallbacks(runnableFinishApp);
                        handler.postDelayed(runnableFinishApp, 1000 * 60 * 60 * 8); //8 hours
                    }
                });
            }
        };

        handler.postDelayed(runnable, timeoutValue * 2 * 1000); // 30 seconds
        handler.postDelayed(runnableFinishApp, 1000 * 60 * 60 * 8); //8 hours
    }

    public void resetAppIdleTimeout() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, timeoutValue * 2 * 1000);
    }

    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        resetAppIdleTimeout();
    }

    private boolean isNetworkAvailable() {
        getApplicationContext();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void storeAndRestoreState(View view) {
        if (actionId != 100) {
            findViewById(actionId).setBackground(storeBackground);
        }
        hideNavBar();
        storeBackground = view.getBackground();
        actionId = view.getId();
    }

    private void fragmentReplace(FragmentManager FM, Fragment fragment) {
        try {
            FragmentTransaction transaction = FM.beginTransaction();
            if (fragment.getClass() != WeatherFragment2.class) {
                Fragment temp;
                try {
                    temp = fragment.getClass().newInstance();
                    transaction.remove(fragment);
                    transaction.replace(R.id.fragment_holder, temp);
                    transaction.commit();
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    transaction.replace(R.id.fragment_holder, fragment);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FM.executePendingTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this,
                            "Permission accepted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void hideNavBar() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        View decorView = getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavBar();
        logoView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.logo_grey));
        resetAppIdleTimeout();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finishFlag = true;
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!finishFlag) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            MainActivity.this.startActivity(intent);
            //for restarting the Activity
            System.exit(2);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        MainActivity.this.startActivity(intent);
        //for restarting the Activity
        System.exit(2);
    }
}