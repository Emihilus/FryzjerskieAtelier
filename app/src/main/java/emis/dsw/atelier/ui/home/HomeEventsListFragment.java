package emis.dsw.atelier.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import emis.dsw.atelier.databinding.FragmentHomeEventsListBinding;

public class HomeEventsListFragment extends Fragment {

    private FragmentHomeEventsListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeEventsListViewModel homeEventsListViewModel =
                new ViewModelProvider(this).get(HomeEventsListViewModel.class);

        binding = FragmentHomeEventsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeEventsListViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}