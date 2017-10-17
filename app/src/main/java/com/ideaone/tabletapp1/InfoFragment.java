package com.ideaone.tabletapp1;


import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {


    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);

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
                    Toast infoMsg1 = Toast.makeText(v.getContext(),"Call Front Desk",Toast.LENGTH_LONG);
                    infoMsg1.setGravity(Gravity.CENTER,0,0);
                    infoMsg1.show();
                    break;
                case R.id.app2:
                    // do something
                    Toast infoMsg2 = Toast.makeText(v.getContext(),"About The Company",Toast.LENGTH_LONG);
                    infoMsg2.setGravity(Gravity.CENTER,0,0);
                    infoMsg2.show();
                    break;
                case R.id.app3:
                    // do something
                    Toast infoMsg3 = Toast.makeText(v.getContext(),"About This Location",Toast.LENGTH_LONG);
                    infoMsg3.setGravity(Gravity.CENTER,0,0);
                    infoMsg3.show();
                    break;
            }
        }
    };

}
