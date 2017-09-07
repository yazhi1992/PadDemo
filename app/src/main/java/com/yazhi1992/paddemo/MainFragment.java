package com.yazhi1992.paddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;

/**
 * Created by zengyazhi on 2017/9/7.
 */

public class MainFragment extends BaseFragment {
    @BindView(R.id.tv_main_fragment)
    TextView mTvMainFragment;
    private String mTitle;

    public static MainFragment getInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.FRAGMENT_TITLE, title);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(Constant.FRAGMENT_TITLE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mTvMainFragment.setText(getTitle());
    }

    @Override
    int initLayoutId() {
        return R.layout.fragment_main;
    }

    public String getTitle() {
        return mTitle;
    }
}
