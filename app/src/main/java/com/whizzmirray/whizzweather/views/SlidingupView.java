package com.whizzmirray.whizzweather.views;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whizzmirray.whizzweather.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SlidingupView extends Fragment {

    public SlidingupView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.slidingup_view, container, false);
    }

}
