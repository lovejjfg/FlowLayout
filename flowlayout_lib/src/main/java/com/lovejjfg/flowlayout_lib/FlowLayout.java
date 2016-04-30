package com.lovejjfg.flowlayout_lib;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 2016/1/9.
 * Email lovejjfg@gmail.com
 */
public class FlowLayout extends ViewGroup {


    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    public static final int DEFAULT_SPACING = 20;
    /**
     * this is mean you can set an exact count in everyLine ,so you should call {@link #setExactCount(int)} to specify how many counts in which row!
     * <p>你使用精确模式的时候，记得调用 {@link #setExactCount(int)} 去指定每行的数量</p>
     */
    public static final int EXACT_MODE = 1;//精确的模式
    /**
     * this is mean you don't care how many counts in one row,it will fill one row by default!
     * of course ,you can call {@link FlowLayout#setLastFull(boolean)} to specify the last row whether fill the entire row or not!
     * <p>自由模式，不用关心每行有多少个子View,会自动填充，当然你可以{@link FlowLayout#setLastFull(boolean)}来指定最后一行是否填充一整行！</p>
     */
    public static final int FREE_MODE = 2; //自由模式

    private static final int SPACE_NO_NEED = -1;
    private static final int SPACE_NEED = -2;
    private static final int DEFALT_COUNT = 2;

    @SuppressWarnings("unused")
    public int getDefaultMode() {
        return mLayoutMode;
    }

    /**
     * you can use  {@link #EXACT_MODE}  or {@link #FREE_MODE}
     */
    @SuppressWarnings("unused")
    public void setDefaultMode(int layoutMode) {
        this.mLayoutMode = layoutMode;
        requestLayout();
    }

    private int mLayoutMode = FREE_MODE;
    private int mDefalCount = DEFALT_COUNT;
    /**
     * 横向间隔
     */
    private int mHorizontalSpacing = DEFAULT_SPACING;
    /**
     * 纵向间隔
     */
    private int mVerticalSpacing = DEFAULT_SPACING;
    /**
     * 是否需要布局，只用于第一次
     */
    boolean mNeedLayout = true;
    /**
     * 当前行已用的宽度，由子View宽度加上横向间隔
     */
    private int mUsedWidth = 0;
    /**
     * 代表每一行的集合
     */
    private final List<Line> mLines = new ArrayList<>();
    private Line mLine = null;
    /**
     * 最大的行数
     */
    private int mMaxLinesCount = Integer.MAX_VALUE;

