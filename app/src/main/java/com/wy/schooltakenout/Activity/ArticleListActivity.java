package com.wy.schooltakenout.Activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wy.schooltakenout.Adapter.ArticleAdapter;
import com.wy.schooltakenout.Data.Article;
import com.wy.schooltakenout.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    /**
     * 具体分类文章列表和适配器
     */
    private List<Article> mArticleList=new ArrayList<>();
    private ArticleAdapter adapter;

    @BindView(R.id.tag_article_list)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        initView();
    }

    private void initView(){
        ButterKnife.bind(this);
        //处理toolbar的相关逻辑
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //处理文章列表相关逻辑
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        initArticles();
        mRecyclerView.setLayoutManager(layoutManager);
        adapter=new ArticleAdapter(mArticleList);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 初始化文章数据
     */
    private void initArticles(){
        Article article=new Article("重庆火锅","是大红大红的假按揭放假啊打开多久啊大的煎熬大口大口的煎熬的卡卡大大的骄傲扩大",
                R.drawable.ic_avatar,"三天前");
        mArticleList.clear();
        for(int i=0;i<10;i++){
            mArticleList.add(article);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:finish();break;
            default:
        }
        return true;
    }
}
