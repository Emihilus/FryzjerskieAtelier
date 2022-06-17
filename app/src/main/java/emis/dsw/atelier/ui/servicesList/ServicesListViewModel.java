package emis.dsw.atelier.ui.servicesList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ServicesListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ServicesListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}