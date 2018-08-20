package com.herve.pageview;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PageViewSwitcher viewSwitcher = (PageViewSwitcher)findViewById(R.id.page_view_switcher);
        PageBean bean = new PageBean();
        bean.name = "name init";
        bean.code = "code init";
        bean.upDown = "upDown init upDown init upDown init";
        viewSwitcher.bindData(bean);
    }
}
