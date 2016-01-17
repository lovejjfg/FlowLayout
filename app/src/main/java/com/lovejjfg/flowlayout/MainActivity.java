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
            {
                    "堀口奈津美",
                    "村上里沙",
                    "月野姬",
                    "绫波优",
                    "圣",
                    "岬里沙",
                    "伊藤青叶",
                    "原更纱",
                    "高濑七海",
                    "南沙也香",
                    "早乙女露依",
                    "相田纱耶香",
                    "翼裕香",
                    "波多野结衣",
                    "田中亚弥",
                    "羽田未来",
                    "赤西凉",
                    "仁科沙也加",
                    "北原夏美",
                    "鲇川奈绪",
                    "白鸟凉子",
                    "若宫莉那",
                    "飞鸟伊央",
                    "绫濑美音",
                    "明日花绮罗",
                    "花井美沙",
                    "青山由衣",
                    "心有花",
                    "冬月枫",
                    "樱木凛",
                    "水城奈绪",
                    "大桥未久",
                    "石黑京香",
                    "濑名步",
                    "小泉梨菜",
                    "琴乃	琴",
                    "美优千奈",
                    "星乃星爱",
                    "爱原翼",
                    "铃木里美",
                    "七咲枫花",
                    "初音实",
                    "本多成实",
                    "星优乃",
                    "饭岛玖罗",
                    "石仓咏美",
                    "佐伯奈奈",
                    "佐山爱",
                    "仙道春奈",
                    "茉莉桃",
                    "樱井优子",
                    "石川施恩惠",
                    "霞理沙",
                    "苍井空"
            };


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
