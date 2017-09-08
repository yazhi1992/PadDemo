package com.yazhi1992.paddemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fudaojun.fudaojunlib.widget.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一开始是以传统的 tab + viewpager 来实现的，但是观察 QQHD ，竖屏时点击某个好友，对话框是直接覆盖整个屏幕的
 * 所以推测可能是有一个 match_parent 的 FrameLayout，在 Fragment 进栈时设为 visible
 * <p>
 * ----
 * <p>
 * 实现过程中发现，如果不使用 viewpager，则维护默认的 mainFragment 状态比较麻烦，仍然改为 viewpager 实现
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.tab1)
    TextView mTab1;
    @BindView(R.id.tab2)
    TextView mTab2;
    @BindView(R.id.tab3)
    TextView mTab3;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();
    private MainViewPagerAdapter mMainViewPagerAdapter;
    @BindView(R.id.container_second)
    FrameLayout mContainerSecond;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        Log.e("zyz", "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(StartFragmentEvent event) {
        //接收到首页三屏里跳转页面的点击事件
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("zyz", backStackEntryCount + " backStackEntryCount");
        mContainerSecond.setVisibility(View.VISIBLE);
        //根据tag构造fragment实例
        Fragment addOne = null;
        switch (event.getName()) {
            case Constant.CHILD_FRAGMENT_1:
                addOne = MainFragment.getInstance("子fragment");
                break;
            default:
                break;
        }
        addFragment(addOne, event.getName());
    }

    private void initView() {
        //添加首页三屏fragment
        mFragments.add(MainOneFragment.getInstance("首页一"));
        mFragments.add(MainFragment.getInstance("首页二"));
        mFragments.add(MainFragment.getInstance("首页三"));
        mMainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
    }

    class MainViewPagerAdapter extends FragmentPagerAdapter {

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    /**
     * 往容器里添加fragment
     * @param fragment
     * @param tag
     */
    private void addFragment(Fragment fragment, String tag) {
        if (fragment.isAdded()) {
            //防止重复加载
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_second, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    @OnClick({R.id.tab1, R.id.tab2, R.id.tab3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                //首页fragment切换，关闭过度动画
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.tab2:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.tab3:
                mViewPager.setCurrentItem(2, false);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("zyz", "onSaveInstanceState");
        //旋转屏幕保存数据
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            //已添加过fragment，FrameLayout设为visible
            outState.putBoolean(Constant.BOOLEAN_SHOW_FRAMELAYOUT, true);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("zyz", "onRestoreInstanceState");
        //旋转屏幕，恢复数据
        if (savedInstanceState.containsKey(Constant.BOOLEAN_SHOW_FRAMELAYOUT)) {
            //fragment栈不为空
            mContainerSecond.setVisibility(View.VISIBLE);
            //容器里设置栈顶的fragment
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            String currentFragmentBackStack = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.getBackStackEntryCount() - 1).getName();
            Fragment fragmentByTag = supportFragmentManager.findFragmentByTag(currentFragmentBackStack);
            addFragment(fragmentByTag, currentFragmentBackStack);
        }
    }

    /**
     * Manifest 设置 android:configChanges="orientation|screenSize" 后，不会重建 activity，所以
     * layout-land 和 layout-port 就失效了
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
            Log.e("zyz", "onConfigurationChanged ORIENTATION_LANDSCAPE");
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            Log.e("zyz", "onConfigurationChanged ORIENTATION_PORTRAIT");
        }
    }

    //双击退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("zyz", "backStackEntryCount " + backStackEntryCount);
        if (backStackEntryCount == 0) {
            if (keyCode == KeyEvent.KEYCODE_BACK && isTaskRoot()) {
                // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
                if (System.currentTimeMillis() - mExitTime > 2000) {
                    showToast("再按一次退出应用");
                    // 将系统当前的时间赋值给exitTime
                    mExitTime = System.currentTimeMillis();
                    return true;
                }
            }
        } else if(backStackEntryCount == 1){
            //栈底fragment pop 动画完成时，将 framelayout 设为 gone
            mContainerSecond.setVisibility(View.GONE);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示通知
     *
     * @param msg
     */
    public void showToast(String msg) {
        if (msg == null || msg.isEmpty()) return;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
