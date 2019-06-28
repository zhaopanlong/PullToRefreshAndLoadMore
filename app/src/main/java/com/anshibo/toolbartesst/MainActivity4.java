package com.anshibo.toolbartesst;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

/**
 * @author zhaopanlong
 * @createtime：2019/6/26 15:41
 */
public class MainActivity4 extends AppCompatActivity {

    PageNavigationView navigationView;
    int i = 0;
    List<Fragment> fragments = new ArrayList<>();
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlexStatusBarUtils.setTransparentStatusBar(this);
        setContentView(R.layout.activity_main4);
        navigationView = findViewById(R.id.pageNavigation);
        viewPager = findViewById(R.id.viewPager);
        NavigationController controller = navigationView.material().addItem(android.R.drawable.ic_menu_camera, "相机")
                .addItem(android.R.drawable.ic_menu_compass, "位置")
                .addItem(android.R.drawable.ic_menu_search, "搜索")
                .addItem(android.R.drawable.ic_menu_help, "帮助").build();
        fragments.add(createFragment());
        fragments.add(createFragment());
        fragments.add(createFragment());
        fragments.add(createFragment());
//        controller.addTabItemSelectedListener(new OnTabItemSelectedListener() {
//            @Override
//            public void onSelected(int index, int old) {
//                showFragment(index);
//            }
//
//            @Override
//            public void onRepeat(int index) {
//
//            }
//        });
//        showFragment(0);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        controller.setupWithViewPager(viewPager);
    }

//    private void showFragment(int i) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragments.get(i)).commit();
//    }

    private Fragment createFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", i);
        i++;
        MainActivity3 fragment = new MainActivity3();
        fragment.setArguments(bundle);
        return fragment;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
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
    }
}
