package com.herve.pageview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by Administrator on 2017/8/19.
 */
public class PageViewSwitcher extends CommonViewSwitcher<PageBean> {

    /**数据中第pos个*/
    private int pos;

    public void setPos(int index){
        this.pos = index;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.obj != null && msg.obj instanceof PageBean){
                bindData(getCurrentView(), (PageBean) msg.obj);
            }
        }
    };

    public PageViewSwitcher(Context context) {
        this(context, null);
    }

    public PageViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void refreshView(View view){
        ((TextView)view.findViewById(R.id.tv_1st)).setText("--");
        ((TextView)view.findViewById(R.id.tv_2nd)).setText("--");
        ((TextView)view.findViewById(R.id.tv_3rd)).setText("--");
    }

    private void setLabel(View view, PullToRefreshBase.Mode mode, String... strs){
        if(view instanceof PullToRefreshBase) {
            if (strs != null && strs.length > 0) {
                ((PullToRefreshBase)view).setPullLabel(strs[0], mode);
            }
            if (strs != null && strs.length > 1) {
                ((PullToRefreshBase)view).setReleaseLabel(strs[1], mode);
            }
            if (strs != null && strs.length > 2) {
                ((PullToRefreshBase)view).setRefreshingLabel(strs[2], mode);
            }
        }
    }

    public void bindData(View view, PageBean bean){
        ((TextView)view.findViewById(R.id.tv_1st)).setText(bean.name);
        ((TextView)view.findViewById(R.id.tv_2nd)).setText(bean.code);
        ((TextView)view.findViewById(R.id.tv_3rd)).setText(bean.upDown);
        view.findViewById(R.id.ll_content).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "第" + pos + "个", Toast.LENGTH_SHORT).show();
            }
        });
        if(view instanceof PullToRefreshPageScrollView) {
            ((PullToRefreshPageScrollView) view).setMode(PullToRefreshBase.Mode.BOTH);
            if (pos == 0) {
                setLabel(view, PullToRefreshBase.Mode.PULL_FROM_START, "不能向上翻页了", "不能向上翻页了", "不能向上翻页了");
                setLabel(view, PullToRefreshBase.Mode.PULL_FROM_END, "上拉查看下一页", "松开查看下一页", "松开查看下一页");
            }else if(pos == 5){
                setLabel(view, PullToRefreshBase.Mode.PULL_FROM_START, "下拉查看上一页", "松开查看上一页", "松开查看上一页");
                setLabel(view, PullToRefreshBase.Mode.PULL_FROM_END, "已滑到底部了", "已滑到底部了", "已滑到底部了");
            }else {
                setLabel(view, PullToRefreshBase.Mode.PULL_FROM_START, "下拉查看上一页", "松开查看上一页", "松开查看上一页");
                setLabel(view, PullToRefreshBase.Mode.PULL_FROM_END, "上拉查看下一页", "松开查看下一页", "松开查看下一页");
            }
            ((PullToRefreshPageScrollView) view).setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
                @Override
                public void onPullDownToRefresh(final PullToRefreshBase<ScrollView> refreshView) {
                    if(pos <= 0){
                        refreshView.onRefreshComplete();
                        return;
                    }
                    pos--;
                    outPullDown.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            refreshView.onRefreshComplete();
                            refreshView.getRefreshableView().scrollTo(0,0);
                            refreshView(refreshView);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    setInAnimation(inPullDown);
                    setOutAnimation(outPullDown);
                    showPrevious();
                    request(pos);
                }

                @Override
                public void onPullUpToRefresh(final PullToRefreshBase<ScrollView> refreshView) {
                    if(pos >= 5){
                        refreshView.onRefreshComplete();
                        return;
                    }
                    pos++;
                    outPullUp.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            refreshView.onRefreshComplete();
                            refreshView.getRefreshableView().scrollTo(0,0);
                            refreshView(refreshView);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    setInAnimation(inPullUp);
                    setOutAnimation(outPullUp);
                    showNext();
                    request(pos);
                }
            });
        }
    }

    private void request(final int pos){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PageBean bean = new PageBean();
                bean.name = "name" + pos;
                bean.code = "code" + pos;
                bean.upDown = "upDown" + pos + "upDown" + pos + "upDown" + pos;
                Message msg = Message.obtain();
                msg.obj = bean;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
