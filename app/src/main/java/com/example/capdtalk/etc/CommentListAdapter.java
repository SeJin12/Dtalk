package com.example.capdtalk.etc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capdtalk.R;

import java.util.List;

public class CommentListAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> commentList;

    public CommentListAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.listview_comment,null);
        TextView lc_content = (TextView) v.findViewById(R.id.lc_content);
        TextView lc_name = (TextView) v.findViewById(R.id.lc_name);
        TextView lc_date = (TextView) v.findViewById(R.id.lc_date);


        lc_content.setText(commentList.get(i).getCcontent());
        lc_name.setText(commentList.get(i).getCname());
        lc_date.setText(commentList.get(i).getCdate());
        v.setTag(commentList.get(i).getCname());
        return v;
    }
}
