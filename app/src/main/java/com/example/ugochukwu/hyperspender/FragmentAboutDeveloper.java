package com.example.ugochukwu.hyperspender;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentAboutDeveloper extends Fragment {


    public FragmentAboutDeveloper() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about_developer, container, false);
        TextView aboutDevTextView = (TextView) rootView.findViewById(R.id.txt_about_developer);
        aboutDevTextView.setText(R.string.developer_description);
        return rootView;
    }



}
