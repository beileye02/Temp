package com.ssx.hepingapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ssx.hepingapp.R;
import com.ssx.hepingapp.fragment.HomeFragment;
import com.ssx.hepingapp.fragment.PersonalFragment;
import com.ssx.hepingapp.fragment.RecordFragment;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final int TYPE_HOME = 0;
    private static final int TYPE_RECORD = 1;
    private static final int TYPE_PERSONAL = 2;

    private FragmentManager fragmentManager;
    private static final Class<?> FRAGMENTS[] = {HomeFragment.class,
            RecordFragment.class, PersonalFragment.class};
    private Fragment currentFragment;
    private int viewType = -1;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        initViews();
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
}
