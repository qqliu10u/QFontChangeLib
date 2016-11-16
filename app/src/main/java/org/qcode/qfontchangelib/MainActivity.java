package org.qcode.qfontchangelib;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.qcode.fontchange.FontManager;
import org.qcode.fontchange.IFontChangeListener;

public class MainActivity extends BaseActivity {

    private TextView mMainTextView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(this);

        //代码内设置TextView的文字大小
        FontManager
                .with(mMainTextView)
                .setFontSize(20)
                .applyFont();
    }

    private void initView(Context context) {
        mMainTextView = (TextView)findViewById(R.id.main_textView);
        mListView = (ListView)findViewById(R.id.main_listview);
        mListView.setAdapter(new MyListAdapter(context));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_small:
                FontManager.getInstance().changeFontSize(0.8f, mListener);
                break;
            case R.id.btn_normal:
                FontManager.getInstance().changeFontSize(1f, mListener);
                break;
            case R.id.btn_big:
                FontManager.getInstance().changeFontSize(1.2f, mListener);
                break;
            case R.id.btn_biggest:
                FontManager.getInstance().changeFontSize(1.6f, mListener);
                break;
        }
    }

    private IFontChangeListener mListener = new IFontChangeListener() {
        @Override
        public void onLoadStart(float scale) {
        }

        @Override
        public void onLoadSuccess(float scale) {
//            Toast.makeText(MainActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLoadFail(float scale) {
//            Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
        }
    };
}
