package com.herve.pageview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by Administrator on 2017/8/19.
 */
public class PageViewSwitcher extends ViewSwitcher implements ViewSwitcher.ViewFactory {

    private final int DURATION_DEFAULT = 400;
    private int mDuration = DURATION_DEFAULT;

    Animation inPullDown, outPullDown, inPullUp, outPullUp;

    protected Context mContext;
    private int pos;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.obj != null && msg.obj instanceof PageBean){
                bindData((PageBean) msg.obj);
            }
        }
    };

    public PageViewSwitcher(Context context) {
        this(context, null);
    }

    public PageViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setFactory(this);
        init();
    }

    private void init(){
        post(new Runnable() {
            @Override
            public void run() {
                int height = getHeight();
                inPullUp = new TranslateAnimation(0, 0, height, 0);
                inPullUp.setDuration(mDuration);
                inPullUp.setInterpolator(new AccelerateInterpolator());
                outPullUp = new TranslateAnimation(0, 0, 0, -height);
                outPullUp.setDuration(mDuration);
                outPullUp.setInterpolator(new AccelerateInterpolator());

                inPullDown = new TranslateAnimation(0, 0, -height, 0);
                inPullDown.setDuration(mDuration);
                inPullDown.setInterpolator(new AccelerateInterpolator());
                outPullDown = new TranslateAnimation(0, 0, 0, height);
                outPullDown.setDuration(mDuration);
                outPullDown.setInterpolator(new AccelerateInterpolator());
            }
        });
    }

    @Override
    public View makeView() {
        PullToRefreshPageScrollView view = (PullToRefreshPageScrollView) View.inflate(mContext, R.layout.include_page_view_switcher, null);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        view.setMode(PullToRefreshBase.Mode.BOTH);
        view.setLayoutParams(layoutParams);

        return view;
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

    public void bindData(PageBean bean){
        View view = getCurrentView();
        ((TextView)view.findViewById(R.id.tv_1st)).setText(bean.name);
        ((TextView)view.findViewById(R.id.tv_2nd)).setText(bean.code);
        ((TextView)view.findViewById(R.id.tv_3rd)).setText(bean.upDown);
        if(view instanceof PullToRefreshPageScrollView) {
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
