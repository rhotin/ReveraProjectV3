package com.ideaone.reveraprojectapp1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingsActivity extends Activity implements SettingsDownloadCompany.Communicator, SettingsDownloadLocation.Communicator {

    Button doneBtn;
    Switch cameraS;
    Switch phaseS;

    ListView compListView;
    ProgressBar compProgressBar;
    TextView compMessageText;
    TextView compSelectedText;
    ListView locListView;
    ProgressBar locProgressBar;
    TextView locMessageText;
    TextView locSelectedText;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public static String theLocationUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        doneBtn = (Button) findViewById(R.id.doneBtn);
        cameraS = (Switch) findViewById(R.id.cameraSwitch);
        phaseS = (Switch) findViewById(R.id.phase2Switch);
        compListView = (ListView) findViewById(R.id.listViewCompany);
        compProgressBar = (ProgressBar) findViewById(R.id.progressBarCompany);
        compMessageText = (TextView) findViewById(R.id.textViewCompany);
        compSelectedText = (TextView) findViewById(R.id.companyTVSelected);
        locListView = (ListView) findViewById(R.id.listViewLocation);
        locProgressBar = (ProgressBar) findViewById(R.id.progressBarLocation);
        locMessageText = (TextView) findViewById(R.id.textViewLocation);
        locSelectedText = (TextView) findViewById(R.id.locationTVSelected);

        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = prefs.edit();

        cameraS.setChecked(prefs.getBoolean("cameraS", false));
        phaseS.setChecked(prefs.getBoolean("phaseS", false));
        compSelectedText.setText(prefs.getString("company", ""));
        locSelectedText.setText(prefs.getString("location", ""));

        cameraS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("cameraS", true);
                } else {
                    editor.putBoolean("cameraS", false);
                }
                editor.commit();
            }
        });

        phaseS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("phaseS", true);
                } else {
                    editor.putBoolean("phaseS", false);
                }
                editor.commit();
            }
        });

        SettingsDownloadCompany downloadComps = new SettingsDownloadCompany(this);
        downloadComps.execute();

        compListView.setVisibility(View.GONE);
        compProgressBar.setVisibility(View.VISIBLE);
        compMessageText.setVisibility(View.VISIBLE);
        locListView.setVisibility(View.GONE);
        locProgressBar.setVisibility(View.VISIBLE);
        locMessageText.setVisibility(View.VISIBLE);

    }

    public void DoneClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateProgressTo(int progress) {
        compProgressBar.setProgress(progress);
    }

    @Override
    public void updateUI(final ArrayList<SettingsCompanyObject> compsArrayList) {
        compListView.setVisibility(View.VISIBLE);
        compProgressBar.setVisibility(View.GONE);
        compMessageText.setVisibility(View.GONE);

        SettingsCompanyAdapter adapter = new SettingsCompanyAdapter(this, compsArrayList);
        compListView.setAdapter(adapter);


        compListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SettingsCompanyObject objectToPass = compsArrayList.get(position);
                editor.putString("company", objectToPass.text);
                editor.commit();
                compSelectedText.setText(objectToPass.text);
                theLocationUrl = "http://" + objectToPass.text + "/displays.json";

                SettingsDownloadLocation downloadLoc = new SettingsDownloadLocation(SettingsActivity.this);
                downloadLoc.execute();

                //  Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                //  intent.putExtra("theLibObject", objectToPass);
                //  startActivity(intent);

                locListView.setVisibility(View.GONE);
                locProgressBar.setVisibility(View.VISIBLE);
                locMessageText.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void updateProgressToLoc(int progress) {
        locProgressBar.setProgress(progress);
    }

    @Override
    public void updateUILoc(final ArrayList<SettingsLocationObject> locArrayList) {
        locListView.setVisibility(View.VISIBLE);
        locProgressBar.setVisibility(View.GONE);
        locMessageText.setVisibility(View.GONE);

        SettingsLocationAdapter adapterLoc = new SettingsLocationAdapter(this, locArrayList);
        locListView.setAdapter(adapterLoc);

        locListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingsLocationObject locToPass = locArrayList.get(position);
                editor.putString("location", locToPass.handle);
                editor.commit();
                locSelectedText.setText(locToPass.handle);
            }
        });
    }
}
