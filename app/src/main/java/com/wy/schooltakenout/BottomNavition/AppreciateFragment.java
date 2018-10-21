package com.wy.schooltakenout.BottomNavition;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.wy.schooltakenout.Activity.EditActivity;
import com.wy.schooltakenout.Adapter.ArticleAdapter;
import com.wy.schooltakenout.Adapter.TagAdapter;
import com.wy.schooltakenout.Banner.BannerPagerAdapter;
import com.wy.schooltakenout.Banner.BannerTimerTask;
import com.wy.schooltakenout.Banner.IndicatorView;
import com.wy.schooltakenout.Data.Article;
import com.wy.schooltakenout.Data.Tag;
import com.wy.schooltakenout.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppreciateFragment extends Fragment {

    /**
     * 添加文章
     */
    @BindView(R.id.add_article)
    FloatingActionButton addArticle;
    /**
     * 标签列表
     */
    @BindView(R.id.sort_list)
    RecyclerView mRecyclerView;
    /**
     * 标签适配器
     */
    private TagAdapter adapter;
    /**
     * 标签列表
     */
    private List<Tag> tagList=new ArrayList<>();
    /**
     * 文章列表
     */
    @BindView(R.id.article_list)
    RecyclerView aRecyclerView;
    private List<Article> articleList=new ArrayList<>();
    /**
     * 文章适配器
     */
    private ArticleAdapter articleAdapter;
    /**
     *轮播图
     */
    @BindView(R.id.vp_banner)
    ViewPager mViewPager;
    /**
     * 指示器
     */
    @BindView(R.id.idv_banner)
    IndicatorView mIndicatorView;
    /**
     * 适配器
     */
    private BannerPagerAdapter mBannerPagerAdapter;
    /**
     * 图片资源
     */
    private List<Integer> pictureList=new ArrayList<>();
    /**
     * 轮播图自动轮播消息
     */
    public static final int AUTOBANNER_CODE=0X1001;
    /**
     * 当前轮播图位置
     */
    private int mBannerPosition;
    /**
     * 自动轮播计时器
     */
    private Timer timer=new Timer();
    /**
     * 自动轮播任务
     */
    private BannerTimerTask mBannerTimerTask;
    /**
     * 用户当前是否点击轮播图
     */
    private boolean mIsUserTouched=false;
    /**
     * 轮播图Handler
     */
    Handler bannerHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            //当用户点击时，不进行轮播
            if(!mIsUserTouched){
                //获取当前位置
                mBannerPosition=mViewPager.getCurrentItem();
                //更换轮播图
                mBannerPosition=(mBannerPosition+1)%mBannerPagerAdapter.FAKE_BANNER_SIZE;
                mViewPager.setCurrentItem(mBannerPosition);
            }
            return true;
        }
    });
    public static AppreciateFragment newInstance() {
        AppreciateFragment fragment = new AppreciateFragment();
        return fragment;
    }

    public AppreciateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.appreciate_fragment, container, false);
        initDatas();
        initView(view);
        return view;
    }
    /**
     * 初始化数据
     */
    private void initDatas(){
        pictureList.add(R.drawable.zhoufangzun);
        pictureList.add(R.drawable.zongxianglisi);
        pictureList.add(R.drawable.zhoufangzun);
        pictureList.add(R.drawable.zongxianglisi);
    }
    private void initView(View v){
        ButterKnife.bind(this,v);
        addArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), EditActivity.class);
                startActivity(intent);
            }
        });
        initArticles();
        initTags();
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),5);
        GridLayoutManager articleLayoutManager=new GridLayoutManager(getContext(),1);
        aRecyclerView.setLayoutManager(articleLayoutManager);
        mRecyclerView.setLayoutManager(layoutManager);
        articleAdapter=new ArticleAdapter(articleList);
        adapter=new TagAdapter(tagList);
        mRecyclerView.setAdapter(adapter);
        aRecyclerView.setAdapter(articleAdapter);
        mBannerPagerAdapter=new BannerPagerAdapter(getContext(),pictureList);
        mViewPager.setAdapter(mBannerPagerAdapter);
        mIndicatorView.setViewPager(pictureList.size(),mViewPager);
        //设置默认起始位置，使开始可以向左边滑动
        mViewPager.setCurrentItem(pictureList.size()*100);
        mIndicatorView.setOnPageChangeListener(new IndicatorView.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN
                        || action == MotionEvent.ACTION_MOVE) {
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsUserTouched = false;
                }
                return false;
            }
        });
        startBannerTimer();
    }
    /**
     * 开始轮播
     */
    private void startBannerTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        if (mBannerTimerTask != null) {
            mBannerTimerTask.cancel();
        }
        mBannerTimerTask = new BannerTimerTask(bannerHandler);
        if (timer != null && mBannerTimerTask != null) {
            // 循环5秒执行
            timer.schedule(mBannerTimerTask, 5000, 5000);
        }
    }
    /**
     * 销毁时,关闭任务,防止异常
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBannerTimerTask) {
            mBannerTimerTask.cancel();
            mBannerTimerTask = null;
        }
    }
    /**
     * 初始化标签数据
     */
    private void initTags(){
        Tag taga=new Tag("周防尊",R.drawable.zhoufangzun);
        Tag tagb=new Tag("宗像礼司",R.drawable.zongxianglisi);
        tagList.clear();
        for(int i=0;i<4;i++){
            tagList.add(taga);
            tagList.add(tagb);
        }
    }
    /**
     * 初始化文章数据
     */
    private void initArticles(){
        Article article=new Article("重庆火锅","是大红大红的假按揭放假啊打开多久啊大的煎熬大口大口的煎熬的卡卡大大的骄傲扩大",
                R.drawable.ic_avatar,"三天前");
        articleList.clear();
        for(int i=0;i<10;i++){
            articleList.add(article);
        }
    }

}
