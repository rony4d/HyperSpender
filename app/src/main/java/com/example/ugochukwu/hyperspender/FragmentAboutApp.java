package com.example.ugochukwu.hyperspender;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentAboutApp extends Fragment {

    public FragmentAboutApp() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about_app, container, false);
        TextView aboutApptextView = (TextView) rootView.findViewById(R.id.txt_about_app);
        aboutApptextView.setText(R.string.app_description);
        return rootView;
    }



}
