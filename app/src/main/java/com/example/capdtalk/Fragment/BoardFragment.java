package com.example.capdtalk.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.Activity.BoardCommentActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.BoardRequest;
import com.example.capdtalk.etc.Board;
import com.example.capdtalk.etc.BoardListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BoardFragment extends Fragment{

    private ListView boardListView;
    private BoardListAdapter bAdapter;
    public List<Board> boardList;

    String[][] barray;
    int count;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("전체 게시판");

        boardListView = (ListView) view.findViewById(R.id.boardListView);
        boardList = new ArrayList<Board>();
        bAdapter = new BoardListAdapter(getActivity().getApplicationContext(),boardList);
        boardListView.setAdapter(bAdapter);

        setBoardListView();

        boardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BoardCommentActivity.class);
                intent.putExtra("title",boardList.get(position).getTitle());
                intent.putExtra("content",boardList.get(position).getContent());
                intent.putExtra("name",boardList.get(position).getName());
                intent.putExtra("date",boardList.get(position).getDate());
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
            }
       });
    }



    private void setBoardListView(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    count = 0;
                    barray = new String[jsonArray.length()][4];
                    bAdapter.notifyDataSetChanged();
                    while(count < jsonArray.length() ){
                        JSONObject object = jsonArray.getJSONObject(count);
                        barray[count][0] = object.getString("board_title");
                        barray[count][1] = object.getString("board_content");
                        barray[count][2] = object.getString("board_name");
                        barray[count][3] = object.getString("board_date");
                        count++;
                    }
                    for(int i=0;i<count;i++){
                        Board board  = new Board(barray[i][0],barray[i][1],barray[i][2],barray[i][3]);
                        boardList.add(board);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        BoardRequest boardRequest = new BoardRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(boardRequest);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board,container,false);
    }
}
