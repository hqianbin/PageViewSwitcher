package com.herve.coordinator.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herve.R;
import com.herve.coordinator.UtilsSpannable;

public class TabPageIndicatorV2 extends LinearLayout {

    private static final int GRAVITY_CENTER = 0;
    private static final int GRAVITY_CENTER_LEFT = 1;
    private static final int GRAVITY_CENTER_RIGHT = 2;
    private static final int GRAVITY_CENTER_BOTTOM = 3;
    private static final int GRAVITY_LEFT_BOTTOM = 4;
    private static final int GRAVITY_RIGHT_BOTTOM = 5;

    private final int SELECT_COLOR_DEFAULT = Color.parseColor("#DF4242");
    private final int UNSELECT_COLOR_DEFAULT = Color.parseColor("#666666");
    private final float TEXT_SIZE_DEFAULT = 16;
    private final int SELECT_POS_DEFAULT = 0;
    private final int NO_ARRAY_ID = -1;
    private final String SYMBOL = "#";

    private final int INDICTOR_WIDTH = dp2pxInt(15);
    private final int INDICTOR_HEIGHT = dp2pxInt(3);
    private final int INDICTOR_MARGIN_TOP = -dp2pxInt(6);

    private int arrayId;
    private Context context;
    private String[] strTextArray;
    private int selectedPos;
    private Paint paint;
    private String text;

    private TextView[] tvArray;
    private ImageView ivIndictor;
    private float selectedTextSize, unSelectedTextSize;
    private int selectedTextColor, unSelectedTextColor;
    private Drawable selectedBackground, unSelectedBackground;
    private boolean isBold, showArr, showIndictor;
    private int textGravity = GRAVITY_CENTER;

    private int ivIndictorWidth, ivIndictorHeight, ivIndictorMarginTop;
    private Drawable ivIndictorSrc, ivArrowSrc;

