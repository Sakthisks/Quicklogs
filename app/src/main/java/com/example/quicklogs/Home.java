//this has the default home page with multiple fragments
package com.example.quicklogs;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        //adding the fragments
        viewPagerAdapter.addFragment(new HomeDefault(), "Amount");
        viewPagerAdapter.addFragment(new HomeFrag(), "Category");
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}
