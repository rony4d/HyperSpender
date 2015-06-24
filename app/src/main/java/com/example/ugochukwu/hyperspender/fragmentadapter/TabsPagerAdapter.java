package com.example.ugochukwu.hyperspender.fragmentadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ugochukwu.hyperspender.FragmentAboutDeveloper;
import com.example.ugochukwu.hyperspender.FragmentAboutApp;

/**
 * Created by ugochukwu on 6/20/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new FragmentAboutDeveloper();
            case 1:
                // Games fragment activity
                return new FragmentAboutApp();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }
}