    public TabPageIndicatorV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabPageIndicatorV2);
        arrayId = a.getResourceId(R.styleable.TabPageIndicatorV2_tp_v2_array_id, NO_ARRAY_ID);
        isBold = a.getBoolean(R.styleable.TabPageIndicatorV2_tp_v2_bold, false);
        showArr = a.getBoolean(R.styleable.TabPageIndicatorV2_tp_v2_show_arrow, false);
        ivArrowSrc = a.getDrawable(R.styleable.TabPageIndicatorV2_tp_v2_arrow_src);

        unSelectedTextColor = a.getColor(R.styleable.TabPageIndicatorV2_tp_v2_unselected_textcolor, UNSELECT_COLOR_DEFAULT);
        selectedTextColor = a.getColor(R.styleable.TabPageIndicatorV2_tp_v2_selected_textcolor, SELECT_COLOR_DEFAULT);
        unSelectedTextSize = a.getFloat(R.styleable.TabPageIndicatorV2_tp_v2_unselected_text_size, TEXT_SIZE_DEFAULT);
        selectedTextSize = a.getFloat(R.styleable.TabPageIndicatorV2_tp_v2_selected_text_size, TEXT_SIZE_DEFAULT);
        unSelectedBackground = a.getDrawable(R.styleable.TabPageIndicatorV2_tp_v2_unselected_background);
        selectedBackground = a.getDrawable(R.styleable.TabPageIndicatorV2_tp_v2_selected_background);

        selectedPos = a.getInt(R.styleable.TabPageIndicatorV2_tp_v2_selected_position, SELECT_POS_DEFAULT);

        showIndictor = a.getBoolean(R.styleable.TabPageIndicatorV2_tp_v2_show_indicator, true);
        ivIndictorWidth = a.getDimensionPixelOffset(R.styleable.TabPageIndicatorV2_tp_v2_indicator_width, INDICTOR_WIDTH);
        ivIndictorHeight = a.getDimensionPixelOffset(R.styleable.TabPageIndicatorV2_tp_v2_indicator_height, INDICTOR_HEIGHT);
        ivIndictorMarginTop = a.getDimensionPixelOffset(R.styleable.TabPageIndicatorV2_tp_v2_indicator_marginTop, INDICTOR_MARGIN_TOP);
        ivIndictorSrc = a.getDrawable(R.styleable.TabPageIndicatorV2_tp_v2_indicator_src);

        textGravity = a.getInt(R.styleable.TabPageIndicatorV2_tp_v2_text_gravity, GRAVITY_CENTER);

        a.recycle();
        init();
    }

    public void changeSkin(int unSelectedColor, int selectedColor, Drawable unSelectedDrawable, Drawable selectedDrawable, Drawable background) {
        unSelectedTextColor = unSelectedColor;
        selectedTextColor = selectedColor;
        if (unSelectedBackground != null && unSelectedDrawable != null) {
            unSelectedBackground = unSelectedDrawable;
        }
        if (selectedBackground != null && selectedDrawable != null) {
            selectedBackground = selectedDrawable;
        }
        if (background != null) {
            setBackgroundDrawable(background);
        }
        resetText();
    }

    private void init() {
        this.context = this.getContext();
        paint = new Paint();
        paint.setTextSize(sp2px(selectedTextSize));
        ivIndictor = new ImageView(context);
        if (ivIndictorSrc == null) {
            ivIndictorSrc = getResources().getDrawable(R.drawable.shape_red_round);
        }
        ivIndictor.setBackgroundDrawable(ivIndictorSrc);
        LayoutParams ivParam = new LayoutParams(ivIndictorWidth, ivIndictorHeight);
        ivParam.topMargin = ivIndictorMarginTop;
        ivIndictor.setLayoutParams(ivParam);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        if (arrayId == NO_ARRAY_ID) {
            return;
        }

        strTextArray = getResources().getStringArray(arrayId);
        addItems();
    }

    private void addItems() {
        tvArray = new TextView[strTextArray.length];
        if (selectedPos < 0 || selectedPos >= strTextArray.length) {
            selectedPos = SELECT_POS_DEFAULT;
        }
        LinearLayout llText = new LinearLayout(context);
        LayoutParams llParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        llText.setOrientation(LinearLayout.HORIZONTAL);
        llText.setLayoutParams(llParam);

        for (int i = 0; i < strTextArray.length; i++) {
            LayoutParams tvParam = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            tvParam.gravity = Gravity.CENTER;
            tvParam.weight = 1.0f;

            TextView child = new TextView(context);
            child.setTag(i);
            child.setEllipsize(TextUtils.TruncateAt.END);
            child.setLines(1);

            if(textGravity == GRAVITY_CENTER_LEFT){
                child.setGravity(Gravity.CENTER | Gravity.LEFT);
            }else if(textGravity == GRAVITY_CENTER_RIGHT){
                child.setGravity(Gravity.CENTER | Gravity.RIGHT);
            }else if(textGravity == GRAVITY_CENTER_BOTTOM){
                child.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            }else if(textGravity == GRAVITY_LEFT_BOTTOM){
                child.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            }else if(textGravity == GRAVITY_RIGHT_BOTTOM){
                child.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
            }else {
                child.setGravity(Gravity.CENTER);
            }

            if (selectedPos == i) {
                if (selectedBackground != null) {
                    child.setBackgroundDrawable(selectedBackground);
                }
                child.setTextColor(selectedTextColor);
                child.setTextSize(selectedTextSize);
            } else {
                if (unSelectedBackground != null) {
                    child.setBackgroundDrawable(unSelectedBackground);
                }
                child.setTextColor(unSelectedTextColor);
                child.setTextSize(unSelectedTextSize);
            }

            if (showArr) {
                CenterImageSpan imageSpan = new CenterImageSpan(context, R.mipmap.arr_down);
                if (ivArrowSrc != null) {
                    imageSpan = new CenterImageSpan(ivArrowSrc);
                }
                child.setText(UtilsSpannable.style(strTextArray[i], SYMBOL, imageSpan));
            } else {
                child.setText(strTextArray[i]);
            }

            if (isBold) {
                TextPaint tp = child.getPaint();
                tp.setFakeBoldText(true);
            }
            tvArray[i] = child;
            llText.addView(child, tvParam);

            final int index = i;
            tvArray[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPos != index && onItemClickLisener != null) {
                        onItemClickLisener.onItemClickLisener(tvArray[index], index);
                    }
                    setSelectedPosNotLisener(index);
                }
            });
        }
        removeAllViews();
        addView(llText);

        post(new Runnable() {
            @Override
            public void run() {
                if (showIndictor) {
                    removeView(ivIndictor);
                    int itemWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / strTextArray.length;
                    LayoutParams ivParam = (LayoutParams) ivIndictor.getLayoutParams();
                    ivParam.leftMargin = (itemWidth - ivIndictorWidth) / 2 + selectedPos * itemWidth;

                    text = strTextArray[selectedPos];
                    float txtWidth = paint.measureText(text);
                    if(textGravity == GRAVITY_CENTER_LEFT || textGravity == GRAVITY_LEFT_BOTTOM){
                        ivParam.leftMargin = (int) (Math.abs(txtWidth - ivIndictorWidth) / 2.0F) + selectedPos * itemWidth;
                    }else if(textGravity == GRAVITY_CENTER_RIGHT || textGravity == GRAVITY_RIGHT_BOTTOM){
                        ivParam.leftMargin = (itemWidth - (int) ((txtWidth + ivIndictorWidth) / 2.0F)) + selectedPos * itemWidth;
                    }else {
                        ivParam.leftMargin = (itemWidth - ivIndictorWidth) / 2 + selectedPos * itemWidth;
                    }
                    addView(ivIndictor, ivParam);
                }
            }
        });
    }

    private void resetText() {
        for (int i = 0; i < strTextArray.length; i++) {
            if (selectedPos == i) {
                tvArray[i].setTextColor(selectedTextColor);
                tvArray[i].setTextSize(selectedTextSize);
                if (selectedBackground != null) {
                    tvArray[i].setBackgroundDrawable(selectedBackground);
                } else {
                    tvArray[i].setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                }
            } else {
                tvArray[i].setTextColor(unSelectedTextColor);
                tvArray[i].setTextSize(unSelectedTextSize);
                if (unSelectedBackground != null) {
                    tvArray[i].setBackgroundDrawable(unSelectedBackground);
                } else {
                    tvArray[i].setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                }
            }
        }
    }

    private void indictorMove(int leftMargin, int width) {
        if (!showIndictor) {
            return;
        }
        LayoutParams params = (LayoutParams) ivIndictor.getLayoutParams();
        if (params != null) {
            params.leftMargin = leftMargin;
            params.width = width;
            ivIndictor.setLayoutParams(params);
            ivIndictor.requestLayout();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

//    private int getPosition(float x){
//        if(strTextArray == null || strTextArray.length == 0){
//            return -1;
//        }
//        int iWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / strTextArray.length;
//        for (int i = 0; i < strTextArray.length; i++){
//            if(getPaddingLeft() + i * iWidth < x && x <= i * iWidth + iWidth + getPaddingLeft()){
//                return i;
//            }
//        }
//        return -1;
//    }

    private OnItemClickLisener onItemClickLisener;

    public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
        this.onItemClickLisener = onItemClickLisener;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        return true;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                int selectedIndex = getPosition(event.getX());
//                if(selectedIndex != -1) {
//                    setSelectedPosition(selectedIndex);
//                }
//                break;
//        }
//        return true;
//    }

    public interface OnItemClickLisener {
        void onItemClickLisener(View view, int position);
    }

    public void setTextArray(String[] texts) {
        this.strTextArray = texts;
        addItems();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPosition(int selectedPosition) {
        if (selectedPos != selectedPosition && onItemClickLisener != null) {
            onItemClickLisener.onItemClickLisener(tvArray[selectedPosition], selectedPosition);
        }
        setSelectedPosNotLisener(selectedPosition);
    }

    public void setSelectedPosNotLisener(int selectedPosition) {
        if (selectedPos != selectedPosition) {
            selectedPos = selectedPosition;
            resetText();
        }
    }

    public void setIndictorMove(int pos, float posOffset) {
        if (!showIndictor || strTextArray == null || strTextArray.length == 0) {
            return;
        }
        if (posOffset >= 0 && posOffset <= 1) {
            int deltaWidth = (int) ((0.5f - Math.abs(posOffset - 0.5f)) * 3 * ivIndictorWidth);
            int width = ivIndictorWidth + deltaWidth;
            int iWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / strTextArray.length;

            text = strTextArray[selectedPos];
            float txtWidth = paint.measureText(text);

            int leftMargin;
            if(textGravity == GRAVITY_CENTER_LEFT || textGravity == GRAVITY_LEFT_BOTTOM){
                leftMargin = (int) (Math.abs(txtWidth - width) / 2.0F + (pos + posOffset) * iWidth);
            }else if(textGravity == GRAVITY_CENTER_RIGHT || textGravity == GRAVITY_RIGHT_BOTTOM){
                leftMargin = (int)(iWidth -  ((txtWidth + width) / 2.0F) + (pos + posOffset) * iWidth);
            }else {
                leftMargin = (int) ((iWidth - width) / 2.0F + (pos + posOffset) * iWidth);
            }
            indictorMove(leftMargin, width);
        }
    }

    private static float dp2px(float dpVal) {
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, r.getDisplayMetrics());
    }

    private static float sp2px(float spVal) {
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, r.getDisplayMetrics());
    }

    private static int dp2pxInt(float dp) {
        return (int) dp2px(dp);
    }

    private static int sp2pxInt(float dp) {
        return (int) sp2px(dp);
    }

}