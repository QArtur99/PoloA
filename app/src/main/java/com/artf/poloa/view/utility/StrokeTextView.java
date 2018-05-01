package com.artf.poloa.view.utility;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.artf.poloa.R;

public class StrokeTextView extends android.support.v7.widget.AppCompatTextView {

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        final ColorStateList textColor = getTextColors();

        TextPaint paint = this.getPaint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeMiter(10);
        this.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
        paint.setStrokeWidth(10);

        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);

        setTextColor(textColor);
        super.onDraw(canvas);
    }

}
