package com.example.capdtalk.etc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capdtalk.R;

import java.util.List;

public class BoardListAdapter extends BaseAdapter {

    private Context context;
    private List<Board> boardList;

    public BoardListAdapter(Context context, List<Board> boardList) {
        this.context = context;
        this.boardList = boardList;
    }

    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Object getItem(int i) {
        return boardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.listview_board,null);
        TextView board_title = (TextView) v.findViewById(R.id.board_title);
        TextView board_content = (TextView) v.findViewById(R.id.board_content);
        TextView board_name = (TextView) v.findViewById(R.id.board_name);
        TextView board_date = (TextView) v.findViewById(R.id.board_date);


        board_title.setText(boardList.get(i).getTitle());
        board_content.setText(boardList.get(i).getContent());
        board_name.setText(boardList.get(i).getName());
        board_date.setText(boardList.get(i).getDate());
        v.setTag(boardList.get(i).getTitle());
        return v;
    }
}
