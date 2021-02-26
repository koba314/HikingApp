package com.example.hikingapp.ui.plans;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.hikingapp.R;

public class PlansFragment extends Fragment {

    public static final String PLANS_FRAGMENT = "com.hikingapp.PLANS_FRAGMENT";

    private PlansViewModel plansViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        plansViewModel =
                new ViewModelProvider(this).get(PlansViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plans, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        plansViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}