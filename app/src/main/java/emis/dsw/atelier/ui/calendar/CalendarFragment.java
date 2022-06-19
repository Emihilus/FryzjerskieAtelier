package emis.dsw.atelier.ui.calendar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import emis.dsw.atelier.MainActivity;
import emis.dsw.atelier.R;
import emis.dsw.atelier.databinding.FragmentCalendarBinding;
import emis.dsw.atelier.ui.servicesList.ServicesListFragment;
import emis.dsw.atelier.utils.AtelierService;

public class CalendarFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        ListView listView = root.findViewById(R.id.slots_list);


        new AsyncTask<Void, Void, Void>() {
            JSONArray response;
            String date;

            @Override
            protected Void doInBackground(final Void... params) {
                date = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
                response = AtelierService.getEventsOfDay(date);

                return null;
            }

            @Override
            protected void onPostExecute(final Void result) {
                listView.setAdapter(new CalendarFragment.CalendarListViewAdapter(getContext(), response, date));
            }
        }.execute();


        CalendarView calendarView = root.findViewById(R.id.simpleCalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                new AsyncTask<Void, Void, Void>() {
                    JSONArray response;
                    String date;

                    @Override
                    protected Void doInBackground(final Void... params) {
                        date =i + "-" + (i1+1) + "-" + i2;
                        response = AtelierService.getEventsOfDay(date);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(final Void result) {
                        listView.setAdapter(new CalendarFragment.CalendarListViewAdapter(getContext(), response, date));
                    }
                }.execute();
            }
        });

        return root;
    }


    private class CalendarListViewAdapter extends BaseAdapter {

        private Context context;
        private slotEntry slotEntry[];
        private String date;

        public CalendarListViewAdapter(Context context, JSONArray source, String date) {
            this.context = context;
            this.date = date;

            slotEntry = new slotEntry[] {
                    new slotEntry("9:00"),
                    new slotEntry("10:00"),
                    new slotEntry("11:00"),
                    new slotEntry("12:00"),
                    new slotEntry("13:00"),
                    new slotEntry("14:00"),
                    new slotEntry("15:00"),
                    new slotEntry("16:00"),
                    new slotEntry("17:00")
            };

            for (int i = 0; i < source.length(); i++ ){

                try {
                    slotEntry[source.getJSONObject(i).getInt("slot")-1].status = 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public int getCount() {
            return slotEntry.length;
        }

        @Override
        public Object getItem(int i) {
                return slotEntry[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.fragment_calendar_list_item, parent, false);
            }
            String slotStatus = "";

            int colorResId = 0;
            if (slotEntry[position].status == 1){
                slotStatus = "Zajety";
                colorResId = R.color.red;
            } else {
                slotStatus = "Wolny";
                colorResId = R.color.green;
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AtelierService.day = date;
                        AtelierService.slot = position+1;
                        Toast.makeText(context,"Wybierz usługe na którą chcesz się umówić", Toast.LENGTH_LONG).show();
                        ((MainActivity) getActivity()).navController.navigate(R.id.nav_services_list);
                    }
                });
            }
            convertView.findViewById(R.id.calendar_item).setBackgroundColor(context.getResources().getColor(colorResId));

            ((TextView) convertView.findViewById(R.id.calendar_hour)).setText(slotEntry[position].cpt);
            ((TextView) convertView.findViewById(R.id.calendar_status)).setText(slotStatus);

            return convertView;
        }
    }

    private class slotEntry {
        public String cpt;
        public int status;

        slotEntry (String cpt) {
            this.cpt = cpt;
            this.status = 0;
        }
    }

}