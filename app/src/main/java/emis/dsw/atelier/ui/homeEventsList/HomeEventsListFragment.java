package emis.dsw.atelier.ui.homeEventsList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import emis.dsw.atelier.R;
import emis.dsw.atelier.utils.AtelierService;

public class HomeEventsListFragment extends Fragment {


    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_events_list, container, false);

        ListView listView = root.findViewById(R.id.events_list);

        new AsyncTask<Void, Void, Void>() {
            JSONArray response;

            @Override
            protected Void doInBackground(final Void... params) {
                response = AtelierService.getMyEvents();

                return null;
            }

            @Override
            protected void onPostExecute(final Void result) {
                listView.setAdapter(new HomeEventsListViewAdapter(getContext(), response));
            }
        }.execute();

        return root;
    }


    private class HomeEventsListViewAdapter extends BaseAdapter {

        private Context context;
        private JSONArray source;

        public HomeEventsListViewAdapter(Context context, JSONArray source) {
            this.context = context;
            this.source = source;
        }

        @Override
        public int getCount() {
            return source.length();
        }

        @Override
        public Object getItem(int i) {
            try {
                return source.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.fragment_events_list_item, parent, false);
            }

            String serviceName = "";

            try {
                serviceName = source.getJSONObject(position).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String serviceCost = "";

            try {
                serviceCost = source.getJSONObject(position).getString("cost");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String eventDatetime = "";

            try {
                eventDatetime = source.getJSONObject(position).getString("date_id") + " "
                + AtelierService.getHour(source.getJSONObject(position).getInt("slot"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int eventStatus = 0;
            try {
                eventStatus = source.getJSONObject(position).getInt("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int colorResId = 0;
            switch (eventStatus) {
                case 0:
                    colorResId = R.color.yellow;
                    break;

                case 1:
                    colorResId = R.color.green;
                    break;

                case -1:
                    colorResId = R.color.red;
                    break;
            }
            convertView.findViewById(R.id.my_events_service_item).setBackgroundColor(context.getResources().getColor(colorResId));

            ((TextView) convertView.findViewById(R.id.my_events_service_name)).setText(serviceName);
            ((TextView) convertView.findViewById(R.id.my_events_service_cost)).setText(serviceCost);
            ((TextView) convertView.findViewById(R.id.my_events_service_when)).setText(eventDatetime);
            return convertView;
        }
    }
}