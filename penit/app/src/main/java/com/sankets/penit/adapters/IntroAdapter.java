package com.sankets.penit.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sankets.penit.fragments.IntroFragment1;
import com.sankets.penit.fragments.IntroFragment2;
import com.sankets.penit.fragments.IntroFragment3;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class IntroAdapter extends FragmentPagerAdapter {

    public IntroAdapter(FragmentManager fm) {
        super(fm);

    }
    @Override
    public Fragment getItem(int position) {


        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position) {
            case 0 :
                fragment = new IntroFragment1();
                break;
            case 1:

                fragment = new IntroFragment2();
                // //.println("array " + arrayList);
                break;
            case 2:
                //fragment = new Frag2();
                fragment = new IntroFragment3();
                break;

        }
        assert fragment != null;
        return fragment;
    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}