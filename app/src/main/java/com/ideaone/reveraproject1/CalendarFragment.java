package com.ideaone.reveraproject1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class CalendarFragment extends Fragment {

    private View V;
    private CalendarView calendar;
    private TextView clock ;
    private long yourmilliseconds ;
    private Date resultdate ;
   // private SimpleDateFormat sdf_clock ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.calendar_fragment, container, false);
        calendar = (CalendarView) V.findViewById(R.id.calendar_view);
        initializeCalendar();

        return V;
    }

    public void initializeCalendar() {


        calendar.setShowWeekNumber(true);
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.white));
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.side_menu_color));
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.side_menu_color));
        calendar.setSelectedDateVerticalBar(R.color.white);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getActivity().getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            }
        });
    }


}
