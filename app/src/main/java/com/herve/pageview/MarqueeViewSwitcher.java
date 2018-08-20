package com.herve.pageview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/19.
 */
public class MarqueeViewSwitcher extends CommonViewSwitcher<PageBean> {

    /**数据中第pos个*/
    private int pos;

    private final int INTERVAL_DEFAULT = 3000;

    private final int FLAG_AUTO_SCROLL_START = 0;
    private boolean isScroller = false;

    private Handler handler;
    private List<PageBean> mDatas;

    public void setPos(int index){
        this.pos = index;
    }

    public void setData(List<PageBean> list){
        mDatas = list;
    }

    public MarqueeViewSwitcher(Context context) {
        this(context, null);
    }

    public MarqueeViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 开始滚动
     */
    public void startAutoScroll() {
        if(!isScroller) {
            isScroller = true;
            if (handler == null) {
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case FLAG_AUTO_SCROLL_START:
                                if (mDatas != null && mDatas.size() > 0) {
                                    pos++;
                                    PageBean seq = mDatas.get(pos % mDatas.size());
                                    final View view = getNextView();
                                    bindData(view, seq);
                                    setInAnimation(inPullUp);
                                    setOutAnimation(outPullUp);
                                    showNext();
                                }
                                handler.sendEmptyMessageDelayed(FLAG_AUTO_SCROLL_START, INTERVAL_DEFAULT);
                                break;
                        }
                    }
                };
            }
            handler.sendEmptyMessage(FLAG_AUTO_SCROLL_START);
        }
    }

    /**
     * 停止滚动
     */
    public void stopAutoScroll() {
        if(isScroller) {
            isScroller = false;
            if (handler != null) {
                handler.removeMessages(FLAG_AUTO_SCROLL_START);
            }
        }
    }

    public void bindData(View view, PageBean bean){
        ((TextView)view.findViewById(R.id.tv_index_1st)).setText(bean.name);
        ((TextView)view.findViewById(R.id.tv_index_2nd)).setText(bean.code);
    }
}
