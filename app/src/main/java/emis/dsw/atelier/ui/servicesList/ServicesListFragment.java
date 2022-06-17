package emis.dsw.atelier.ui.servicesList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import emis.dsw.atelier.databinding.FragmentServicesListBinding;

public class ServicesListFragment extends Fragment {

    private FragmentServicesListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ServicesListViewModel servicesListViewModel =
                new ViewModelProvider(this).get(ServicesListViewModel.class);

        binding = FragmentServicesListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        servicesListViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}