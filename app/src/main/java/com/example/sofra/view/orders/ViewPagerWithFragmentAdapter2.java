package com.example.sofra.view.orders;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerWithFragmentAdapter2 extends FragmentPagerAdapter {


    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentsTitle = new ArrayList<>();
public static  int vis;
    public ViewPagerWithFragmentAdapter2(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments = new ArrayList<>();
        fragmentsTitle = new ArrayList<>();


    }

    public void addPager(Fragment fragments, String fragmentTitle , int vis) {
        this.fragments.add(fragments);
        this.fragmentsTitle.add(fragmentTitle);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return fragments.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsTitle.get(position);
    }
}
