package com.anshibo.toolbartesst;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaopanlong
 * @createtime：2019/6/26 15:41
 */
public class MainActivity3 extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    private List<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int type = getArguments().getInt("type");
        View view = inflater.inflate(R.layout.activity_main3,container,false);
        toolbar = view.findViewById(R.id.toolBar);
        TextView title = view.findViewById(R.id.title);
        title.setText("第"+type+"个");
        if (type==3){
            toolbar.setVisibility(View.GONE);
        }
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        initFragments();
        viewPager.setAdapter(new FragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initFragments() {
        TabFragment tab1 = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TAB", 1);
        tab1.setArguments(bundle);

        TabFragment tab2 = new TabFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("TAB", 2);
        tab2.setArguments(bundle2);

        TabFragment tab3 = new TabFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("TAB", 3);
        tab3.setArguments(bundle3);


        TabFragment tab4 = new TabFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putInt("TAB", 4);
        tab4.setArguments(bundle4);

        TabFragment tab5 = new TabFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putInt("TAB", 5);
        tab5.setArguments(bundle5);
        fragments.add(tab1);
        fragments.add(tab2);
        fragments.add(tab3);
        fragments.add(tab4);
        fragments.add(tab5);
        fragments.add(creatFragment());
        fragments.add(creatFragment());
        fragments.add(creatFragment());
        fragments.add(creatFragment());
        fragments.add(creatFragment());
        fragments.add(creatFragment());


    }

    private TabFragment creatFragment() {
        TabFragment tab5 = new TabFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putInt("TAB", 5);
        tab5.setArguments(bundle5);
        return tab5;
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "第" + position + "个";
        }
    }
}
