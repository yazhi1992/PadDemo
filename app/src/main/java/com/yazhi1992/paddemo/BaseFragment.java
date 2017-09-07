package com.yazhi1992.paddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zengyazhi on 2017/9/7.
 */

public abstract class BaseFragment extends Fragment {

    private View mContentView;
    private Unbinder unbinder;

    abstract int initLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(initLayoutId(), container, false);
        }
        unbinder = ButterKnife.bind(this, mContentView);
        return mContentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
