package com.ideaone.tabletapp1;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamesFragment extends Fragment {


    public GamesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_games, container, false);

        v.findViewById(R.id.app1).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app2).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app3).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app4).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app5).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app6).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app7).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app8).setOnClickListener(buttonClickListener);
        v.findViewById(R.id.app9).setOnClickListener(buttonClickListener);

        return v;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.app1:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "uk.co.aifactory.chess");
                    break;
                case R.id.app2:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "uk.co.aifactory.checkers");
                    break;
                case R.id.app3:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "uk.co.aifactory.backgammon");
                    break;
                case R.id.app4:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "com.brainium.spider");
                    break;
                case R.id.app5:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "uk.co.aifactory.rr");
                    break;
                case R.id.app6:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "uk.co.aifactory.hearts");
                    break;
                case R.id.app7:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "uk.co.aifactory.spades");
                    break;
                case R.id.app8:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "uk.co.aifactory.sudoku");
                    break;
                case R.id.app9:
                    // do something
                    new NewActivity().startNewActivity(v.getContext(), "uk.co.aifactory.ginrummy");
                    break;
            }
        }
    };
}
