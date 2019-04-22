package com.ssx.hepingapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

import com.ssx.hepingapp.R;
import com.ssx.hepingapp.fragment.HomeFragment;
import com.ssx.hepingapp.fragment.PersonalFragment;
import com.ssx.hepingapp.fragment.RecordFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final int TYPE_HOME = 0;
    private static final int TYPE_RECORD = 1;
    private static final int TYPE_PERSONAL = 2;
    private final int SDK_PERMISSION_REQUEST = 127;

    private FragmentManager fragmentManager;
    private static final Class<?> FRAGMENTS[] = {HomeFragment.class,
            RecordFragment.class, PersonalFragment.class};
    private Fragment currentFragment;
    private int viewType = -1;
    private BottomNavigationView navigationView;
    private String permissionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        initViews();
        //申请权限
        getPermissions();
    }

    private void initViews() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        changeFrag(TYPE_HOME);
    }

    private void changeFrag(int type) {
        if (viewType != type) {
            viewType = type;
            changeFrag(FRAGMENTS[viewType]);
        }
    }

    private void changeFrag(Class<?> fragmentClazz) {
        // hide all fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Class<?> fc : FRAGMENTS) {
            if (fc == fragmentClazz) {
                continue;
            }
            Fragment fragment = fragmentManager.findFragmentByTag(fc
                    .getCanonicalName());
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }

        Fragment lastFragment = fragmentManager.findFragmentByTag(fragmentClazz
                .getCanonicalName());
        if (lastFragment == null) {
            try {
                lastFragment = (Fragment) fragmentClazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            transaction.add(R.id.content, lastFragment,
                    fragmentClazz.getCanonicalName());
        } else {
            transaction.show(lastFragment);
        }
        transaction.commitAllowingStateLoss();
        currentFragment = lastFragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                changeFrag(TYPE_HOME);
                return true;
            case R.id.navigation_dashboard:
                changeFrag(TYPE_RECORD);
                return true;
            case R.id.navigation_notifications:
                changeFrag(TYPE_PERSONAL);
                return true;
            default:
                break;
        }
        return false;
    }

    public int getId() {
        return preferenceManager.getId();
    }

    public String getAvatar() {
        return preferenceManager.getAvatar();
    }

    public String getName() {
        return preferenceManager.getName();
    }

    public String getJob() {
        return preferenceManager.getJob();
    }

    public void logout() {
        preferenceManager.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /*
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
