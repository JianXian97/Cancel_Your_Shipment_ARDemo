package com.org.ardemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FilterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_fragment, container, false);
        View backgroundBlur = view.findViewById(R.id.filterFragmentLayoutBlur);
        backgroundBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(FilterFragment.this).commit();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
