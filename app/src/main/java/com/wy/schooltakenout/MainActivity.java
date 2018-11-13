package com.wy.schooltakenout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.wy.schooltakenout.BottomNavition.AppreciateFragment;
import com.wy.schooltakenout.BottomNavition.HomeFragment;
import com.wy.schooltakenout.BottomNavition.MyinfoFragment;
import com.wy.schooltakenout.Tool.TestPrinter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Wang Chenguang
 * 加入具有缓存机制的底部导航栏
 */

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private String TAG = MainActivity.class.getSimpleName();
    private MyinfoFragment mMyinfoFragment;
    private AppreciateFragment mAppreciateFragment;
    private HomeFragment mHomeFragment;
    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fm = getSupportFragmentManager();
        /**
         * bottomNavigation 设置
         */

        ButterKnife.bind(this);

        /** 导航基础设置 包括按钮选中效果 导航栏背景色等 */
        bottomNavigationBar
                .setTabSelectedListener(this)
                .setMode(BottomNavigationBar.MODE_FIXED)
                /**
                 *  setMode() 内的参数有三种模式类型：
                 *  MODE_DEFAULT 自动模式：导航栏Item的个数<=3 用 MODE_FIXED 模式，否则用 MODE_SHIFTING 模式
                 *  MODE_FIXED 固定模式：未选中的Item显示文字，无切换动画效果。
                 *  MODE_SHIFTING 切换模式：未选中的Item不显示文字，选中的显示文字，有切换动画效果。
                 */

                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                /**
                 *  setBackgroundStyle() 内的参数有三种样式
                 *  BACKGROUND_STYLE_DEFAULT: 默认样式 如果设置的Mode为MODE_FIXED，将使用BACKGROUND_STYLE_STATIC
                 *                                    如果Mode为MODE_SHIFTING将使用BACKGROUND_STYLE_RIPPLE。
                 *  BACKGROUND_STYLE_STATIC: 静态样式 点击无波纹效果
                 *  BACKGROUND_STYLE_RIPPLE: 波纹样式 点击有波纹效果
                 */

                .setActiveColor("#FF107FFD") //选中颜色
                .setInActiveColor("#e9e6e6") //未选中颜色
                .setBarBackgroundColor("#c0c0c0");//导航栏背景色

        /** 添加导航按钮 */
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_xin, "鉴赏"))
                .addItem(new BottomNavigationItem(R.drawable.ic_aboutme, "我的"))
                .setFirstSelectedPosition(lastSelectedPosition )
                .initialise(); //initialise 一定要放在 所有设置的最后一项
        setDefaultFragment();//设置默认导航栏
    }

    /**
     * 设置默认导航栏
     */
    private void setDefaultFragment() {
        showFragment(0);
    }
    /**
     * 设置导航选中的事件
     */
    @Override
    public void onTabSelected(int position) {
        Log.d(TAG, "onTabSelected() called with: " + "position = [" + position + "]");
        switch (position) {
            case 0:
                showFragment(0);
                break;
            case 1:
                showFragment(1);
                break;
            case 2:
                showFragment(2);
                break;
        }

    }
    public void showFragment(int index) {
        FragmentTransaction ft = fm.beginTransaction();
        // 想要显示一个fragment,先隐藏全部fragment。防止重叠
        hideFragments(ft);
        switch (index){
            case 0:
                // 假设fragment1已经存在则将其显示出来
                if (mHomeFragment != null)
                    ft.show(mHomeFragment);
                    // 否则是第一次切换则加入fragment1，注意加入后是会显示出来的。replace方法也是先remove后add
                else {
                    Intent intent = getIntent();
                    int userID = intent.getIntExtra("userID", 0);
                    mHomeFragment = HomeFragment.newInstance(userID);
                    ft.add(R.id.tb, mHomeFragment);
                }
                break;
            case 1:
                if (mAppreciateFragment != null)
                    ft.show(mAppreciateFragment);
                else {
                    mAppreciateFragment = AppreciateFragment.newInstance();
                    ft.add(R.id.tb, mAppreciateFragment);
                }
                break;
            case 2:
                if (mMyinfoFragment != null)
                    ft.show(mMyinfoFragment);
                else {
                    Intent intent = getIntent();
                    int userID = intent.getIntExtra("userID", 0);
                    mMyinfoFragment = MyinfoFragment.newInstance(userID);
                    ft.add(R.id.tb, mMyinfoFragment);
                }
                break;
        }
        ft.commit();
    }

    // 当fragment已被实例化，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (mHomeFragment != null)
            ft.hide(mHomeFragment);
        if (mAppreciateFragment != null)
            ft.hide(mAppreciateFragment);
        if (mMyinfoFragment != null)
            ft.hide(mMyinfoFragment);
    }

    /**
     * 设置未选中Fragment 事务
     */
    @Override
    public void onTabUnselected(int position) {

    }

    /**
     * 设置释放Fragment 事务
     */
    @Override
    public void onTabReselected(int position) {

    }
}