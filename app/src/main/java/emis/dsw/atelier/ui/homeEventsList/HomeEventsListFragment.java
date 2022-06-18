package emis.dsw.atelier.ui.homeEventsList;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import emis.dsw.atelier.R;
import emis.dsw.atelier.databinding.FragmentHomeEventsListBinding;
import emis.dsw.atelier.utils.AtelierService;

public class HomeEventsListFragment extends Fragment {


    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_home_events_list, container, false);

        TextView textView = root.findViewById(R.id.text_home);

        new AsyncTask<Void, Void, Void>() {
            JSONArray response;

            @Override
            protected Void doInBackground( final Void ... params ) {
                response = AtelierService.getMyEvents();

                return null;
            }

            @Override
            protected void onPostExecute( final Void result ) {
                try {
                   textView.setText(response.getJSONObject(0).getString("notice"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}