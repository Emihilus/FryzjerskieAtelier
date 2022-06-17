package emis.dsw.atelier.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeEventsListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeEventsListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("To jest moja lista zdarzeń");
    }

    public LiveData<String> getText() {
        return mText;
    }
}