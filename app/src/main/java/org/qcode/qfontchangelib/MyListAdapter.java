package org.qcode.qfontchangelib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.qcode.fontchange.FontManager;

import java.util.ArrayList;

/**
 * qqliu
 * 2016/11/16.
 */

public class MyListAdapter extends BaseAdapter {

    private final Context mContext;
    private ArrayList<String> mList = new ArrayList<String>();

    public MyListAdapter(Context context) {
        mContext = context;
        for(int i = 0;i < 30; i++) {
            mList.add("测试数据" + i);
        }
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(null == convertView) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_item, null);
            holder = new ViewHolder();
            holder.textView =
                    (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //对convertView的所有子元素应用字体大小属性
        FontManager.getInstance().applyFont(convertView, true);

        //刷新数据
        holder.textView.setText(mList.get(position));

        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
    }
}
