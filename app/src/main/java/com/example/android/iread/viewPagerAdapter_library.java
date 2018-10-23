package com.example.android.iread;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class viewPagerAdapter_library extends FragmentPagerAdapter {
    public viewPagerAdapter_library(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new myeBooksFragment();
        } else if (position == 1){
            return new currentReadsFragment();
        } else {
            return new recentlyAddedFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "My eBooks" ;
        } else  if(position == 1){
            return "Current Reads";
        }else {
            return "Recently Added";
        }
    }
}
