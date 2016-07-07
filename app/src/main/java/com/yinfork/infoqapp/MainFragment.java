package com.yinfork.infoqapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;

import com.yinfork.infoqapp.news.InfoqNewsFragment;
import com.yinfork.infoqapp.news.InfoqVideoFragment;
import com.yinfork.infoqapp.util.ToolbarUtil;

/**
 * Created by yinjianhua on 16/5/3.
 */
public class MainFragment extends Fragment {

    TabLayout mTabLayout;
    private ViewPager mViewPager;


    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_main, container, false);

        mViewPager = (ViewPager) root.findViewById(R.id.main_viewpager);

        mViewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager()));

        mTabLayout = ToolbarUtil.getTab(getActivity());

        if (null != mTabLayout) {
            mTabLayout.setupWithViewPager(mViewPager);
        }

        return root;
    }


    String[][] news = new String[][]{
            {"android", "http://www.infoq.com/cn/android/news/"},
            {"移动", "http://www.infoq.com/cn/mobile/news/"},
            {"架构", "http://www.infoq.com/cn/architecture-design/news/"},
            {"iOS", "http://www.infoq.com/cn/iOS/news/"},
            {"Html5", "http://www.infoq.com/cn/html-5/news/"},
            {"视频", "http://www.infoq.com/cn/presentations/"},
    };


    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == news.length - 1) {
                return InfoqVideoFragment.newInstance(news[position][1]);
            } else {
                return InfoqNewsFragment.newInstance(news[position][1],news[position][0]);
            }
        }

        @Override
        public int getCount() {
            return news.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return news[position][0];
        }

    }
}
