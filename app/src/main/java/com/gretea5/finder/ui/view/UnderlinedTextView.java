package com.gretea5.finder.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class UnderlinedTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Paint paint = new Paint();

    public UnderlinedTextView(Context context) {
        super(context);
        init();
    }

    public UnderlinedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnderlinedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getCurrentTextColor());
        paint.setStrokeWidth(5); // 밑줄 두께를 조정하는 부분
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int lineHeight = getLineHeight();
        int lineCount = getLineCount();
        int baseline = getLineBounds(0, null);

        for (int i = 0; i < lineCount; i++) {
            int startY = baseline + (i * lineHeight) + 10; // 밑줄 위치 조정
            canvas.drawLine(0, startY, getWidth(), startY, paint);
        }
    }
}
