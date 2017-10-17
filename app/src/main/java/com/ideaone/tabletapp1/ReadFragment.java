package com.ideaone.tabletapp1;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadFragment extends Fragment {

    public ReadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_read, container, false);

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
                    new NewActivity().startNewActivity(v.getContext(), "com.amazon.kindle");
                    break;
                case R.id.app2:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.google.android.apps.books");
                    break;
                case R.id.app3:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.google.android.apps.genie.geniewidget");
                    break;
            }
        }
    };
  //  @Override
  //  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
  //      super.onViewCreated(view, savedInstanceState);
  //      new NewActivity().startNewActivity(view.getContext(), "com.google.android.youtube");
  //  }
}
