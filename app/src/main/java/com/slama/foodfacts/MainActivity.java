package com.slama.foodfacts;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.slama.foodfacts.fragment.HistoryFragment;
import com.slama.foodfacts.fragment.SearchFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {



    @BindView(R.id.tab_layout)
    TabLayout tab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tab.setTabTextColors(
                getColor(this, R.color.white),
                getColor(this, R.color.colorAccent)
        );
        //create tabs title
        tab.addTab(tab.newTab().setText(getResources().getString(R.string.search)));
        tab.addTab(tab.newTab().setText(getResources().getString(R.string.history)));


        //replace default fragment
        replaceFragment(new SearchFragment());

        //handling tab click event
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new SearchFragment());
                } else if (tab.getPosition() == 1) {
                    replaceFragment(new HistoryFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
    }

}
