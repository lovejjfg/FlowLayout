package com.lovejjfg.flowlayout;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lovejjfg.flowlayout_lib.FlowLayout;
import com.lovejjfg.flowlayout_lib.NormalFlowLayout;
import com.lovejjfg.flowlayout_lib.TagAdapter;
import com.lovejjfg.flowlayout_lib.TagView;

import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NormalFlowLayout.OnSelectListener, NormalFlowLayout.OnTagClickListener {
    private static final String TAG = "FLOW";
    private static Toast result;
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView", "Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NormalFlowLayout mFlowLayout = (NormalFlowLayout) findViewById(R.id.flow);
        mFlowLayout.setTagAdapter(mTagAdapter);
        mFlowLayout.setOnTagClickListener(this);
        mFlowLayout.setOnSelectListener(this);
//        mFlowLayout.setmSelectedMax(3);
    }

    TagAdapter<String> mTagAdapter = new TagAdapter<String>(mVals) {
        final Random random = new Random();

        @Override
        public View getView(FlowLayout parent, int position, String strings) {
            TextView textView = new TextView(MainActivity.this);

            textView.setGravity(Gravity.CENTER);

            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

            textView.setTextColor(Color.WHITE);

            textView.setBackgroundResource(R.drawable.check_selector);

            textView.setText(mVals[position]);

            return textView;
        }

        @Override
        public int getCount() {
            return mVals.length;
        }
    };

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        TagView tagView = (TagView) view;

        showText(this, ((TextView) (tagView.getTagView())).getText() + "：" + tagView.isChecked());

        TextView textView = (TextView) tagView.getTagView();
        textView.setTextColor(tagView.isChecked() ? Color.BLUE : Color.WHITE);
        return false;
    }

    @Override
    public void onSelected(Set<Integer> selectPosSet) {

    }

    public void showText(Context context, String text) {
        if (null == result) {
            result = new Toast(context);
        }
        TextView textView = new TextView(this);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        result.setView(textView);
        result.setDuration(Toast.LENGTH_SHORT);
        result.show();
    }
}
