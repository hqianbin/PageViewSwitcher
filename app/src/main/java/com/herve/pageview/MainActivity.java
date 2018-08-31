package com.herve.pageview;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.herve.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    MarqueeViewSwitcher marqueeViewSwitcher;
    PageViewSwitcher pageViewSwitcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        marqueeViewSwitcher = (MarqueeViewSwitcher)findViewById(R.id.marquee_view_switcher);
        List<PageBean> beans = new ArrayList<>();
        beans.add(new PageBean("沪指", "000001"));
        beans.add(new PageBean("深指", "399001"));
        beans.add(new PageBean("创指", "399006"));
        marqueeViewSwitcher.setData(beans);
        marqueeViewSwitcher.startAutoScroll();

        pageViewSwitcher = (PageViewSwitcher)findViewById(R.id.page_view_switcher);
        PageBean bean = new PageBean();
        bean.name = "name init";
        bean.code = "code init";
        bean.upDown = "upDown init upDown init upDown init";
        pageViewSwitcher.bindData(pageViewSwitcher.getCurrentView(), bean);
    }

    protected void onDestroy(){
        marqueeViewSwitcher.stopAutoScroll();
        super.onDestroy();
    }
}
