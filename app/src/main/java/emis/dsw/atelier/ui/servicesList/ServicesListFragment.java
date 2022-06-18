package emis.dsw.atelier.ui.servicesList;

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
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;

import emis.dsw.atelier.R;
import emis.dsw.atelier.databinding.FragmentServicesListBinding;
import emis.dsw.atelier.ui.homeEventsList.HomeEventsListFragment;
import emis.dsw.atelier.utils.AtelierService;

public class ServicesListFragment extends Fragment {

    private FragmentServicesListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_services_list, container, false);

        ListView listView = root.findViewById(R.id.services_list);

        new AsyncTask<Void, Void, Void>() {
            JSONArray response;

            @Override
            protected Void doInBackground(final Void... params) {
                response = AtelierService.getServices();

                return null;
            }

            @Override
            protected void onPostExecute(final Void result) {
                listView.setAdapter(new ServicesListFragment.ServicesListViewAdapter(getContext(), response));
            }
        }.execute();

        return root;
    }


    private class ServicesListViewAdapter extends BaseAdapter {

        private Context context;
        private JSONArray source;

        public ServicesListViewAdapter(Context context, JSONArray source) {
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
                        inflate(R.layout.fragment_services_list_item, parent, false);
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

            ((TextView) convertView.findViewById(R.id.services_name)).setText(serviceName);
            ((TextView) convertView.findViewById(R.id.services_cost)).setText(serviceCost);
            return convertView;
        }
    }

}