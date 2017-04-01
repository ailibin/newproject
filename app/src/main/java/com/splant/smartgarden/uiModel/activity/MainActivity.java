package com.splant.smartgarden.uiModel.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.splant.smartgarden.R;
import com.splant.smartgarden.baseModel.BaseActivity;
import com.splant.smartgarden.baseModel.BaseFragment;
import com.splant.smartgarden.beanModel.Event.ChangeEvent;
import com.splant.smartgarden.customModel.CustomViewPager;
import com.splant.smartgarden.uiModel.fragment.PlantFragment;
import com.splant.smartgarden.uiModel.fragment.PlantTypeFragment;
import com.splant.smartgarden.uiModel.fragment.ProfileFragment;
import com.splant.smartgarden.uiModel.fragment.WeatherFragment;
import com.splant.smartgarden.utilModel.AppManager;
import com.splant.smartgarden.utilModel.ToastUtil;

import java.util.ArrayList;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {

    @Bind(R.id.main_BottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;
    @Bind(R.id.main_ViewPager)
    CustomViewPager viewPager;

    private String[] main_strings;
    private ArrayList<BaseFragment> fragments;
    private long exitTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System_Util.configLanguage(this);
        setContentView(R.layout.activity_main);
        main_strings = getResources().getStringArray(R.array.bottom_title);
        //注册EventBus
        EventBus.getDefault().register(this);
        viewPager.setCurrentItem(0);
        titlebarView.setTitleBarDismiss(true);
        titlebarView.setBackgroundColor(getResources().getColor(R.color.system_titleBarBg));
        //禁止viewpager滑动
        viewPager.setNoScroll(true);
        fragments = getFragments();
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);//缓存页面3个  显示1个
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        titlebarView.setTitleBarDismiss(true);
                        break;
                    case 1:
                        titlebarView.setTitleBarDismiss(true);
                        break;
                    case 2:
                        titlebarView.setTitleBarDismiss(true);
                        break;
                    default:
                        titlebarView.setTitleBarDismiss(false);
                        titlebarView.setBtnLeftVisable(false);
                        titlebarView.setTitle(main_strings[position]);
                        titlebarView.setBackgroundColor(getResources().getColor(R.color.actionbar_bg_color));
                        break;
                }
                //跟着viewpager切换
                bottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        //设置背景颜色
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_tab_plant_checked, main_strings[0]).setActiveColorResource(R.color.bg_bottom))
                .addItem(new BottomNavigationItem(R.mipmap.ic_tab_weather_checked, main_strings[1]).setActiveColorResource(R.color.bg_bottom))
                .addItem(new BottomNavigationItem(R.mipmap.ic_tab_book_checked, main_strings[2]).setActiveColorResource(R.color.bg_bottom))
                .addItem(new BottomNavigationItem(R.mipmap.ic_tab_me_checked, main_strings[3]).setActiveColorResource(R.color.bg_bottom))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private ArrayList<BaseFragment> getFragments() {
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(PlantFragment.newInstance(main_strings[0]));
        fragments.add(WeatherFragment.newInstance(main_strings[1]));
        fragments.add(PlantTypeFragment.newInstance(main_strings[2]));
        fragments.add(ProfileFragment.newInstance(main_strings[3]));
        return fragments;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    /**
     * 用户按返回键的处理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.toastShort(this, R.string.press_again);
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.finishAllActivity();
        }
    }

    /**
     * 接收其他界面发送过来的消息
     */
    public void onEventMainThread(ChangeEvent event) {
        if ("change_language".equals(event.getMsg())) {
            Jump2Activity(MainActivity.class);
//            switchLanguage(event.getType());
            ArrayList<BaseFragment> fragments = getFragments();
            for (int i = 0; i < fragments.size(); i++) {
                fragments.get(i).setNeedRefresh();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }
}
