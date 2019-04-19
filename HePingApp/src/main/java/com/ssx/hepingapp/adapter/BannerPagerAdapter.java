package com.ssx.hepingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ssx.hepingapp.R;

import java.util.List;

public class BannerPagerAdapter extends PagerAdapter {
    private List<String> bannerList;
    private Context context;
    private ViewPager viewPager;

    public BannerPagerAdapter(Context context, ViewPager viewPager, List<String> bannerList) {
        this.context = context;
        this.viewPager = viewPager;
        this.bannerList = bannerList;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.layout_home_banner, null);
        ImageView banner = view.findViewById(R.id.banner);
        Glide.with(context).load(bannerList.get(position)).into(banner);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        int position = viewPager.getCurrentItem();

        if (position == 0) {
            position = bannerList.size() - 2;
            viewPager.setCurrentItem(position, false);
        } else if (position == bannerList.size() - 1) {
            position = 1;
            viewPager.setCurrentItem(position, false);
        }
    }
}
