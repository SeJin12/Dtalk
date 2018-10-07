package com.example.capdtalk.etc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capdtalk.R;

import java.util.List;

public class NoticeListAdapter extends BaseAdapter {

    private Context context;
    private List<Notice> noticeList;

    public NoticeListAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public int getCount() {
        return noticeList.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.listview_notice,null);
        TextView notice_title = (TextView) v.findViewById(R.id.notice_title);
        TextView notice_content = (TextView) v.findViewById(R.id.notice_content);
        TextView notice_name = (TextView) v.findViewById(R.id.notice_name);
        TextView notice_date = (TextView) v.findViewById(R.id.notice_date);

        notice_title.setText(noticeList.get(i).getTitle());
        notice_content.setText(noticeList.get(i).getContent());
        notice_name.setText(noticeList.get(i).getName());
        notice_date.setText(noticeList.get(i).getDate());

        v.setTag(noticeList.get(i).getTitle()); // 무슨 뜻이지?
        return v;
    }
}
