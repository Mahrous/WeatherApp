package com.graduation.mah.weather.ui.Disasters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.graduation.mah.weather.databinding.FragmentNotificationsBinding;
import com.graduation.mah.weather.ui.Earthquake.EarthquakeActivity;
import com.graduation.mah.weather.ui.Helpline;
import com.graduation.mah.weather.ui.MercalliSensorActivity;
import com.graduation.mah.weather.ui.ResourcesActivity;
import com.graduation.mah.weather.ui.egypthistory.EGDHistory;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.instruc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ResourcesActivity.class));
            }
        });
        binding.earth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EarthquakeActivity.class));
            }
        });
        binding.askHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Helpline.class));
            }
        });

        binding.earthSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MercalliSensorActivity.class));
            }
        }); binding.egHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EGDHistory.class));
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}