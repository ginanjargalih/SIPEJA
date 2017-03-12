package com.example.android.sipeja;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.sipeja.dashboard.Pendapatan_Perlab;
import com.example.android.sipeja.dashboard.SPM;
import com.example.android.sipeja.dashboard.Status_Order;
import com.example.android.sipeja.dashboard.Status_Pembayaran;

/**
 * Created by ASUS X550D on 31/10/2016.
 */

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Status_Order SO = new Status_Order();
                return SO;
            case 1:
                SPM spm1 = new SPM();
                return spm1;
            case 2:
                Pendapatan_Perlab PPL = new Pendapatan_Perlab();
                return PPL;
            case 3:
                Status_Pembayaran SP = new Status_Pembayaran();
                return SP;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
