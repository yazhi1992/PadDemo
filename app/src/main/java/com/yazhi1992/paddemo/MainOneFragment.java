package com.yazhi1992.paddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zengyazhi on 2017/9/7.
 */

public class MainOneFragment extends BaseFragment {
    @BindView(R.id.tv_main_fragment)
    TextView mTvMainFragment;
    private String mTitle;

    public static MainOneFragment getInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.FRAGMENT_TITLE, title);
        MainOneFragment mainFragment = new MainOneFragment();
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
        mTvMainFragment.setText(getTitle());
    }

    @Override
    int initLayoutId() {
        return R.layout.fragment_main_one;
    }

    public String getTitle() {
        return mTitle;
    }

    @OnClick(R.id.btn_fragment_one)
    public void onViewClicked() {
        EventBus.getDefault().post(new StartFragmentEvent(Constant.CHILD_FRAGMENT_1));
    }
}
