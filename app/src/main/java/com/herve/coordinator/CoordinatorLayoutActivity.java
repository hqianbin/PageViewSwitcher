package com.herve.coordinator;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herve.R;
import com.herve.coordinator.view.CenterImageSpan;
import com.herve.coordinator.view.TabPageIndicatorV2;

public class CoordinatorLayoutActivity extends FragmentActivity {
    private LinearLayout head_layout;
    //private TabLayout toolbar_tab;
    private TabPageIndicatorV2 tpV2;
    private ViewPager viewPager;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CoordinatorLayout root_layout;

    private int offset;

    public int getOffset(){
        return offset;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_coordinator);

        try {
            BlurUtil.setTranslateStatusBar(this, R.id.v_status_bar);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        AppBarLayout app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout.LayoutParams params = (CollapsingToolbarLayout.LayoutParams)mToolbar.getLayoutParams();
        params.height = BlurUtil.getStatusBarHeight(this) + BlurUtil.dp2px(40);
        mToolbar.setLayoutParams(params);
        mToolbar.setPadding(0,BlurUtil.getStatusBarHeight(this),0,0);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        head_layout = (LinearLayout) findViewById(R.id.head_layout);
        root_layout = (CoordinatorLayout) findViewById(R.id.root_layout);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleMarginTop(BlurUtil.getStatusBarHeight(this));

        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                offset = verticalOffset;
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    if(TextUtils.isEmpty(mCollapsingToolbarLayout.getTitle()) || !mCollapsingToolbarLayout.getTitle().equals("首页动态")) {
                        mCollapsingToolbarLayout.setTitle("首页动态");
                    }
                } else {
                    mCollapsingToolbarLayout.setTitle("");
                }
            }
        });

        tpV2 = findViewById(R.id.tp_v2);

//        toolbar_tab = (TabLayout) findViewById(R.id.toolbar_tab);
        viewPager = (ViewPager) findViewById(R.id.main_vp_container);

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(vpAdapter);
//        main_vp_container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener
//                (toolbar_tab));
//        toolbar_tab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener
//                (main_vp_container));

        viewPager.setOnPageChangeListener(new HqOnPageChangeListener());

        tpV2.setOnItemClickLisener(new TabPageIndicatorV2.OnItemClickLisener() {
            @Override
            public void onItemClickLisener(View view, int position) {
                viewPager.setCurrentItem(position);
            }
        });

        ((TextView)findViewById(R.id.tv_activity_1st)).setText(UtilsSpannable.style("# 万2.5开户", "#",
                new CenterImageSpan(this, R.mipmap.trade_ad_hk)));
        ((TextView)findViewById(R.id.tv_activity_2nd)).setText(UtilsSpannable.style("# 港股大赛", "#",
                new CenterImageSpan(this, R.mipmap.trade_ad_hk)));
        ((TextView)findViewById(R.id.tv_activity_3rd)).setText(UtilsSpannable.style("# 一键猜涨跌", "#", new CenterImageSpan(this, R.mipmap.trade_ad_hk)));

        loadBlurAndSetStatusBar();
    }

    /**
     * 设置毛玻璃效果和沉浸状态栏
     */
    private void loadBlurAndSetStatusBar() {

    }

    class HqOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(final int position) {
            tpV2.setSelectedPosNotLisener(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            tpV2.setIndictorMove(position, positionOffset);
        }

        @Override
        public void onPageScrollStateChanged(int index) {

        }
    }
}