    @SuppressWarnings("unused")
    public void setLastFull(boolean lastFull) {
        this.lastFull = lastFull;
        requestLayoutInner();

    }
    @SuppressWarnings("unused")
    public boolean getLastFull() {
        return lastFull;
    }
    /**
     * set the count in every row
     */
    @SuppressWarnings("unused")
    public void setExactCount(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("the defaltCount must't Less than 1 ");
        }
        mDefalCount = count;
        requestLayout();
    }

    private boolean lastFull = false;

    /**
     * set the HorizontalSpacing in px
     */
    @SuppressWarnings("unused")
    public void setHorizontalSpacing(int spacing) {
        if (mHorizontalSpacing != spacing) {
            mHorizontalSpacing = spacing;
            requestLayoutInner();
        }
    }
    /**
     * set the VerticalSpacing in px
     */
    @SuppressWarnings("unused")
    public void setVerticalSpacing(int spacing) {
        if (mVerticalSpacing != spacing) {
            mVerticalSpacing = spacing;
            requestLayoutInner();
        }
    }

    @SuppressWarnings("unused")
    public void setMaxLines(int count) {
        if (mMaxLinesCount != count) {
            mMaxLinesCount = count;
            requestLayoutInner();
        }
    }

    private void requestLayoutInner() {
        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int finalWidth = (sizeWidth - mHorizontalSpacing * ((mDefalCount - 1))) / mDefalCount;
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        restoreLine();// 还原数据，以便重新记录
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int childWidthMeasureSpec;
            //不同模式不同的测量方式
            if (mLayoutMode == EXACT_MODE) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY);
            } else if (mLayoutMode == FREE_MODE) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth, modeWidth == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeWidth);
            } else {
                throw new IllegalArgumentException("There is not your specified LayoutMode!! ");
            }
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight, modeHeight == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeHeight);
            // 测量child
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            if (mLine == null) {
                mLine = new Line();
            }
            int childWidth = child.getMeasuredWidth();
            mUsedWidth += childWidth;// 增加使用的宽度
            if (mUsedWidth <= sizeWidth) {// 使用宽度小于总宽度，该child属于这一行。
                mLine.addView(child);// 添加child
                mUsedWidth += mHorizontalSpacing;// 加上间隔
                if (mUsedWidth >= sizeWidth) {// 加上间隔后如果大于等于总宽度，需要换行
                    if (!newLine()) {
                        break;
                    }
                }
            } else {// 使用宽度大于总宽度。需要换行
                if (mLine.getViewCount() == 0) {// 如果这行一个child都没有，即使占用长度超过了总长度，也要加上去，保证每行都有至少有一个child
                    mLine.addView(child);// 添加child
                    if (!newLine()) {// 换行
                        break;
                    }
                } else {// 如果该行有数据了，就直接换行
                    if (!newLine()) {// 换行
                        break;
                    }
                    // 在新的一行，不管是否超过长度，先加上去，因为这一行一个child都没有，所以必须满足每行至少有一个child
                    mLine.addView(child);
                    mUsedWidth += childWidth + mHorizontalSpacing;
                }
            }
        }

        if (mLine != null && mLine.getViewCount() > 0 && !mLines.contains(mLine)) {
            // 由于前面采用判断长度是否超过最大宽度来决定是否换行，则最后一行可能因为还没达到最大宽度，所以需要验证后加入集合中
            mLines.add(mLine);
        }

        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = 0;
        final int linesCount = mLines.size();
        for (int i = 0; i < linesCount; i++) {// 加上所有行的高度
            totalHeight += mLines.get(i).mHeight;
        }
        totalHeight += mVerticalSpacing * (linesCount - 1);// 加上所有间隔的高度
        totalHeight += getPaddingTop() + getPaddingBottom();// 加上padding
        // 设置布局的宽高，宽度直接采用父view传递过来的最大宽度，而不用考虑子view是否填满宽度，因为该布局的特性就是填满一行后，再换行
        // 高度根据设置的模式来决定采用所有子View的高度之和还是采用父view传递过来的高度
        setMeasuredDimension(totalWidth, resolveSize(totalHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!mNeedLayout || changed) {//没有发生改变就不重新布局
            mNeedLayout = false;
            int left = getPaddingLeft();//获取最初的左上点
            int top = getPaddingTop();
            final int linesCount = mLines.size();
            for (int i = 0; i < linesCount; i++) {
                final Line oneLine = mLines.get(i);
                if (i < linesCount - 1) {
                    oneLine.layoutView(left, top);//布局每一行
                } else {
                    oneLine.layoutView(left, top, lastFull ? SPACE_NEED : SPACE_NO_NEED);
                }
                top += oneLine.mHeight + mVerticalSpacing;//为下一行的top赋值
            }
        }
    }

    /**
     * 还原所有数据
     */
    private void restoreLine() {
        mLines.clear();
        mLine = new Line();
        mUsedWidth = 0;
    }

    /**
     * 新增加一行
     */
    private boolean newLine() {
        mLines.add(mLine);
        if (mLines.size() < mMaxLinesCount) {
            mLine = new Line();
            mUsedWidth = 0;
            return true;
        }
        return false;
    }


    /**
     * 代表着一行，封装了一行所占高度，该行子View的集合，以及所有View的宽度总和
     */
    class Line {
        int mWidth = 0;// 该行中所有的子View累加的宽度
        int mHeight = 0;// 该行中所有的子View中高度的那个子View的高度
        List<View> views = new ArrayList<>();

        public void addView(View view) {// 往该行中添加一个
            views.add(view);
            mWidth += view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();
            mHeight = mHeight < childHeight ? childHeight : mHeight;//高度等于一行中最高的View
        }

        public int getViewCount() {
            return views.size();
        }

        public void layoutView(int l, int t) {// 布局
            layoutView(l, t, SPACE_NEED);
        }

        public void layoutView(int l, int t, int flag) {
            int left = l;
            int count = getViewCount();
            //总宽度
            int layoutWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            //剩余的宽度，是除了View和间隙的剩余空间
            int surplusWidth = layoutWidth - mWidth - mHorizontalSpacing * (count - 1);
            if (surplusWidth >= 0) {// 剩余空间
                // 采用float类型数据计算后四舍五入能减少int类型计算带来的误差
                int splitSpacing = flag == SPACE_NO_NEED ? 0 : (int) (surplusWidth / count + 0.5);
                for (int i = 0; i < count; i++) {
                    final View view = views.get(i);
                    int childWidth = view.getMeasuredWidth();
                    int childHeight = view.getMeasuredHeight();
                    //计算出每个View的顶点，是由最高的View和该View高度的差值除以2
                    int topOffset = (int) ((mHeight - childHeight) / 2.0 + 0.5);
                    if (topOffset < 0) {
                        topOffset = 0;
                    }
                    childWidth = childWidth + splitSpacing;
                    view.getLayoutParams().width = childWidth;
                    if (splitSpacing > 0) {//View的长度改变了，需要重新measure
                        //把剩余空间平均到每个View上
                        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                        view.measure(widthMeasureSpec, heightMeasureSpec);
                    }
                    //布局View
                    view.layout(left, t + topOffset, left + childWidth, t + topOffset + childHeight);
                    left += childWidth + mHorizontalSpacing; //为下一个View的left赋值
                }
            } else {
                if (count == 1) {
                    View view = views.get(0);
                    view.layout(left, t, left + view.getMeasuredWidth(), t + view.getMeasuredHeight());
                }
            }
        }
    }


}
