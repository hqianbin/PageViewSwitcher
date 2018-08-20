package com.herve.pageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ViewSwitcher;

import java.util.List;

/**
 * Created by Administrator on 2017/8/19.
 */
public abstract class CommonViewSwitcher<T> extends ViewSwitcher implements ViewSwitcher.ViewFactory {

    private final int DURATION_DEFAULT = 300;

    private int mDuration;
    private int mLayoutResource = 0;

    Animation inPullDown, outPullDown, inPullUp, outPullUp;

    protected Context mContext;
    private List<T> mDatas;

    public CommonViewSwitcher(Context context) {
        this(context, null);
    }

    public CommonViewSwitcher(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public CommonViewSwitcher(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CommonViewSwitcher(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CommonViewSwitcher, defStyleAttr, defStyleRes);

        mLayoutResource = a.getResourceId(R.styleable.CommonViewSwitcher_inflatedLayout, 0);
        mDuration = a.getInt(R.styleable.CommonViewSwitcher_duration, DURATION_DEFAULT);

        a.recycle();

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
        if(mLayoutResource == 0) {
            throw new IllegalStateException("PageViewSwitcher didn't contain a valid inflatedLayout");
        }
        View view = View.inflate(mContext, mLayoutResource, null);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);

        return view;
    }

    public abstract void bindData(View view, T bean);

}
