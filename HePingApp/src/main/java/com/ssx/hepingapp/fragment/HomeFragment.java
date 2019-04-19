package com.ssx.hepingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ssx.hepingapp.R;
import com.ssx.hepingapp.activity.MainActivity;
import com.ssx.hepingapp.adapter.BannerPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends BaseFragment {
    private static final int FIRST_PAGE = 1;
    private List<String> list = new ArrayList<String>();
    private ViewPager viewPager;
    private BannerPagerAdapter pagerAdapter;
    private int currentPosition;

    private Timer timer;
    private TimerTask timerTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        list.add("https://hpcg.zhihuidangjian.com/upload/201903/25/201903251131255040.jpg");
        list.add("https://hpcg.zhihuidangjian.com/upload/201904/02/201904021313509640.jpg");
        list.add("https://hpcg.zhihuidangjian.com/upload/201903/25/201903251131255040.jpg");
        list.add("https://hpcg.zhihuidangjian.com/upload/201904/02/201904021313509640.jpg");
        initViewpager();
        return view;
    }

    private void initViewpager() {
        pagerAdapter = new BannerPagerAdapter(context, viewPager, list);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(FIRST_PAGE, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == list.size() - 1) {
                    currentPosition = FIRST_PAGE;
                } else if (position == 0) {
                    // 如果索引值为0了,就设置索引值为倒数第二个
                    currentPosition = list.size() - 2;
                } else {
                    currentPosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //主线程中执行
                ((MainActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, false);
                    }
                });
            }
        };
        timer.schedule(timerTask, 3000, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
