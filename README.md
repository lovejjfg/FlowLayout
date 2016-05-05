![FlowLayout](https://raw.githubusercontent.com/lovejjfg/FlowLayout-master/master/flowLayout.gif)

这个流式布局点击及布局加载来源于 [hongyangAndroid/FlowLayout](https://github.com/hongyangAndroid/FlowLayout) 然后有以下的丰富:
 * 支持单选和多选模式：
  
	  mFlowLayout.setmSelectedMax(1);

 * 实现了两种布局模式:

> EXACT_MODE :精确模式，这个模式下，你需要指定每一行默认展示多少个条目。这个看起来是整齐的！这个比较适合那些字数比较固定的展示方式！

> FREE_MODE :自由模式，就是根据文字长度和剩余长度依次填充每一行，这个看起来就是很随意的，适合哪种长度变化很大没法做到统一一行几个条目的情况！

 * 最后一行的展示方式：

        mFlowLayout.setLastFull(false);//最后一个行是否填充整行显示
 * 单选和复选模式
       mFlowLayout.setmSelectedMax(int count); //最多的选中数量

            mFlowLayout = (NormalFlowLayout) findViewById(R.id.flow);
            mFlowLayout.setmSelectedMax(1);
            mFlowLayout.setTagAdapter(mTagAdapter);
            mFlowLayout.setOnSelectListener(this);
            //使用adapter实现布局的填充！！
            TagAdapter<String> mTagAdapter = new TagAdapter<String>(mVals) {
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
            public void onSelected(Set<Integer> selectPosSet) {

            }

            @Override
            public void onCheckChanged(TagView tagView, int position, boolean checked)     {
                TextView textView = (TextView) tagView.getTagView();
                textView.setTextColor(checked ? Color.BLUE : Color.WHITE);
                showText(this, ((TextView) (tagView.getTagView())).getText() + "：" + tagView.isChecked());
            }
