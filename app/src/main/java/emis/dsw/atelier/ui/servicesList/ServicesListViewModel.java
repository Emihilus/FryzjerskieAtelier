package emis.dsw.atelier.ui.servicesList;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import emis.dsw.atelier.utils.AtelierService;

public class ServicesListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ServicesListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");

        new AsyncTask<Void, Void, Void>() {
            JSONArray response;

            @Override
            protected Void doInBackground( final Void ... params ) {
                response = AtelierService.getServices();

                return null;
            }

            @Override
            protected void onPostExecute( final Void result ) {
                try {
                    mText.setValue(response.getJSONObject(0).getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    public LiveData<String> getText() {
        return mText;
    }
}