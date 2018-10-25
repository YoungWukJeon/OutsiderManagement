package com.test.kani.outsidermanagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter
{
    //Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        //Returning the current tabs
        switch (position){
            case 0:
                MyInfoFragment myInfoFragment = new MyInfoFragment();
                return myInfoFragment;
            case 1:
                ReportFragment reportFragment = new ReportFragment();
                return reportFragment;
            case 2:
                CallVisitFragment callVisitFragment = new CallVisitFragment();
                return callVisitFragment;
            case 3:
                OutsiderManagementFragment outsiderManagementFragment = new OutsiderManagementFragment();
                return outsiderManagementFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
