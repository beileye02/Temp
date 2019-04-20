package com.ssx.hepingapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.ssx.hepingapp.R;
import com.ssx.hepingapp.activity.MainActivity;
import com.ssx.hepingapp.activity.PolicyActivity;
import com.ssx.hepingapp.adapter.BannerPagerAdapter;
import com.ssx.hepingapp.utils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class HomeFragment extends BaseFragment {
    private static final int FIRST_PAGE = 1;
    private static final String TAG_BANNER = "banner";
    private RetrofitClient retrofitClient;
    private List<String> list = new ArrayList<String>();
    private ViewPager viewPager;
    private GridView gridView;
    private BannerPagerAdapter pagerAdapter;
    private int currentPosition;

    private Timer timer;
    private TimerTask timerTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        gridView = view.findViewById(R.id.grid);
        initGridView();
        getBannerList();
        return view;
    }

    private void initGridView() {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        int[] icons = {R.mipmap.ic_clock_in, R.mipmap.ic_policy, R.mipmap.ic_case_report, R.mipmap.ic_video};
        String[] texts = {
                getString(R.string.clock_in),
                getString(R.string.policy),
                getString(R.string.case_record),
                getString(R.string.live_video)};
        for (int i = 0; i < 4; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("icon", icons[i]);
            map.put("text", texts[i]);
            dataList.add(map);
        }
        String[] from = {"icon", "text"};
        int[] to = {R.id.icon, R.id.text};
        SimpleAdapter adapter = new SimpleAdapter(context, dataList, R.layout.layout_home_grid_item, from, to);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else if (position == 1) {
                    Intent intent = new Intent(context, PolicyActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getBannerList() {
        retrofitClient = RetrofitClient.getInstance();
        retrofitClient.getBannerList(new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    parseResult(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, TAG_BANNER);
    }

    private void initViewPager() {
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

        /*timer = new Timer();
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
        timer.schedule(timerTask, 3000, 3000);*/
    }

    private void parseResult(String result) {
        if (TextUtils.isEmpty(result)) {
            return;
        }
        try {
            JSONArray jsonArray = new JSONArray(result);
            int len = jsonArray.length();
            if (len > 0) {
                List<String> urlList = new ArrayList<String>();
                for (int i = 0; i < len; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String imageUrl = jsonObject.getString("img_url");
                    urlList.add(imageUrl);
                }
                List<String> imageList = new ArrayList<String>();
                imageList.add(urlList.get(len - 1));
                imageList.addAll(urlList);
                imageList.add(urlList.get(0));

                list.addAll(imageList);
                initViewPager();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        retrofitClient.unSubscribe(TAG_BANNER);
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
