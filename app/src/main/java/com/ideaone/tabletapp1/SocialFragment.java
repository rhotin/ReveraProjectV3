package com.ideaone.tabletapp1;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialFragment extends Fragment {


    public SocialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_social, container, false);

        v.findViewById(R.id.app1).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app2).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app3).setOnClickListener(buttonClickListener);

        return v;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app1:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.skype.raider");
                    break;
                case R.id.app2:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.facebook.katana");
                    break;
                case R.id.app3:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.facebook.orca");
                    break;
            }
        }
    };

}
