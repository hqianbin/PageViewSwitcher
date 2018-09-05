package com.herve.coordinator;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;


public class UtilsSpannable {

    public static SpannableStringBuilder styleColor(String sup, String sub, int colorId) {
        return style(sup, sub, new ForegroundColorSpan(colorId));
    }

    public static SpannableStringBuilder style(String sup, String sub, CharacterStyle span) {
        CharacterStyle[] arrSpan = new CharacterStyle[]{span};
        return style(sup, sub, arrSpan);
    }

    public static SpannableStringBuilder style(String sup, String sub, CharacterStyle... span) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if(sup == null){
            builder.append("");
            return builder;
        }
        builder.append(sup);

        if(sub == null){
           return builder;
        }

        if(!sup.contains(sub)){
            return builder;
        }
        if(span == null || span.length == 0){
            return builder;
        }

        int start = sup.indexOf(sub);
        int end = start + sub.length();
        for(int i = 0; i < span.length; i++) {
            builder.setSpan(span[i], start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

}
