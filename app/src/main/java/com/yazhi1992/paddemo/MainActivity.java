package com.yazhi1992.paddemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tab1)
    TextView mTab1;
    @BindView(R.id.tab2)
    TextView mTab2;
    @BindView(R.id.tab3)
    TextView mTab3;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
    }

    @OnClick({R.id.tab1, R.id.tab2, R.id.tab3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                break;
            case R.id.tab2:
                break;
            case R.id.tab3:
                break;
        }
    }
}
