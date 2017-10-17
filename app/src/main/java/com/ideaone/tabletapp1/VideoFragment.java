package com.ideaone.tabletapp1;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {


    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video, container, false);

        v.findViewById(R.id.app1).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app2).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app3).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app4).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app5).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app6).setOnClickListener(buttonClickListener);

        return v;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app1:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "org.xbmc.kodi");
                    break;
                case R.id.app2:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.google.android.youtube");
                    break;
                case R.id.app3:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.gotv.crackle.handset");
                    break;
                case R.id.app4:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.netflix.mediaclient");
                    break;
                case R.id.app5:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.tubitv");
                    break;
                case R.id.app6:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "ca.bellmedia.cravetv");
                    break;
            }
        }
    };

}
