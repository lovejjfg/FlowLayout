package com.lovejjfg.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.ehousechina.yier.view.widget.TintTextView;

/**
 * Created by Joe on 2017/5/10.
 * Email lovejjfg@gmail.com
 */

public class FlowTextView extends TintTextView {
    public FlowTextView(Context context) {
        super(context);
    }

    public FlowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = View.MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
